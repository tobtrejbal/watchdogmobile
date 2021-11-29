//
// Created by Tobous on 19.02.2017.
//

#ifndef WATCHDOGMOBILE_SAMPLE_H
#define WATCHDOGMOBILE_SAMPLE_H

#include <map>
#include "SensorValue.h"

class Sample {

public:
    Sample(long timeStamp, std::map<int,SensorValue*> sensorValues);
    ~Sample();

    int getTimestamp() { return timeStamp; }
    int getBatteryLevel() { return batteryLevel; }
    double getLat() { return lat; }
    double getLon() { return lon; }
    std::map<int,SensorValue*> getSensorValues() { return sensorValues; }

private:
    long timeStamp;
    int batteryLevel;
    double lat;
    double lon;
    std::map<int,SensorValue*> sensorValues;
};

#endif //WATCHDOGMOBILE_SAMPLE_H
