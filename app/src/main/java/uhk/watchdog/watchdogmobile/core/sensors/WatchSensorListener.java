package uhk.watchdog.watchdogmobile.core.sensors;

import uhk.watchdog.watchdogmobile.core.model.SensorValue;

/**
 * Created by Tobous on 12. 11. 2014.
 *
 */
public interface WatchSensorListener {

    void onValueChanged(SensorValue value);

}
