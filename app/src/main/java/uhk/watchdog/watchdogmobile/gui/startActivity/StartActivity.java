package uhk.watchdog.watchdogmobile.gui.startActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import uhk.watchdog.watchdogmobile.R;
import uhk.watchdog.watchdogmobile.app.AppCore;
import uhk.watchdog.watchdogmobile.app.utils.ConfigUtils;
import uhk.watchdog.watchdogmobile.core.communication.CommunicationListener;
import uhk.watchdog.watchdogmobile.core.communication.CommunicationManager;
import uhk.watchdog.watchdogmobile.core.communication.impl.Mqtt;
import uhk.watchdog.watchdogmobile.core.model.Sample;
import uhk.watchdog.watchdogmobile.gui.mainScreen.MainActivity;

/**
 * Created by Tob on 12. 1. 2016.
 */
public class StartActivity extends AppCompatActivity {

    private AppCore mAppCore;

    /**
     *
     */
    private Button mBtnStart;

    /**
     *
     */
    private ActionBar mActionbar;

    /**
     *
     */

    private ConfigUtils mConfigUtils;

    private AlertDialog configDialog;

    private CommunicationManager mCommManager;

    private CommunicationListener communicationListener = new CommunicationListener() {
        @Override
        public void connectionSuccess() {
            Log.v("success","blabla");
            mCommManager.disconnect();
            configDialog.cancel();
        }

        @Override
        public void connectionFail() {
            createDialog();
            configDialog.setMessage("špatné heslo");
        }

        @Override
        public void connectionLost() {

        }

        @Override
        public void messageArrived(String s, byte[] content) {

        }

        @Override
        public void messageSuccess() {

        }

        @Override
        public void messageFail(String type, byte[] message) {

        }

        @Override
        public void dataSendFail(List<Sample> samples) {

        }
    };

    public StartActivity() {
        this.mAppCore = AppCore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("creating string",stringFromJNI()+" aaaha");

        Log.v("creating string",myString()+" aaaha");

        setContentView(R.layout.activity_start);

        mConfigUtils = new ConfigUtils(this);

        if(mAppCore.firstTimeCheck()) {
            this.mBtnStart = (Button) findViewById(R.id.btn_start);
            this.mActionbar = getSupportActionBar();

            mActionbar.hide();

            mBtnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    mAppCore.setFirstTime();
                    finish();
                }
            });
            mAppCore.verifyStoragePermissions(this);
            createDialog();
        } else {
            //if(mode)
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
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

    public void createDialog() {
        Log.v("showing activityyy","aaaa");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("První konfigurace");

        LinearLayout layout = new LinearLayout(this);

        final TextView txt_login_lbl = new TextView(this);
        final TextView txt_password_lbl = new TextView(this);
        final TextView txt_server_address_lbl = new TextView(this);
        final TextView txt_server_port_lbl = new TextView(this);

        txt_login_lbl.setText("Login:");
        txt_password_lbl.setText("Heslo:");
        txt_server_address_lbl.setText("Server:");
        txt_server_port_lbl.setText("Port:");

        final EditText txt_login = new EditText(this);
        final EditText txt_password = new EditText(this);
        txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final EditText txt_server_address = new EditText(this);
        final EditText txt_server_port = new EditText(this);
        txt_server_port.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
        layout.addView(txt_login_lbl);
        layout.addView(txt_login);
        layout.addView(txt_password_lbl);
        layout.addView(txt_password);
        layout.addView(txt_server_address_lbl);
        layout.addView(txt_server_address);
        layout.addView(txt_server_port_lbl);
        layout.addView(txt_server_port);

        alertDialogBuilder.setView(layout);

        // set dialog message
        alertDialogBuilder
                .setMessage("Nakonfiguruj aplikaci")
                .setCancelable(false)
                .setPositiveButton("Ulož", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        /*setFirstConfig(txt_login.getText().toString(),
                                txt_password.getText().toString(), txt_server_address.getText().toString(),
                                txt_server_port.getText().toString());*/
                        mConfigUtils.saveFirstConfig(txt_server_address.getText().toString(), txt_server_port.getText().toString(), txt_login.getText().toString(), txt_password.getText().toString(), AppCore.MODE_ONLINE);
                        mConfigUtils.loadConfig();

                        authenticate(txt_login.getText().toString(), txt_password.getText().toString(), txt_server_address.getText().toString(), txt_server_port.getText().toString());

                    }
                })
                .setNegativeButton("Offline mode", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mConfigUtils.saveFirstConfig("","0" ,"" ,"" ,AppCore.MODE_OFFLINE);
                        mConfigUtils.loadConfig();
                        mAppCore.setMode(AppCore.MODE_OFFLINE);
                    }
                });



        // create alert dialog
        configDialog = alertDialogBuilder.create();

        // show it
        configDialog.show();

    }

    public void authenticate(String login, String password, String address, String port) {
        mCommManager = new Mqtt("tcp://"+address+":"+port, "watchdogmobile", new String[] {"lala"}, new int[] {0});
        mCommManager.setCommunicationListener(communicationListener);
        mCommManager.connect(login, password);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String myString();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

}