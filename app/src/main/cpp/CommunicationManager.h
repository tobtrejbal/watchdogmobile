//
// Created by Tobous on 19.02.2017.
//

#ifndef WATCHDOGMOBILE_COMMUNICATIONMANAGER_H
#define WATCHDOGMOBILE_COMMUNICATIONMANAGER_H

#include <string>
#include <vector>
#include <string>
#include "Sample.h"
#include "paho/MQTTClient.h"


class CommunicationListener {

public:

    virtual void connectionSuccessful() =0;
    virtual void connectionLost() =0;
    virtual void messageArrived(char* topic) =0;
    virtual void messageDelivered() =0;
};

class CommunicationManager {

public:

    virtual void connect(std::string userName, std::string password) = 0;
    virtual void disconnect() = 0;
    virtual void sendData(std::vector<Sample*> data) = 0;
    virtual void setCommunicationListener(CommunicationListener *listener) = 0;

    CommunicationListener *listener;
};

class CommunicationManagerImpl : public CommunicationManager {

public:

    CommunicationManagerImpl(const char* serverAddress, const char* clientID, std::vector<std::string> topics, int qos[]);

    void connect(std::string userName, std::string password) override;
    void disconnect() override;
    void sendData(std::vector<Sample*> data) override;

    void setCommunicationListener(CommunicationListener *listener) override;
private:

    MQTTClient client;
    volatile MQTTClient_deliveryToken deliveredtoken;
    std::string broker;
    std::vector<char*> topics;
    int qos[];

    void sendMessage(char * topic, char *data, int length);

    static void delivered(void *context, MQTTClient_deliveryToken dt);
    static int msgarrvd(void *context, char *topicName, int topicLen, MQTTClient_message *message);
    static void connlost(void *context, char *cause);
};

#endif //WATCHDOGMOBILE_COMMUNICATIONMANAGER_H
