//
// Created by Tobous on 20.02.2017.
//

#ifndef WATCHDOGMOBILE_MONITOR_H
#define WATCHDOGMOBILE_MONITOR_H


#include "Sample.h"
#include <queue>

class Monitor {

public:

    virtual void analyze(std::queue<Sample*> data) = 0;

private:

};

class MonitorImpl : public Monitor {

public:

    MonitorImpl();

    void analyze(std::queue<Sample*> data);

private:

};

#endif //WATCHDOGMOBILE_MONITOR_H
