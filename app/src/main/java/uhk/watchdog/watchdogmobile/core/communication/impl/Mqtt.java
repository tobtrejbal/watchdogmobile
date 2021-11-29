package uhk.watchdog.watchdogmobile.core.communication.impl;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

import uhk.watchdog.watchdogmobile.core.communication.CommunicationManager;
import uhk.watchdog.watchdogmobile.core.communication.CommunicationListener;
import uhk.watchdog.watchdogmobile.core.communication.constants.NetworkCommunication;
import uhk.watchdog.watchdogmobile.core.flatbuffer.Transformer;
import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by tobou on 15.10.2016.
 */

public class Mqtt implements MqttCallback, CommunicationManager {

    int qos             = 0;
    String broker       ;

    String[] topics;
    int[] quos;

    MqttAsyncClient client;

    CommunicationListener communicationListener;

    MemoryPersistence persistence;

    MqttConnectOptions connOpts = new MqttConnectOptions();

    public Mqtt(String address, String clientId, String[] topics, int[] quos) {
        try {
            this.persistence = new MemoryPersistence();
            this.broker = address;
            this.client = new MqttAsyncClient(address, clientId, persistence);
            this.connOpts = new MqttConnectOptions();
            this.client.setCallback(this);
            this.topics = topics;
            this.quos = quos;
        } catch (MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        communicationListener.connectionLost();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        communicationListener.messageArrived(s, mqttMessage.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        communicationListener.messageSuccess();
    }

    @Override
    public void setCommunicationListener(CommunicationListener communicationListener) {
        this.communicationListener = communicationListener;
    }

    public void connect(String username, String password) {
        if (!client.isConnected()) {
            System.out.println("Connecting to broker: " + broker);
            connOpts.setCleanSession(true);
            //connOpts.setUserName(username);
            //connOpts.setPassword(password.toCharArray());
            try {
                client.connect(connOpts, null, new IMqttActionListener() {

                    public void onSuccess(IMqttToken iMqttToken) {
                        try {
                            client.subscribe(topics, quos);
                            communicationListener.connectionSuccess();
                        } catch (MqttException e) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                            e.printStackTrace();
                            communicationListener.connectionFail();
                        }
                    }

                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        throwable.printStackTrace();
                        communicationListener.connectionFail();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                communicationListener.connectionFail();
            }

        }
    }

    private void sendMessage(String topic, byte[] content) {
        try {
            MqttMessage message = new MqttMessage(content);
            System.out.println("odesláno");
            client.publish(topic, message);
            System.out.println("odesláno2"+topic);
        } catch (MqttException ex) {
            ex.printStackTrace();
            if(topic.equals(NetworkCommunication.MQTT_SEND_DATA_TOPIC)) {
                communicationListener.dataSendFail(Transformer.fbDataToObjects(content));
            } else {
                communicationListener.messageFail(topic, content);
            }
        }
    }

    @Override
    public void sendData(List<Sample> data) {
        sendMessage(NetworkCommunication.MQTT_SEND_DATA_TOPIC, Transformer.objectToDataFB(data));
    }

    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

}