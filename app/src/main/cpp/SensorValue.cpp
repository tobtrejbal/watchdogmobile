//
// Created by Tobous on 19.02.2017.
//

#include "SensorValue.h"
#include "android/log.h"
#include <sstream>

SensorValue::SensorValue(int sensorID, float sensorValues[]) {
    this->sensorID = sensorID;
    this->valuesSize = 5;
    this->sensorValues = new float[valuesSize];
    for(int i = 0; i < valuesSize; i++) {
        this->sensorValues[i] = sensorValues[i];
    }
}

SensorValue::SensorValue(const SensorValue& value) {
    sensorValues = new float[value.valuesSize];
    for(int i = 0; i < value.valuesSize; i++)
    {
        sensorValues[i] = value.sensorValues[i];
    }
    //atd, vse prekopirovat
}

SensorValue::~SensorValue(void) {
    delete [] sensorValues;
}