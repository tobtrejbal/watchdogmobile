//
// Created by Tobous on 19.02.2017.
//

#ifndef WATCHDOGMOBILE_BUFFER_H
#define WATCHDOGMOBILE_BUFFER_H

#include "Sample.h"
#include <queue>
#include <vector>
#include <mutex>

class Buffer {

public:

    virtual void addSampleToAnalyze(Sample* sample) = 0;
    virtual std::queue<Sample*> getSamplesToAnalyze() = 0;
    virtual void analyzeCompleted() = 0;
    virtual void addSampleSend(Sample* sample) = 0;
    virtual std::vector<Sample*> clearBufferSend() = 0;
    virtual int sizeSend() = 0;

private:

};


class BufferImpl : public Buffer {

public:

    BufferImpl(int analyzeBufferSize);

    void addSampleToAnalyze(Sample* sample);
    std::queue<Sample*> getSamplesToAnalyze();
    void analyzeCompleted();
    void addSampleSend(Sample* sample);
    std::vector<Sample*> clearBufferSend();
    int sizeSend();

private:

    int analyzeBufferSize;
    int primaryBuffer = 1;
    int primaryBufferSend = 1;

    void addSample(std::queue<Sample*> &buffer, Sample* sample);
    void switchBuffers();

    std::queue<Sample*> analyzeBuffer;
    std::queue<Sample*> analyzeBufferSecondary;
    std::vector<Sample*> sendBuffer;
    std::vector<Sample*> sendBufferSecondary;

    std::mutex analyzeBufferMutex;
    std::mutex sendBufferMutex;

};


#endif //WATCHDOGMOBILE_BUFFER_H
