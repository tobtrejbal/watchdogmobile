//
// Created by Tobous on 19.02.2017.
//

#include "SensorManager.h"
#include <chrono>
#include <sstream>
#include "android/log.h"

SensorManagerImpl::SensorManagerImpl() {
    sensorManager = ASensorManager_getInstance();
    accelerometer = ASensorManager_getDefaultSensor(sensorManager, ASENSOR_TYPE_ACCELEROMETER);
}

void SensorManagerImpl::startListening() {
    sensorThread = std::thread([=] {getSensorValuesRunnable();});
}

void SensorManagerImpl::stopListening() {
    ASensorEventQueue_disableSensor(accelerometerEventQueue, accelerometer);
}

void SensorManagerImpl::getSensorValuesRunnable() {
    registerInternalSensors();
    while(1) {
        getSensorValuesInternal();
        usleep(1000);
    }
}

void SensorManagerImpl::registerInternalSensors() {
    looper = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);
    accelerometerEventQueue = ASensorManager_createEventQueue(sensorManager, looper, LOOPER_ID_USER, NULL, NULL);
    ASensorEventQueue_enableSensor(accelerometerEventQueue, accelerometer);
    auto status = ASensorEventQueue_setEventRate(accelerometerEventQueue, accelerometer, SENSOR_REFRESH_PERIOD_US);
}

void SensorManagerImpl::getSensorValuesInternal() {
    ALooper_pollAll(0, NULL, NULL, NULL);
    ASensorEvent event;
    while (ASensorEventQueue_getEvents(accelerometerEventQueue, &event, 1) > 0) {
        /*std::string txt = "velikost dat je : ";
        std::stringstream sstm;
        sstm << txt << event.acceleration.x;
        sstm << txt << ":";
        sstm << txt << event.acceleration.y;
        sstm << txt << ":";
        sstm << txt << event.acceleration.z;
        txt = sstm.str();
        __android_log_print(ANDROID_LOG_DEBUG, "VALUE", "%s", txt.c_str());*/
        sensorValues[event.type] = event.data;
    }
}

Sample * SensorManagerImpl::getSample() {
    using namespace std::chrono;
    milliseconds ms = duration_cast< milliseconds >(
            system_clock::now().time_since_epoch()
    );

    std::map<int,SensorValue*> valuesTemp;
    for (auto const& x : sensorValues) {
        valuesTemp[x.first] = new SensorValue(x.first, x.second);
    }
    Sample * sample = new Sample(ms.count(), valuesTemp);
    return sample;
}
