//
// Created by Tobous on 24.02.2017.
//

#ifndef WATCHDOGMOBILE_TRANSFORMERFB_H
#define WATCHDOGMOBILE_TRANSFORMERFB_H


#include "Sample.h"
#include "flatbuffers/objects/DataFB_generated.h"
#include "flatbuffers/objects/SampleFB_generated.h"
#include "flatbuffers/objects/SensorValueFB_generated.h"

#include "flatbuffers/flatbuffers.h"
#include "flatbuffers/hash.h"
#include "flatbuffers/idl.h"
#include "flatbuffers/reflection.h"
#include "flatbuffers/reflection_generated.h"
#include "flatbuffers/util.h"

#include <vector>

void objectToDataFB(std::vector<Sample*> &samples, uint8_t* &dataPointer, int &size);

flatbuffers::Offset<watchdog::SampleFB> createSampleFB(flatbuffers::FlatBufferBuilder &fbb, Sample* sample);

flatbuffers::Offset<watchdog::SensorValueFB> createSensorValueFB(flatbuffers::FlatBufferBuilder &fbb, SensorValue* sensorValue);

class TransformerFB {

};


#endif //WATCHDOGMOBILE_TRANSFORMERFB_H
