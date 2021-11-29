package uhk.watchdog.watchdogmobile.app;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import uhk.watchdog.watchdogmobile.app.utils.ConfigUtils;


/**
 * Created by Tob on 27. 6. 2016.
 */
public class AppCore extends Application {

    /**
     *
     */
    private static AppCore sInstance;

    /**
     *
     * @return
     */
    public static AppCore getInstance() {
        return sInstance;
    }

    /**
     *
     */
    private ConfigUtils mConfigUtils;

    public static final int MODE_OFFLINE = 0;

    public static final int MODE_ONLINE = 1;

    private int mode;

    @Override
    public void onCreate() {
        super.onCreate();

        this.sInstance = this;
        this.mConfigUtils = new ConfigUtils(this);
        Log.v(AppCore.this.getClass().getName() + "bubaaaak", "");
        setCrashLog();
        mConfigUtils.loadConfig();
    }

    /**
     *
     */
    private void setCrashLog() {
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException (Thread thread, Throwable e) {
                handleUncaughtException (thread, e);
            }
        });
    }

    /**
     *
     * @param thread
     * @param e
     */
    public void handleUncaughtException (Thread thread, Throwable e) {
        /*Toast toast = Toast.makeText(this, "spadlo to",Toast.LENGTH_LONG);
        toast.show();*/
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        /*Intent intent = new Intent ();
        intent.setAction ("com.mydomain.SEND_LOG"); // see step 5.
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity (intent);*/

        System.exit(1); // kill off the crashed app
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     *
     * @return
     */
    public boolean isOnline() {
        if(mode == MODE_OFFLINE) {
            return false;
        }
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     *
     * @return
     */
    public ConfigUtils getmConfigUtils() {
        return mConfigUtils;
    }

    /**
     *
     * @return
     */
    public boolean firstTimeCheck() {
        final String PREFS_NAME = "myPrefs";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    public void setFirstTime() {
        final String PREFS_NAME = "myPrefs";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putBoolean("my_first_time", false).commit();
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
