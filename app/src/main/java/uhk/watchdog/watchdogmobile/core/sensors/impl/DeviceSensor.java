package uhk.watchdog.watchdogmobile.core.sensors.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import uhk.watchdog.watchdogmobile.core.model.SensorValue;
import uhk.watchdog.watchdogmobile.core.sensors.WatchSensorListener;

/**
 * Created by Tobous on 3. 11. 2014.
 *
 */
public class DeviceSensor implements SensorEventListener {

    /**
     *
     */
    private SensorManager mSensorManager;

    /**
     *
     */
    private WatchSensorListener mSensorListener;

    /**
     *
      * @param context
     * @param sensorListener
     */
    public DeviceSensor(Context context, WatchSensorListener sensorListener) {
        this.mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.mSensorListener = sensorListener;
    }

    long currentTime;
    long previousTime;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mSensorListener.onValueChanged(new SensorValue(sensorEvent.sensor.getType(), sensorEvent.values));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     *
     * @param sensors
     */
    public void startListening(int[] sensors) {
        for(int i : sensors) {
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(i), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    /**
     *
     */
    public void stopListening() {
        mSensorManager.unregisterListener(this);
    }
}
