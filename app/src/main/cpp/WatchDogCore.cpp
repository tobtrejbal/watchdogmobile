//
// Created by Tobous on 19.02.2017.
//

#include "WatchDogCore.h"
#include <android/log.h>
#include <unistd.h>
#include <sstream>
#include <thread>
#include <vector>


WatchDogCore::WatchDogCore() {

    std::vector<std::string> topics = {"watchdog_bubak", "watchdog_bubak3"};

    int qos[2] = {0,0};

    buffer = new BufferImpl(50);

    sensorManager = new SensorManagerImpl();

    communicationManager = new CommunicationManagerImpl("tcp://imitgw.uhk.cz:59708", "tobous", topics, qos);

    monitor = new MonitorImpl();

    communicationManager->setCommunicationListener(this);
}

void WatchDogCore::start() {

    communicationManager->connect("dasd","dsad");

    sensorManager->startListening();

    takeSampleThread = std::thread([=] {takeSampleRunnable();});
    analyzeThread = std::thread([=] {analyzeRunnable();});
    clearBufferThread = std::thread([=] {clearBufferRunnable();});
    clearDatabaseThread = std::thread([=] {clearDatabaseRunnable();});

}

void WatchDogCore::takeSampleRunnable() {
    while(1) {
        takeSample();
        usleep(4000);
    }
}

void WatchDogCore::analyzeRunnable() {
    while(1) {
        __android_log_print(ANDROID_LOG_DEBUG, "ANALYZE","analyzing");
        analyzeData();
        usleep(20000);
    }
}

void WatchDogCore::clearBufferRunnable() {
    while(1) {
        clearBuffer();
        __android_log_print(ANDROID_LOG_DEBUG, "CLEAR BUFFER RUNNABLE","clearing buffer");
        usleep(20000);
    }
}

void WatchDogCore::clearDatabaseRunnable() {
    while(1) {
        __android_log_print(ANDROID_LOG_DEBUG, "CLEAR DATABASE RUNNABLE","clearing database");
        usleep(60000000);
    }
}

void WatchDogCore::connectionLost() {

}

void WatchDogCore::connectionSuccessful() {

}

void WatchDogCore::messageArrived(char *topic) {
    __android_log_print(ANDROID_LOG_DEBUG, "MESSAGE ARRIVED","%s",topic);
    //__android_log_print(ANDROID_LOG_DEBUG, "MESSAGE ARRIVED","%i",std::this_thread::get_id());
}

void WatchDogCore::messageDelivered() {
    __android_log_print(ANDROID_LOG_DEBUG, "MESSAGE DELIVERED","%s","delivered");
}


void WatchDogCore::takeSample() {
    buffer->addSampleToAnalyze(sensorManager->getSample());
    if(appMode) {
       buffer->addSampleSend(sensorManager->getSample());
    }
}

void WatchDogCore::analyzeData() {
    buffer->getSamplesToAnalyze();
    usleep(100000);
    buffer->analyzeCompleted();
    __android_log_print(ANDROID_LOG_DEBUG, "MESSAGE DELIVERED","%s","switched");
}

void WatchDogCore::clearBuffer() {
    if(appMode) {
        if (buffer->sizeSend() > 0) {
            std::vector<Sample*> data = buffer->clearBufferSend();
            if (online) {
                communicationManager->sendData(data);
                for(Sample* sample : data) {
                    delete sample;
                }
            } else {
                lastSuccess = 0;
                databaseManager->insertData(data);
                databaseCount = databaseManager->getDataNumber();
            }
        }
    }
}

void WatchDogCore::clearDatabase() {
    if(appMode) {
        databaseCount = databaseManager->getDataNumber();
        if(databaseCount > 0) {
            if (online) {
                communicationManager->sendData(databaseManager->getData(200));
                databaseCount = databaseManager->getDataNumber();
            }
        }
    } else {
        databaseManager->clearDatabase();
    }
}