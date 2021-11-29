package uhk.watchdog.watchdogmobile.core.sensors;

import uhk.watchdog.watchdogmobile.core.model.Sample;

/**
 * Created by tobou on 30.10.2016.
 */

public interface SensorManager {

    void startSensors(int[] sensorList);
    void stopSensors();
    Sample getSample();

}
