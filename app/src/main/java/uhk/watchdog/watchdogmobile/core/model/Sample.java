package uhk.watchdog.watchdogmobile.core.model;

import  java.io.Serializable;
import java.util.Map;

/**
 * Created by Tobous on 1. 11. 2014.
 * dd
 */
public class Sample implements Serializable {

    /**
     *
     * @param values
     * @param date
     * @param userID
     * @param batteryLevel
     * @param lat
     * @param lon
     */
    public Sample(Map<Integer, SensorValue> values, long date, String userID, int batteryLevel, double lat, double lon) {
        this.values = values;
        this.date = date;
        this.userID = userID;
        this.batteryLevel = batteryLevel;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     *
     */
    private final Map<Integer,SensorValue> values;

    /**
     *
     */
    private final long date;

    /**
     *
     */
    private final String userID;

    /**
     *
     */
    private final int batteryLevel;

    /**
     *
     */
    private final double lat;

    /**
     *
     */
    private final double lon;

    /**
     *
     * @return
     */
    public double getLat() {
        return lat;
    }

    /**
     *
     * @return
     */
    public double getLon() {
        return lon;
    }

    /**
     *
     * @return
     */
    public long getDate() {
        return date;
    }

    /**
     *
     * @return
     */
    public Map<Integer, SensorValue> getValues() {
        return values;
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
     * @return
     */
    public String getUserID() {
        return userID;
    }

}
