package uhk.watchdog.watchdogmobile.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import uhk.watchdog.watchdogmobile.R;
import uhk.watchdog.watchdogmobile.app.AppConfig;
import uhk.watchdog.watchdogmobile.app.AppCore;
import uhk.watchdog.watchdogmobile.app.AppState;
import uhk.watchdog.watchdogmobile.core.WatchDogCore;
import uhk.watchdog.watchdogmobile.core.communication.constants.NetworkCommunication;
import uhk.watchdog.watchdogmobile.core.monitor.SensorIncidentListener;
import uhk.watchdog.watchdogmobile.gui.mainScreen.MainActivity;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Tobous on 15. 10. 2014.
 * Core class
 *
 */
public class WatchDogService extends Service implements SensorIncidentListener {

    public static final int NOTIFICATION_ID = 17221477;

    /**
     * App core
     */
    private AppCore mAppCore;

    /**
     * App state
     */
    private AppState mAppState;

    /**
     * App Configuration
      */
    private AppConfig mAppConfig;

    /**
     *
     */
    private WatchDogCore mWatchDogCore;

    /**
     * wake lock
     */
    private PowerManager.WakeLock mWakeLock;

    /**
     * Notification manager
     */
    private NotificationManager mNotificationManager;

    /**
     *
     */
    private Notification mNotification;

    /**
     *
     */
    private boolean running;

    /**
     * Core receiver
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            Log.i(this.getClass().getName(),"received broadcast with type : " + extras.getInt(NetworkCommunication.BROADCASTS_TYPE+""));

            switch(extras.getInt(NetworkCommunication.BROADCASTS_TYPE+"")) {
                case NetworkCommunication.BROADCASTS_RESTART:
                    mWatchDogCore.restart();
                    break;
                case NetworkCommunication.BROADCASTS_KILL:
                    Log.v("STOPIIIING", "LALALA");
                    onDestroy();
                    break;
                case NetworkCommunication.BROADCASTS_AUTHENTICATE:
                    sendMessage(NetworkCommunication.BROADCASTS_AUTHENTISATION_SUCCESS);
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        WatchDogCore.createInstance(this);

        this.mWatchDogCore = WatchDogCore.getInstance();
        this.mAppCore = AppCore.getInstance();
        this.mAppState = AppState.getInstance();
        this.mAppConfig = AppConfig.getInstance();
        Log.i(this.getClass().getName(),"classes initialized");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        start();
        Log.i(this.getClass().getName(),"successfully started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(this.getClass().getName(),"killing app");
        stop();
        Log.i(this.getClass().getName(),"stopped");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void sensorIncident(int id) {
    }

    /**
     *
     */
    private void start() {
        if(!running) {
            registerReceiver();
            acquireWakelock();
            createNotification();
            //mWatchDogCore.start();
            running = true;
            // for priority - will show notification
            startForeground(NOTIFICATION_ID, mNotification);
        }
    }

    /**
     *
     */
    private void stop() {
        if(running) {
            mWatchDogCore.stop();
            unregisterReceiver();
            releaseWakeLock();
            Log.i(this.getClass().getName(),"stopped");
            stopForeground(true);
            running = false;
        }
    }

    /**
     * acquires wakelock
     * core can run in background now
     */
    private void acquireWakelock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WatchDog");
        mWakeLock.acquire();
        Log.i(this.getClass().getName(),"created wakelock");
    }

    /**
     * release wakelock
     * core cannot run in background now
     */
    private void releaseWakeLock() {
        Log.i(this.getClass().getName(),"destroyed wakelock");
        mWakeLock.release();
    }

    private InputStream getCertificate() {
        Log.i(this.getClass().getName(),"getting certificate");
        try {
            return getAssets().open("keystore.bks");
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * registers broadcast receiver
     */
    private void registerReceiver() {
        this.registerReceiver(receiver, new IntentFilter(NetworkCommunication.INTENT_FILTER_WATCH_DOG_MAIN));
        Log.i(this.getClass().getName(),"registered receiver");


        //startMeasure();
        //this.registerReceiver(BroadcastReceiverSignal, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    /**
     * unregisters broadcast receiver
     */
    private void unregisterReceiver() {
        this.unregisterReceiver(receiver);
        Log.i(this.getClass().getName(),"unregistered receiver");
    }

    /**
     * sends broadcast
     * @param message type of message
     */
    private void sendMessage(int message) {
        Intent intentMessage = new Intent(NetworkCommunication.INTENT_FILTER_WATCH_DOG_MAIN);
        intentMessage.putExtra(NetworkCommunication.BROADCASTS_TYPE + "", message);
        sendBroadcast(intentMessage);
        Log.i(this.getClass().getName(),"broadcast message with type : " + message + " sent");
    }

    /**
     * creates notification that sampling is running
     */
    private void createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_app)
                .setContentTitle(getString(R.string.app_name))
                .setOngoing(true)
                .setContentText("vzorkování probíhá");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = mBuilder.build();
    }


    /*BluetoothAdapter mBluetooth;

    public void startMeasure() {
        mBluetooth = BluetoothAdapter.getDefaultAdapter();

        Thread thread1 = new Thread(runnableSignal);
        thread1.start(); //First thread will often be denied
    }

    public Runnable runnableSignal = new Runnable() {
        @Override
        public void run() {
            BluetoothAdapter mBluetooth = BluetoothAdapter.getDefaultAdapter();
            while(true) {
                if(!mBluetooth.isDiscovering()) {
                    mBluetooth.startDiscovery();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private final BroadcastReceiver BroadcastReceiverSignal = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String mIntentAction = intent.getAction();
            Log.v("WATCHDOG BT","SIGNAL: ");
            if(BluetoothDevice.ACTION_FOUND.equals(mIntentAction)) {
                int RSSI = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String mDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                Log.v("WATCHDOG BT","DEVICE: "+ mDeviceName+ " SIGNAL: "+RSSI);

            }
        }
    };*/

}
