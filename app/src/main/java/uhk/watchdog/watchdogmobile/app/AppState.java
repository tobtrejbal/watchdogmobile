package uhk.watchdog.watchdogmobile.app;

import java.util.Date;

/**
 * Created by Tob on 1. 7. 2016.
 */
public class AppState {

    /**
     *
     */
    private static AppState instance;

    /**
     *
     */
    private AppState() {

        lastDataSend = new Date();
    }

    /**
     *
     * @return
     */
    public static AppState getInstance() {

        if (instance == null) {
            instance = new AppState();
        }

        return instance;

    };

    /**
     *
     */
    private double lng;

    /**
     *
     */
    private double lat;

    /**
     *
     */
    private int sampleSendCounter;

    /**
     *
     */
    private Date lastDataSend;

    /**
     *
     */
    private int batteryLevel;

    /**
     *
     */
    private boolean lastSendSuccess;

    /**
     *
     */
    private int databaseCount;

    /**
     *
     */
    private String token;

    /**
     *
     * @return
     */
    public Date getLastDataSend() {
        return lastDataSend;
    }

    /**
     *
     * @param lastDataSend
     */
    public void setLastDataSend(Date lastDataSend) {
        this.lastDataSend = lastDataSend;
    }

    /**
     *
     * @return
     */
    public double getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     */
    public double getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     */
    public int getBatteryLevel() {
        return batteryLevel;
    }

    /**
     *
     * @param batteryLevel
     */
    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    /**
     *
     * @return
     */
    public boolean isLastSendSuccess() {
        return lastSendSuccess;
    }

    /**
     *
     * @param lastSendSuccess
     */
    public void setLastSendSuccess(boolean lastSendSuccess) {
        this.lastSendSuccess = lastSendSuccess;
    }

    /**
     *
     * @return
     */
    public int getSampleSendCounter() {
        return sampleSendCounter;
    }

    /**
     *
     * @param sampleSendCounter
     */
    public void setSampleSendCounter(int sampleSendCounter) {
        this.sampleSendCounter = sampleSendCounter;
    }

    /**
     *
     * @return
     */
    public int getDatabaseCount() {
        return databaseCount;
    }

    /**
     *
     * @param databaseCount
     */
    public void setDatabaseCount(int databaseCount) {
        this.databaseCount = databaseCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}