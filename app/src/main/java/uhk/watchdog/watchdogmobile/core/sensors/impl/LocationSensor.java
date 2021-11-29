package uhk.watchdog.watchdogmobile.core.sensors.impl;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import uhk.watchdog.watchdogmobile.app.AppState;

/**
 * Created by Tobous on 19. 10. 2014.
 *
 */
public class LocationSensor implements LocationListener {

    /**
     *
     */
    private LocationManager mLocationManager;

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
    public LocationSensor(Context context) {
        this.mContext = context;
        this.mAppState = AppState.getInstance();
        this.mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     *
     */
    public void stopListening() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.removeUpdates(this);
    }

    /**
     *
     */
    public void startListening() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mAppState.setLat(location.getLatitude());
        mAppState.setLng(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
