package uhk.watchdog.watchdogmobile.core.communication;

import java.util.List;

import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by tobou on 15.10.2016.
 */

public interface CommunicationManager {

    void setCommunicationListener(CommunicationListener communicationListener);
    void connect(String userName, String password);
    void disconnect();
    void sendData(List<Sample> data);

}
