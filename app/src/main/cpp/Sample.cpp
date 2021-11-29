//
// Created by Tobous on 19.02.2017.
//

#include "Sample.h"
#include "SensorValue.h"

Sample::Sample(long timeStamp, std::map<int,SensorValue*> sensorValues) {
    this->timeStamp = timeStamp;
    this->sensorValues = sensorValues;
}

Sample::~Sample(void) {
    for (auto const &x : sensorValues) {
        free(x.second);
    }
}
