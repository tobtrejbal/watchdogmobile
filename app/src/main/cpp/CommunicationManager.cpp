//
// Created by Tobous on 19.02.2017.
//

#include "CommunicationManager.h"
#include <thread>
#include "android/log.h"
#include "TransformerFB.h"
#include "paho/MQTTClient.h"


CommunicationManagerImpl::CommunicationManagerImpl(const char* serverAddress, const char* clientID,
                                                   std::vector<std::string> topics, int qos[]) {

    /*std::string stringyPyco[] = {"abc", "xyz"};
    char *const topics[] = (char* const*) malloc(sizeof(char *) *2);
    for(int i = 0; i < 2; i++) {
        polePyco[i] = stringyPyco[i].c_str();
    }

    std::vector<char*> filts =
            MQTTAsync_subscribeMany(cli_, (int) topicFilters.size(),
                                    (char**) &filts[0], (int*) &qos[0], &opts);


    std::string str;
    const char * c = str.c_str();



     for (const auto& t : topicFilters) {
		char* filt = new char[t.size()+1];
		std::strcpy(filt, t.c_str());
		filts.push_back(filt);
	}

     */

    this->topics;

    for(int i = 0; i < topics.size(); i++) {
        this->topics.push_back(const_cast<char *>(topics[i].c_str()));
        this->qos[i] = qos[i];
    }

#define PAYLOAD     "Hello World!"

    MQTTClient_create(&client, serverAddress, clientID, MQTTCLIENT_PERSISTENCE_NONE, this);
    MQTTClient_setCallbacks(client, this, &CommunicationManagerImpl::connlost, &CommunicationManagerImpl::msgarrvd, &CommunicationManagerImpl::delivered);
}

void CommunicationManagerImpl::delivered(void *context, MQTTClient_deliveryToken dt) {
    printf("Message with token value %d delivery confirmed\n", dt);

    __android_log_print(ANDROID_LOG_DEBUG, "MESSAGE DELIVERED","%s","delivered");

    CommunicationManagerImpl *thiz = (CommunicationManagerImpl *)context;
    thiz->listener->messageDelivered();
}

void CommunicationManagerImpl::setCommunicationListener(CommunicationListener *listener) {
    this->listener = listener;
}

int CommunicationManagerImpl::msgarrvd(void *context, char *topicName, int topicLen, MQTTClient_message *message) {
    int i;
    char* payloadptr;

    printf("Message arrived\n");
    printf("     topic: %s\n", topicName);
    printf("   message: ");

    payloadptr = (char*) message->payload;
    for(i=0; i<message->payloadlen; i++) {
        putchar(*payloadptr++);
    }
    putchar('\n');
    MQTTClient_freeMessage(&message);
    MQTTClient_free(topicName);

    CommunicationManagerImpl *thiz = (CommunicationManagerImpl *)context;
    thiz->listener->messageArrived(topicName);

    return 1;
}

void CommunicationManagerImpl::connlost(void *context, char *cause) {
    printf("\nConnection lost\n");
    printf("     cause: %s\n", cause);

    CommunicationManagerImpl *thiz = (CommunicationManagerImpl *)context;
    thiz->listener->connectionLost();
}

void CommunicationManagerImpl::connect(std::string userName, std::string password) {

    MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
    conn_opts.keepAliveInterval = 20;
    conn_opts.connectTimeout = 10;
    conn_opts.cleansession = 1;

    __android_log_print(ANDROID_LOG_DEBUG, "CONNECTING","%i",std::this_thread::get_id());

    int returnCode;

    if ((returnCode = MQTTClient_connect(client, &conn_opts)) != MQTTCLIENT_SUCCESS) {
        printf("Failed to connect, return code %d\n", returnCode);
    }

    MQTTClient_subscribeMany(client, topics.size(), (char**) &topics[0], &qos[0]);

    listener->connectionSuccessful();
}

void CommunicationManagerImpl::disconnect() {
    MQTTClient_disconnect(client, 10000);
    MQTTClient_destroy(&client);
}

void CommunicationManagerImpl::sendData(std::vector<Sample*> data) {
    __android_log_print(ANDROID_LOG_DEBUG, "SENDING MESSAGE","%i",std::this_thread::get_id());
    uint8_t * payloadPointer;
    int size;
    objectToDataFB(data, payloadPointer, size);
    sendMessage("watchdog_data", (char *) payloadPointer, size);
}

void CommunicationManagerImpl::sendMessage(char * topic, char *data, int length) {
    MQTTClient_message pubmsg = MQTTClient_message_initializer;
    MQTTClient_deliveryToken token;
    pubmsg.payload = data;
    pubmsg.payloadlen = length;
    pubmsg.qos = 0;
    pubmsg.retained = 0;
    deliveredtoken = 0;
    MQTTClient_publishMessage(client, topic, &pubmsg, &token);
}