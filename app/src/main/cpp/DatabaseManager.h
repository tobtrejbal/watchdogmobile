//
// Created by Tobous on 19.02.2017.
//

#ifndef WATCHDOGMOBILE_DATABASEMANAGER_H
#define WATCHDOGMOBILE_DATABASEMANAGER_H


#include "Sample.h"
#include <vector>

class DatabaseManager {

public:

    virtual int getDataNumber() = 0;
    virtual void insertData(std::vector<Sample*> data) = 0;
    virtual std::vector<Sample*> getData(int rowNumber) = 0;
    virtual void clearDatabase() = 0;
    virtual void open() = 0;
    virtual void close() = 0;

private:

};

class DatabaseManagerImpl : public DatabaseManager {

public:

    DatabaseManagerImpl();

    int getDataNumber();
    void insertData(std::vector<Sample*> data);
    std::vector<Sample*> getData(int rowNumber);
    void clearDatabase();
    void open();
    void close();

private:



};

#endif //WATCHDOGMOBILE_DATABASEMANAGER_H
