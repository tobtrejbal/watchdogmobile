//
// Created by Tobous on 19.02.2017.
//

#ifndef WATCHDOGMOBILE_WATCHDOGCORE_H
#define WATCHDOGMOBILE_WATCHDOGCORE_H


#include <thread>
#include "SensorManager.h"
#include "Buffer.h"
#include "CommunicationManager.h"
#include "DatabaseManager.h"
#include "Monitor.h"

typedef void connectionSuccessful();
typedef void connectionLost();
typedef void messageArrived();
typedef void messageDelivered();

class WatchDogCore : public CommunicationListener {

public:

    /*static WatchDogCore *instance() {
        if (s_instance == nullptr) {
            s_instance = new WatchDogCore;
        }
        return s_instance;
    }*/

    WatchDogCore();

    virtual void start();

private:

    //WatchDogCore();

    void takeSample();
    void analyzeData();
    void clearBuffer();
    void clearDatabase();

    void takeSampleRunnable();
    void analyzeRunnable();
    void clearBufferRunnable();
    void clearDatabaseRunnable();

    void connectionSuccessful() override;
    void connectionLost() override;
    void messageArrived(char *topic) override;
    void messageDelivered() override;

    //static WatchDogCore *s_instance = nullptr;

    Buffer * buffer;
    SensorManager * sensorManager;
    CommunicationManager * communicationManager;
    DatabaseManager * databaseManager;
    Monitor * monitor;

    std::thread takeSampleThread;
    std::thread analyzeThread;
    std::thread clearBufferThread;
    std::thread clearDatabaseThread;

    int appMode = 1;
    int online = 1;
    int lastSuccess = 0;
    int databaseCount = 0;

};


#endif //WATCHDOGMOBILE_WATCHDOGCORE_H
