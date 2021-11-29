package uhk.watchdog.watchdogmobile.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import uhk.watchdog.watchdogmobile.app.AppConfig;
import uhk.watchdog.watchdogmobile.app.AppCore;

/**
 * Created by Tob on 3. 7. 2016.
 */
public class ConfigUtils {

    private final static String CONF_APP_MODE = "CONF_APP_MODE";
    private final static String CONF_SAMPLE_RATE = "CONF_SAMPLE_RATE";
    private final static String CONF_CLEAR_BUFFER_RATE = "CONF_CLEAR_BUFFER_RATE";
    private final static String CONF_SERVER_ADDRESS = "CONF_SERVER_ADDRESS";
    private final static String CONF_SERVER_PORT = "CONF_SERVER_PORT";
    private final static String CONF_USER_LOGIN = "CONF_USER_LOGIN";
    private final static String CONF_USER_PASSWORD = "CONF_USER_PASSWORD";
    private final static String CONF_HORIZONTAL_MENU = "CONF_HORIZONTAL_MENU";

    /**
     *
     */
    private AppConfig mAppConfig;

    /**
     *
     */
    private AppCore mAppCore;


    /**
     *
     */
    private SharedPreferences mPreferences;

    /**
     *
     */
    private Context mContext;

    /**
     *
     * @param context
     */
    public ConfigUtils(Context context) {
        this.mContext = context;
        this.mAppConfig = AppConfig.getInstance();
        this.mAppCore = AppCore.getInstance();
        this.mPreferences = context.getSharedPreferences(context.getApplicationInfo().name, Context.MODE_PRIVATE);
    }

    /**
     *
     */
    public void loadConfig() {
        mAppConfig.setSampleRate(mPreferences.getInt(CONF_SAMPLE_RATE, 4));
        mAppConfig.setClearBufferRate(mPreferences.getInt(CONF_CLEAR_BUFFER_RATE, 2000));
        mAppConfig.setServerAddress(mPreferences.getString(CONF_SERVER_ADDRESS, "imitgw.uhk.cz"));
        mAppConfig.setServerPort(mPreferences.getInt(CONF_SERVER_PORT, 59703));
        mAppConfig.setUserLogin(mPreferences.getString(CONF_USER_LOGIN, "pokus"));
        mAppConfig.setUserPassword(mPreferences.getString(CONF_USER_PASSWORD, "pokus"));
        mAppConfig.setHorizontalMenu(mPreferences.getBoolean(CONF_HORIZONTAL_MENU, false));
        mAppCore.setMode(mPreferences.getInt(CONF_APP_MODE, 0));
    }

    /**
     *
     */
    public void saveConfig() {
        mPreferences.edit().putInt(CONF_SAMPLE_RATE, mAppConfig.getSampleRate()).commit();
        mPreferences.edit().putInt(CONF_CLEAR_BUFFER_RATE, mAppConfig.getClearBufferRate()).commit();
        mPreferences.edit().putString(CONF_SERVER_ADDRESS, mAppConfig.getServerAddress()).commit();
        mPreferences.edit().putInt(CONF_SERVER_PORT,mAppConfig.getServerPort()).commit();
        mPreferences.edit().putString(CONF_USER_LOGIN, mAppConfig.getUserLogin()).commit();
        mPreferences.edit().putString(CONF_USER_PASSWORD, mAppConfig.getUserPassword()).commit();
        mPreferences.edit().putBoolean(CONF_HORIZONTAL_MENU, mAppConfig.isHorizontalMenu()).commit();
        mPreferences.edit().putInt(CONF_APP_MODE, mAppCore.getMode()).commit();
    }


    /**
     *
     */
    public void saveFirstConfig(String serverAddress, String serverPort, String username, String userpassword, int appMode) {
        mPreferences.edit().putString(CONF_SERVER_ADDRESS, serverAddress).commit();
        mPreferences.edit().putInt(CONF_SERVER_PORT,Integer.parseInt(serverPort)).commit();
        mPreferences.edit().putString(CONF_USER_LOGIN, username).commit();
        mPreferences.edit().putString(CONF_USER_PASSWORD, userpassword).commit();
        mPreferences.edit().putInt(CONF_APP_MODE, appMode).commit();
    }
}
