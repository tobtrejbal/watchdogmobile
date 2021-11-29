package uhk.watchdog.watchdogmobile.gui.activities.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import uhk.watchdog.watchdogmobile.R;

/**
 * Created by Tob on 12. 1. 2016.
 */
public class AboutActivity extends AppCompatActivity {

    /**
     *
     */
    private TextView mTxtVersion;

    /**
     *
     */
    private ActionBar mActionbar;

    /**
     *
     */
    public AboutActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        this.mTxtVersion = (TextView) findViewById(R.id.about_txt_version);
        this.mActionbar = getSupportActionBar();

        mActionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        mActionbar.setDisplayHomeAsUpEnabled(true);
        mActionbar.setTitle(R.string.menu_title_about);
        mActionbar.setDisplayHomeAsUpEnabled(true);

        String version = "version ";

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version += pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mTxtVersion.setText(version);
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
}