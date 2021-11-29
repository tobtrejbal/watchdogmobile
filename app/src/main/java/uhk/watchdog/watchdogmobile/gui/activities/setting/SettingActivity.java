package uhk.watchdog.watchdogmobile.gui.activities.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import uhk.watchdog.watchdogmobile.R;
import uhk.watchdog.watchdogmobile.app.AppConfig;
import uhk.watchdog.watchdogmobile.app.AppCore;
import uhk.watchdog.watchdogmobile.app.utils.ConfigUtils;
import uhk.watchdog.watchdogmobile.core.communication.constants.NetworkCommunication;

/**
 * Created by Tob on 12. 1. 2016.
 */
public class SettingActivity extends AppCompatActivity  {

    /**
     *
     */
    private EditText mTxtSampleFrequency;

    /**
     *
     */
    private EditText mTxtSendFrequency;

    /**
     *
     */
    private EditText mTxtServerAddress;

    /**
     *
     */
    private EditText mTxtServerPort;

    /**
     *
     */
    private EditText mTxtUserLogin;

    /**
     *
     */
    private EditText mTxtUserPassword;

    /**
     *
     */
    private CheckBox mCheckBoxHorizontalMenu;

    /**
     *
     */
    private CheckBox mCheckBoxAppMode;

    /**
     *
     */
    private final AppCore mAppCore;

    /**
     *
     */
    private AppConfig mAppConfig;

    /**
     *
     */
    private ConfigUtils mConfigUtils;

    /**
     *
     */
    private ActionBar mActionBar;

    /**
     *
     */
    public SettingActivity() {
        mAppCore = AppCore.getInstance();
        mAppConfig = AppConfig.getInstance();
        mConfigUtils = mAppCore.getmConfigUtils();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        this.mActionBar = getSupportActionBar();
        this.mTxtSampleFrequency = (EditText) findViewById(R.id.setting_item_txt_sample_frequency);
        this.mTxtSendFrequency = (EditText) findViewById(R.id.setting_item_txt_send_frequency);
        this.mTxtServerAddress = (EditText) findViewById(R.id.setting_item_txt_server_address);
        this.mTxtServerPort = (EditText) findViewById(R.id.setting_item_txt_server_port);
        this.mTxtUserLogin = (EditText) findViewById(R.id.setting_item_txt_user_login);
        this.mTxtUserPassword = (EditText) findViewById(R.id.setting_item_txt_user_password);
        this.mCheckBoxHorizontalMenu = (CheckBox) findViewById(R.id.setting_item_checkbox_horizontal_menu);
        this.mCheckBoxAppMode = (CheckBox) findViewById(R.id.setting_item_checkbox_app_mode);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(R.string.menu_title_setting);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        showConfig();
    }

    @Override
    public void onPause() {
        super.onPause();

        saveSetting();
        updateCore();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     *
     */
    public void showConfig() {
        mTxtSampleFrequency.setText(String.valueOf(mAppConfig.getSampleRate()));
        mTxtSendFrequency.setText(String.valueOf(mAppConfig.getClearBufferRate()));
        mTxtServerAddress.setText(mAppConfig.getServerAddress());
        mTxtServerPort.setText(String.valueOf(mAppConfig.getServerPort()));
        mTxtUserLogin.setText(mAppConfig.getUserLogin());
        mTxtUserPassword.setText(mAppConfig.getUserPassword());
        mCheckBoxHorizontalMenu.setChecked(mAppConfig.isHorizontalMenu());
        mCheckBoxAppMode.setChecked(mAppCore.getMode() == AppCore.MODE_ONLINE);
    }

    /**
     *
     */
    public void saveSetting() {
        mAppConfig.setSampleRate(Integer.parseInt(mTxtSampleFrequency.getText().toString()));
        mAppConfig.setClearBufferRate(Integer.parseInt(mTxtSendFrequency.getText().toString()));
        mAppConfig.setServerAddress(mTxtServerAddress.getText().toString());
        mAppConfig.setServerPort(Integer.parseInt(mTxtServerPort.getText().toString()));
        mAppConfig.setUserLogin(mTxtUserLogin.getText().toString());
        mAppConfig.setUserPassword(mTxtUserPassword.getText().toString());
        mAppConfig.setHorizontalMenu(mCheckBoxHorizontalMenu.isChecked());
        if(mCheckBoxAppMode.isChecked()) {
            mAppCore.setMode(AppCore.MODE_ONLINE);
        } else {
            mAppCore.setMode(AppCore.MODE_OFFLINE);
        }
        mConfigUtils.saveConfig();
    }

    /**
     *
     */
    public void updateCore() {
        Intent intentMessage = new Intent(NetworkCommunication.INTENT_FILTER_WATCH_DOG_MAIN);
        intentMessage.putExtra(NetworkCommunication.BROADCASTS_TYPE+"", NetworkCommunication.BROADCASTS_RESTART);
        sendBroadcast(intentMessage);
    }


}
