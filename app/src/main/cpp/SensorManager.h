//
// Created by Tobous on 19.02.2017.
//

#ifndef WATCHDOGMOBILE_SENSORMANAGER_H
#define WATCHDOGMOBILE_SENSORMANAGER_H

#include "Sample.h"
#include <map>
#include <thread>
#include <unistd.h>
#include <android/asset_manager_jni.h>
#include <android/sensor.h>


class SensorManager {

public:
    // pure virtual function
    virtual void startListening() = 0;
    virtual void stopListening() = 0;
    virtual Sample * getSample() = 0;
private:

};

class SensorManagerImpl : public SensorManager {

public:
    SensorManagerImpl();
    // pure virtual function
    void startListening() override;
    void stopListening() override;
    Sample * getSample() override;
private:

    std::thread sensorThread;
    ASensorManager *sensorManager;
    const ASensor *accelerometer;
    ASensorEventQueue *accelerometerEventQueue;
    ALooper *looper;
    std::map<int,float*> sensorValues;

    const static int LOOPER_ID_USER = 3;
    const static int SENSOR_HISTORY_LENGTH = 100;
    const static int SENSOR_REFRESH_RATE_HZ = 100;
    constexpr static int32_t SENSOR_REFRESH_PERIOD_US = int32_t(1000000 / SENSOR_REFRESH_RATE_HZ);

    void registerInternalSensors();
    void getSensorValuesRunnable();
    void getSensorValuesInternal();
};


#endif //WATCHDOGMOBILE_SENSORMANAGER_H
