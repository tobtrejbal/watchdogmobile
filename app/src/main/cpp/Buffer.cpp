//
// Created by Tobous on 19.02.2017.
//

#include "Buffer.h"
#include <android/log.h>
#include <string>
#include <sstream>
#include <unistd.h>

BufferImpl::BufferImpl(int analyzeBufferSize) {
    this->analyzeBufferSize = analyzeBufferSize;
}

/**
 * Public methods
 */
void BufferImpl::addSampleToAnalyze(Sample* sample) {
    analyzeBufferMutex.lock();
    if(primaryBuffer) {
        //__android_log_print(ANDROID_LOG_DEBUG, "CLEAR BUFFER RUNNABLE","adding to primary");
        addSample(analyzeBuffer, sample);
    } else {
        //__android_log_print(ANDROID_LOG_DEBUG, "CLEAR BUFFER RUNNABLE","adding to secondary");
        addSample(analyzeBufferSecondary, sample);
    }
    analyzeBufferMutex.unlock();
}

std::queue<Sample*> BufferImpl::getSamplesToAnalyze() {
    analyzeBufferMutex.lock();
    primaryBuffer = 0;
    analyzeBufferMutex.unlock();
    return analyzeBuffer;
}

void BufferImpl::analyzeCompleted() {
    __android_log_print(ANDROID_LOG_DEBUG, "CLEAR BUFFER RUNNABLE","switching buffers");
    analyzeBufferMutex.lock();
    switchBuffers();
    analyzeBufferMutex.unlock();
}


void BufferImpl::addSampleSend(Sample* sample) {
    sendBufferMutex.lock();
    if(primaryBufferSend) {
        sendBuffer.push_back(sample);
    } else {
        sendBufferSecondary.push_back(sample);
    }
    sendBufferMutex.unlock();
}

std::vector<Sample*> BufferImpl::clearBufferSend() {
    sendBufferMutex.lock();
    if(primaryBufferSend) {
        primaryBufferSend ^= true;
        sendBufferSecondary.clear();
        sendBufferMutex.unlock();
        return sendBuffer;
    } else {
        primaryBufferSend ^= true;
        sendBuffer.clear();
        sendBufferMutex.unlock();
        return sendBufferSecondary;
    }
}

int BufferImpl::sizeSend() {
    sendBufferMutex.lock();
    int size = sendBuffer.size();
    sendBufferMutex.unlock();
    return size;
}


/**
 * Private methods
 */
void BufferImpl::addSample(std::queue<Sample*> &buffer, Sample* sample) {
    buffer.push(sample);
    if(buffer.size() > analyzeBufferSize) {
       // __android_log_print(ANDROID_LOG_DEBUG, "CLEAR BUFFER RUNNABLE","adsffdgfdgfdgfdg");
        Sample* sample1 = buffer.front();
        buffer.pop();
        delete sample1;
    }
}

void BufferImpl::switchBuffers() {
    if(!primaryBuffer) {
        while (!analyzeBufferSecondary.empty()) {
            //__android_log_print(ANDROID_LOG_DEBUG, "CLEAR BUFFER RUNNABLE","COPYYYYYYYYY");
            addSample(analyzeBuffer, analyzeBufferSecondary.front());
            analyzeBufferSecondary.pop();
        }
        std::string txt = "velikost dat je : ";
        std::stringstream sstm;
        sstm << txt << analyzeBufferSecondary.size();
        txt = sstm.str();
        //__android_log_print(ANDROID_LOG_DEBUG, "OLEHO","%s",txt.c_str());
    }
    primaryBuffer = 1;
}
