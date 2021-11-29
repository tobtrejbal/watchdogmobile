package uhk.watchdog.watchdogmobile.core.sensors.impl;

import android.content.Context;

import uhk.watchdog.watchdogmobile.app.AppConfig;
import uhk.watchdog.watchdogmobile.app.AppState;
import uhk.watchdog.watchdogmobile.core.model.Sample;
import uhk.watchdog.watchdogmobile.core.model.SensorValue;
import uhk.watchdog.watchdogmobile.core.sensors.SensorManager;
import uhk.watchdog.watchdogmobile.core.sensors.WatchSensorListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobous on 18. 10. 2014.
 *
 *
 */
public class WatchSensorManager implements SensorManager, WatchSensorListener {

    /**
     *
     */
    private DeviceSensor mDeviceSensor;

    /**
     *
     */
    private AppState mAppState;

    /**
     *
     */
    private AppConfig mAppConfig;

    /**
     *
     */

    private ExternalSensor mExternalSensor;

    /**
     *
     */
    private LocationSensor mSensorLocation;

    /**
     *
     */
    private BatterySensor mBatterySensor;

    /**
     *
     */
    private Map<Integer, SensorValue> mSensorValues;

    /**
     *
     * @param context
     */
    public WatchSensorManager(Context context) {
        mAppState = AppState.getInstance();
        mAppConfig = AppConfig.getInstance();
        mDeviceSensor = new DeviceSensor(context, this);
        mExternalSensor = new ExternalSensor(this);
        mSensorLocation = new LocationSensor(context);
        mBatterySensor = new BatterySensor(context);
        mSensorValues = new HashMap<>();
    }

    @Override
    public void onValueChanged(SensorValue value) {
        mSensorValues.put(value.getType(), value);
    }

    /**
     *
     * @param sensorList
     */
    public void startSensors(int[] sensorList) {
        mSensorLocation.startListening();
        mExternalSensor.startListening();
        mBatterySensor.startListening();
        mDeviceSensor.startListening(sensorList);
    }

    /**
     *
     */
    public void stopSensors() {
        mSensorLocation.stopListening();
        mExternalSensor.stopListening();
        mBatterySensor.stopListening();
        mDeviceSensor.stopListening();
    }

    /**
     *
     * @return
     */
    public Sample getSample() {
        Map<Integer, SensorValue> valueMap = new HashMap<>();
        valueMap.putAll(mSensorValues);
        Sample sample = new Sample(valueMap, new Date().getTime(), mAppConfig.getUserLogin(), mAppState.getBatteryLevel(),
                mAppState.getLat(), mAppState.getLng());
        mAppState.setSampleSendCounter(mAppState.getSampleSendCounter()+1);
        return sample;
    }
}
