package uhk.watchdog.watchdogmobile.core;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import uhk.watchdog.watchdogmobile.app.AppConfig;
import uhk.watchdog.watchdogmobile.app.AppCore;
import uhk.watchdog.watchdogmobile.app.AppState;
import uhk.watchdog.watchdogmobile.core.buffer.Buffer;
import uhk.watchdog.watchdogmobile.core.buffer.impl.BufferImpl;
import uhk.watchdog.watchdogmobile.core.communication.CommunicationListener;
import uhk.watchdog.watchdogmobile.core.communication.CommunicationManager;
import uhk.watchdog.watchdogmobile.core.communication.impl.Mqtt;
import uhk.watchdog.watchdogmobile.core.controller.Controller;
import uhk.watchdog.watchdogmobile.core.controller.impl.ControllerImpl;
import uhk.watchdog.watchdogmobile.core.data.DatabaseManager;
import uhk.watchdog.watchdogmobile.core.data.impl.DatabaseManagerImpl;
import uhk.watchdog.watchdogmobile.core.model.Sample;
import uhk.watchdog.watchdogmobile.core.monitor.Monitor;
import uhk.watchdog.watchdogmobile.core.monitor.SensorIncidentListener;
import uhk.watchdog.watchdogmobile.core.monitor.impl.MonitorImpl;
import uhk.watchdog.watchdogmobile.core.sensors.SensorManager;
import uhk.watchdog.watchdogmobile.core.sensors.impl.WatchSensorManager;

/**
 * Created by Tobous on 06.02.2017.
 */

public class WatchDogCore implements SensorIncidentListener {

    private static WatchDogCore sInstance;

    public static WatchDogCore getInstance() {
        return sInstance;
    }

    public static void createInstance(Context context) {
        if(sInstance == null) {
            sInstance = new WatchDogCore(context);
        }
    }

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
     * Timer - responsible for periodic events - sampling, sending SampleDAO, clearing database etc.
     */
    private Controller mController;

    /**
     * BufferSend - contains samples
     */
    private Buffer mBuffer;

    /**
     * MonitorImpl - analyzing SampleDAO
     */
    private Monitor mMonitor;

    /**
     * DatabaseManagerImpl - manager of database operations
     */
    private DatabaseManager mDatabaseManager;

    /**
     * Communication
     */
    private CommunicationManager mCommunicationManager;

    /**
     * Sensors
     */
    private SensorManager mSensorManager;

    /**
     *
     */
    private boolean running;

    /**
     *
     */
    boolean online = false;

    /**
     *
     */
    String[] topics = {"watchdog_data"};

    /**
     *
     */
    int[] quos = {0};

    /**
     *
     */
    String clientName = "watchdogmobile";

    /**
     * run take sample
     */
    private Runnable takeSampleRunnable = new Runnable() {
        @Override
        public void run() {
            takeSample();
        }
    };

    /**
     * Analyze SampleDAO
     */
    private Runnable analyzeDataRunnable = new Runnable() {
        @Override
        public void run() {
            analyzeData();
        }
    };

    /**
     * run clear buffer method
     */
    private Runnable clearBufferRunnable = new Runnable() {
        @Override
        public void run() {
            clearBuffer();
        }
    };

    /**
     * run clear database method
     */
    private Runnable clearDatabase = new Runnable() {
        @Override
        public void run() {
            clearDatabase();
        }

    };

