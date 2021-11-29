//
// Created by Tobous on 19.02.2017.
//

#ifndef WATCHDOGMOBILE_SENSORVALUE_H
#define WATCHDOGMOBILE_SENSORVALUE_H


class SensorValue {

public:
    SensorValue(int sensorID, float values[]);
    SensorValue(const SensorValue& value);
    ~SensorValue();

    int getSensorID() { return sensorID; }
    float* getSensorValues() { return sensorValues; }
    int getValuesSize() {return valuesSize;}

private:
    int sensorID;
    float *sensorValues;
    int valuesSize;


};


#endif //WATCHDOGMOBILE_SENSORVALUE_H
