package uhk.watchdog.watchdogmobile.core.communication;

import java.util.List;

import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by tobou on 15.10.2016.
 */

public interface CommunicationListener {

    void connectionSuccess();
    void connectionFail();
    void connectionLost();
    void messageArrived(String s, byte[] content);
    void messageSuccess();
    void messageFail(String type, byte[] message);
    void dataSendFail(List<Sample> samples);


}
