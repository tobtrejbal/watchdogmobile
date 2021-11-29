package uhk.watchdog.watchdogmobile.core.monitor.impl;

import android.content.Context;

import uhk.watchdog.watchdogmobile.core.model.Sample;
import uhk.watchdog.watchdogmobile.core.monitor.Monitor;
import uhk.watchdog.watchdogmobile.core.monitor.SensorIncidentListener;

import java.util.Deque;
import java.util.List;

/**
 * Created by Tobous on 15. 10. 2014.
 */
public class MonitorImpl implements Monitor {

    /**
     *
     */
    private SensorIncidentListener mSensorIncidentListener;

    /**
     *
     */
    private Context mContext;

    /**
     *
     */
    private int polySides;

    /**
     *
     */
    private double[] constansts;

    /**
     *
     */
    private double[] multiply;

    /**
     *
     */
    private double[] polyY;

    /**
     *
     */
    private double[] polyX;

    /**
     *
     * @param context
     * @param listener
     */
    public MonitorImpl(Context context, SensorIncidentListener listener) {
        this.mSensorIncidentListener = listener;
        this.mContext = context;
    }

    /**
     *
     * @param samples
     * @throws InterruptedException
     */
    public void analyze(Deque<Sample> samples) throws InterruptedException {

    }

    /**
     *
     * @param x
     * @param y
     */
    public void start(double[] x, double[] y) {
       precalculateLocationPolygon(x, y);
    }

    /**
     *
     */
    public void stop() {
    }

    /**
     *
     * @param id
     */
    private void problem(int id) {
        mSensorIncidentListener.sensorIncident(id);
    }

    /**
     *
     * @param lon
     * @param lat
     */
    private void analyzeLocation(double lon, double lat) {
        if(lon != 0.0d && lat != 0.0d) {
            int j = polySides - 1;
            double x = lon;
            double y = lat;
            boolean oddNodes = false;
            for (int i = 0; i < polySides; i++) {
                if ((polyY[i] < y && polyY[j] >= y
                        || polyY[j] < y && polyY[i] >= y)) {
                    oddNodes ^= (y * multiply[i] + multiply[i] < x);
                }
                j = i;
            }
            if (oddNodes == false) {
                problem(1);
            }
        }
    }

    /**
     * Code from http://alienryderflex.com/polygon/
     * @param polyY
     * @param polyX
     */
    private void precalculateLocationPolygon(double[] polyY, double[] polyX) {
        this.polySides = polyX.length;
        this.polyY = polyY;
        this.polyX = polyX;
        this.constansts = new double[polySides];
        this.multiply = new double[polySides];
        int   i, j=polySides-1 ;
        for(i=0; i<polySides; i++) {
            if(polyY[j]==polyY[i]) {
                constansts[i]=polyX[i];
                multiply[i]=0;
            } else {
                constansts[i]=polyX[i]-(polyY[i]*polyX[j])/(polyY[j]-polyY[i])+(polyY[i]*polyX[i])/(polyY[j]-polyY[i]);
                multiply[i]=(polyX[j]-polyX[i])/(polyY[j]-polyY[i]);
            }
            j=i;
            }
    }
}
