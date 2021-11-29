//
// Created by Tobous on 24.02.2017.
//

#include "TransformerFB.h"
#include "android/log.h"
#include <sstream>

void objectToDataFB(std::vector<Sample*> &samples, uint8_t* &dataPointer, int &size) {

    flatbuffers::FlatBufferBuilder fbb;

    std::string txt = "velikost dat je : ";
    std::stringstream sstm;
    sstm << txt << samples.size();
    txt = sstm.str();
    __android_log_print(ANDROID_LOG_DEBUG, "ANALYZING DATAAAASSSSSSSS","%s",txt.c_str());


    std::vector<flatbuffers::Offset<watchdog::SampleFB>> sampleOffsets;

    for (int i = 0; i < samples.size(); i++) {
        sampleOffsets.push_back(createSampleFB(fbb, samples[i]));
    }

    auto userIdOffset = fbb.CreateString("tobous");
    auto sampleOffset = fbb.CreateVector(sampleOffsets);

    auto offset = watchdog::CreateDataFB(fbb, userIdOffset, sampleOffset);

    fbb.Finish(offset);

    dataPointer = fbb.GetBufferPointer();
    size = fbb.GetSize();
}


flatbuffers::Offset<watchdog::SampleFB> createSampleFB(flatbuffers::FlatBufferBuilder &fbb, Sample* sample) {

    std::vector<flatbuffers::Offset<watchdog::SensorValueFB>> offsets;

    for (auto const& x : sample->getSensorValues()) {
        offsets.push_back(createSensorValueFB(fbb, x.second));
    }

    auto sensorOffset = fbb.CreateVector(offsets);
    auto offset = watchdog::CreateSampleFB(fbb, sample->getTimestamp(), sample->getTimestamp(), sample->getLat(), sample->getLon(), sensorOffset);

    return offset;
}

flatbuffers::Offset<watchdog::SensorValueFB> createSensorValueFB(flatbuffers::FlatBufferBuilder &fbb, SensorValue* sensorValue) {

    auto valuesOffset = fbb.CreateVector(sensorValue->getSensorValues(),sensorValue->getValuesSize());

    flatbuffers::Offset<watchdog::SensorValueFB> offset = watchdog::CreateSensorValueFB(fbb, sensorValue->getSensorID(), valuesOffset);

    return offset;
}
