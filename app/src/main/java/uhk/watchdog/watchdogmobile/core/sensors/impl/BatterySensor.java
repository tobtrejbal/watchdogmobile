package uhk.watchdog.watchdogmobile.core.sensors.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import uhk.watchdog.watchdogmobile.app.AppState;

/**
 * Created by Tobous on 19. 10. 2014.
 *
 */
public class BatterySensor extends BroadcastReceiver {

    /**
     *
     */
    private AppState mAppState;

    /**
     *
     */
    private Context mContext;

    /**
     *
     * @param context
     */
    public BatterySensor(Context context) {
        this.mAppState = AppState.getInstance();
        this.mContext = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mAppState.setBatteryLevel(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
    }

    /**
     *
     */
    public void startListening() {
        mContext.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /**
     *
     */
    public void stopListening() {
        mContext.unregisterReceiver(this);
    }

}