    /**
     *
     */
    public CommunicationListener communicationListener = new CommunicationListener() {

        @Override
        public void connectionSuccess() {
            online = true;
        }

        @Override
        public void connectionFail() {
            online = false;
            Log.v("connection fail","asdasdasdasd");
            connect();
        }

        @Override
        public void connectionLost() {
            online = false;
            connect();
        }

        @Override
        public void messageArrived(String s, byte[] content) {

        }

        @Override
        public void messageSuccess() {
            System.out.println("succeeeesssssss");
            mAppState.setLastSendSuccess(true);
            mAppState.setLastDataSend(new Date());
        }

        @Override
        public void messageFail(String type, byte[] message) {

        }

        @Override
        public void dataSendFail(List<Sample> samples) {
            try {
                mAppState.setLastSendSuccess(false);
                mDatabaseManager.insertData(samples);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private WatchDogCore(Context context) {
        Random random = new Random(System.currentTimeMillis());
        this.mAppCore = AppCore.getInstance();
        this.mAppState = AppState.getInstance();
        this.mAppConfig = AppConfig.getInstance();
        this.mBuffer = new BufferImpl(200,1000);
        this.mController = new ControllerImpl();
        this.mSensorManager = new WatchSensorManager(context);
        this.mMonitor = new MonitorImpl(context, this);
        this.mDatabaseManager = new DatabaseManagerImpl(context);
        clientName = clientName+":"+mAppConfig.getUserLogin()+":"+random.nextInt();
        this.mCommunicationManager = new Mqtt("tcp://"+mAppConfig.getServerAddress()+":"+mAppConfig.getServerPort(), clientName, topics, quos);
        mCommunicationManager.setCommunicationListener(communicationListener);
        Log.i(this.getClass().getName(),"classes initialized");
    }

    @Override
    public void sensorIncident(int id) {
    }

    /**
     *
     */
    public void start() {
        if(!running) {
            openDatabase();
            startSensors();
            createSchedulers();
            startMonitor();
            connect();
            running = true;
        }
    }


    /**
     *
     */
    public void stop() {
        if(running) {
            stopSchedulers();
            stopSensors();
            stopMonitor();
            closeDatabase();
            disconnect();
            running = false;
            Log.i(this.getClass().getName(),"stopped");
        }
    }

    /**
     * restart core
     */

    public void restart() {
        stopSchedulers();
        stopSensors();
        stopMonitor();
        closeDatabase();
        openDatabase();
        startMonitor();
        startSensors();
        createSchedulers();
    }

    /**
     * creates schedulers
     */
    private void createSchedulers() {
        mController.start(4);
        mController.scheduleWithFixedDelay(takeSampleRunnable, 3000, mAppConfig.getSampleRate(), TimeUnit.MILLISECONDS);
        mController.scheduleWithFixedDelay(analyzeDataRunnable, 2013, mAppConfig.getSampleRate()*100, TimeUnit.MILLISECONDS);
        mController.scheduleWithFixedDelay(clearBufferRunnable, mAppConfig.getClearBufferRate(), mAppConfig.getClearBufferRate(), TimeUnit.MILLISECONDS);
        //mController.scheduleWithFixedDelay(clearDatabase, 3000, 3000, TimeUnit.MILLISECONDS);
        Log.i(this.getClass().getName(),"created schedulers");
    }

    /**
     * stops schedulers
     */
    private void stopSchedulers() {
        mController.stop();
    }


    /**
     * Starts sensors
     */
    private void startSensors() {
        Log.i(this.getClass().getName(),mAppConfig.getSensorList().length+"");
        mSensorManager.startSensors(mAppConfig.getSensorList());
    }

    /**
     * Stops sensors
     */
    private void stopSensors() {
        mSensorManager.stopSensors();
    }

    /**
     * Starts monitor
     */
    private void startMonitor() {
    }

    /**
     * Stops monitor
     */
    private void stopMonitor() {
    }

    /**
     * Open database
     */
    private void openDatabase() {
        mDatabaseManager.open();
    }

    /**
     * Closes database
     */
    private void closeDatabase() {
        mDatabaseManager.close();
    }

    /**
     *
     */
    public void takeSample() {
        try {
            Sample sample = mSensorManager.getSample();

            mBuffer.addSampleToAnalyze(sample);
            if(mAppCore.getMode() == AppCore.MODE_ONLINE) {
                mBuffer.addSampleSend(sample);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void analyzeData() {
        try {
            mMonitor.analyze(mBuffer.getSamplesToAnalyze());
            mBuffer.analyzeCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearBuffer() {
        try {
            if(mAppCore.getMode() == AppCore.MODE_ONLINE) {
                if (mBuffer.sizeSend() > 0) {
                    //Log.i(this.getClass().getName(),"clearing buffer");
                    List<Sample> data = mBuffer.clearBufferSend();
                    if (online) {
                        mCommunicationManager.sendData(data);
                    } else {
                        AppState.getInstance().setLastSendSuccess(false);
                        //mDatabaseManager.insertData(data);
                        //mAppState.setDatabaseCount(mDatabaseManager.getDataNumber());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearDatabase() {
        try {
            if(mAppCore.getMode() == AppCore.MODE_ONLINE) {
                mAppState.setDatabaseCount(mDatabaseManager.getDataNumber());
                Log.v(mAppState.getDatabaseCount()+"","hihi");
                if(mAppState.getDatabaseCount() > 0) {
                    if (online) {
                        mCommunicationManager.sendData(mDatabaseManager.getData(200));
                        mAppState.setDatabaseCount(mDatabaseManager.getDataNumber());
                    }
                }
            } else {
                mDatabaseManager.clearDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        mCommunicationManager.connect(mAppConfig.getUserLogin(), mAppConfig.getUserPassword());
    }

    public void disconnect() {
        mCommunicationManager.disconnect();
    }

}
