package uhk.watchdog.watchdogmobile.gui.mainScreen;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import uhk.watchdog.watchdogmobile.R;
import uhk.watchdog.watchdogmobile.app.AppConfig;
import uhk.watchdog.watchdogmobile.app.AppCore;
import uhk.watchdog.watchdogmobile.core.communication.constants.NetworkCommunication;
import uhk.watchdog.watchdogmobile.app.service.WatchDogService;
import uhk.watchdog.watchdogmobile.gui.activities.about.AboutActivity;
import uhk.watchdog.watchdogmobile.gui.activities.setting.SettingActivity;
import uhk.watchdog.watchdogmobile.gui.mainScreen.fragment.FragmentListener;
import uhk.watchdog.watchdogmobile.gui.mainScreen.navdrawer.DrawerItem;
import uhk.watchdog.watchdogmobile.gui.mainScreen.navdrawer.DrawerNavAdapter;
import uhk.watchdog.watchdogmobile.gui.mainScreen.slidingtab.SlidingTabLayout;
import uhk.watchdog.watchdogmobile.gui.mainScreen.viewpager.PageAdapter;
import uhk.watchdog.watchdogmobile.gui.utils.RecyclerAdapterListener;
import uhk.watchdog.watchdogmobile.gui.utils.RecyclerItemTouchListener;
import uhk.watchdog.watchdogmobile.app.utils.LoggingUtils;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tobous on 6. 11. 2014.
 *
 */
public class MainActivity extends AppCompatActivity implements RecyclerItemTouchListener, ViewPager.OnPageChangeListener {

    /**
     *
     */
    private final AppCore mAppCore = AppCore.getInstance();

    /**
     *
     */
    private AppConfig mAppConfig = AppConfig.getInstance();

    /**
     *
     */
    private ActionBar mActionBar;

    /**
     *
     */
    private ActionBarDrawerToggle toggle;

    /**
     *
     */
    private DrawerLayout mDrawerLayout;

    /**
     *
     */
    private RelativeLayout drawerRelativeLayout;

    /**
     *
     */
    private RecyclerView mRecyclerViewNavDrawer;

    /**
     *
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     *
     */
    private DrawerNavAdapter mDrawerNavAdapter;

    /**
     *
     */
    private PageAdapter mPageAdapter;

    /**
     *
     */
    private ViewPager mViewPager;

    /**
     *
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     *
     */
    private ArrayList<DrawerItem> mMenuItems;

    /**
     *
     */
    private RecyclerAdapterListener mAdapterListener;

    /**
     *
     */
    private static final String EXTRA_KEY_SELECTED_TAB = "selected_tab";

    /**
     *
     */
    private String[] tabs;

    /**
     *
     */
    private int previousPosition = 0;

    AlertDialog alertDialogPassword;

    /**
     * Core receiver
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            Log.i(this.getClass().getName(),"received broadcast with type : " + extras.getInt(NetworkCommunication.BROADCASTS_TYPE+""));

            switch(extras.getInt(NetworkCommunication.BROADCASTS_TYPE+"")) {
                case NetworkCommunication.BROADCASTS_AUTHENTISATION_FAILED:
                    if(alertDialogPassword != null && alertDialogPassword.isShowing()) {
                        alertDialogPassword.setMessage("Špatné heslo nebo uživatel. jméno");
                    } else {
                        createDialog();
                    }
                    break;
                case NetworkCommunication.BROADCASTS_AUTHENTISATION_SUCCESS:
                    if(alertDialogPassword.isShowing()) {
                        alertDialogPassword.cancel();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.tabs = new String[] {getResources().getString(R.string.main_tab_status_app),getResources().getString(R.string.main_tab_status_pacient)};

        this.mPageAdapter = new PageAdapter(
                getSupportFragmentManager(), tabs);
        this.mViewPager = (ViewPager) findViewById(R.id.main_view_pager);
        this.mActionBar = getSupportActionBar();
        this.mAdapterListener = new RecyclerAdapterListener(this, this);
        this.mMenuItems = new ArrayList<>();
        this.mDrawerNavAdapter = new DrawerNavAdapter(mMenuItems);
        this.mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.main_sliding_tab);
        this.drawerRelativeLayout = (RelativeLayout) findViewById(R.id.main_nav_drawer_layout);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        this.mRecyclerViewNavDrawer = (RecyclerView) findViewById(R.id.main_nav_drawer_list_view);
        this.mLayoutManager = new LinearLayoutManager(this);
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.menu_drawer_open, R.string.menu_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            }
        };

        mViewPager.setAdapter(mPageAdapter);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.action_bar_selected_background);
            }
        });

        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(this);
        mRecyclerViewNavDrawer.setHasFixedSize(true);
        mRecyclerViewNavDrawer.setLayoutManager(mLayoutManager);
        mRecyclerViewNavDrawer.setAdapter(mDrawerNavAdapter);
        mRecyclerViewNavDrawer.addOnItemTouchListener(mAdapterListener);
        toggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(toggle);
        mActionBar.setElevation(0);

        if (savedInstanceState != null) {
            previousPosition = savedInstanceState.getInt(EXTRA_KEY_SELECTED_TAB);

        }

        mViewPager.setCurrentItem(previousPosition);
        mViewPager.addOnPageChangeListener(this);

        LoggingUtils.configure();
        registerReceiver();
        startServices();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshActivity();
        registerReceiver();
    }

    private void fillDrawer() {
        mMenuItems.clear();

        if(!mAppConfig.isHorizontalMenu()) {
            mMenuItems.add(new DrawerItem(R.drawable.ic_app, 0, getResources().getString(R.string.nav_drawer_header_menu)));
            mMenuItems.add(new DrawerItem(R.drawable.ic_schedule, 1, getResources().getString(R.string.main_tab_status_app)));
            mMenuItems.add(new DrawerItem(R.drawable.ic_classroom, 1, getResources().getString(R.string.main_tab_status_pacient)));
        }

        mMenuItems.add(new DrawerItem(R.drawable.ic_setting, 1, getResources().getString(R.string.nav_drawer_item_settings)));
        mMenuItems.add(new DrawerItem(R.drawable.ic_about, 1, getResources().getString(R.string.nav_drawer_item_about)));

        mDrawerNavAdapter.notifyDataSetChanged();
    }

    /**
     *
     */
    public void refreshActivity() {
        for (int i = 0; i < mPageAdapter.getCount(); i++) {
            FragmentListener fragment = (FragmentListener) mPageAdapter.getItem(i);
            fragment.refreshFragment();
        }

        if (mAppConfig.isHorizontalMenu()) {
            mSlidingTabLayout.show();
        } else {
            mSlidingTabLayout.hide();
        }

        fillDrawer();

    }

    /**
     *
     * @param position
     */
    private void selectItem(int position) {
        String title = mMenuItems.get(position).getTitle();

        if(title.equals(getResources().getString(R.string.main_tab_status_app))) {
            mViewPager.setCurrentItem(0);
        }
        if(title.equals(getResources().getString(R.string.main_tab_status_pacient))) {
            mViewPager.setCurrentItem(1);
        }
        if(title.equals(getResources().getString(R.string.nav_drawer_item_settings))) {
            startActivity(new Intent(this, SettingActivity.class));
        }
        if(title.equals(getResources().getString(R.string.nav_drawer_item_about))) {
            startActivity(new Intent(this, AboutActivity.class));
        }

        mDrawerLayout.closeDrawer(drawerRelativeLayout);
    }

    /**
     *
     */
    public void startServices() {
        Intent intent = new Intent(this, WatchDogService.class);
        startService(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();

    }


    @Override
    public void onLongTouch(int position) {
        selectItem(position);
        mDrawerLayout.closeDrawers();

    }

    @Override
    public void onShortTouch(int position) {
        selectItem(position);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * registers broadcast receiver
     */
    private void registerReceiver() {
        this.registerReceiver(receiver, new IntentFilter(NetworkCommunication.INTENT_FILTER_WATCH_DOG_MAIN));
        Log.i(this.getClass().getName(),"registered receiver");
    }

    /**
     * unregisters broadcast receiver
     */
    private void unregisterReceiver() {
        this.unregisterReceiver(receiver);
        Log.i(this.getClass().getName(),"unregistered receiver");
    }

    public void createDialog() {
        Log.v("showing activityyy","aaaa");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("První konfigurace");

        LinearLayout layout = new LinearLayout(this);

        final TextView txt_login_lbl = new TextView(this);
        final TextView txt_password_lbl = new TextView(this);

        txt_login_lbl.setText("Login:");
        txt_password_lbl.setText("Heslo:");

        final EditText txt_login = new EditText(this);
        final EditText txt_password = new EditText(this);
        txt_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        layout.setOrientation(LinearLayout.VERTICAL); //1 is for vertical orientation
        layout.addView(txt_login_lbl);
        layout.addView(txt_login);
        layout.addView(txt_password_lbl);
        layout.addView(txt_password);

        alertDialogBuilder.setView(layout);

        // set dialog message
        alertDialogBuilder
                .setMessage("Zadej přihlašovací údaje")
                .setCancelable(false)
                .setPositiveButton("Ulož", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAppConfig.setUserLogin(txt_login.getText().toString());
                        mAppConfig.setUserPassword(txt_password.getText().toString());
                        sendMessage(NetworkCommunication.BROADCASTS_AUTHENTICATE);
                        dialog.cancel();

                    }
                })
                .setNegativeButton("Zruš", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });



        // create alert dialog
        alertDialogPassword = alertDialogBuilder.create();

        // show it
        alertDialogPassword.show();

    }

    /**
     * sends broadcast
     * @param message type of message
     */
    private void sendMessage(int message) {
        Intent intentMessage = new Intent(NetworkCommunication.INTENT_FILTER_WATCH_DOG_MAIN);
        intentMessage.putExtra(NetworkCommunication.BROADCASTS_TYPE + "", message);
        sendBroadcast(intentMessage);
        Log.i(this.getClass().getName(),"broadcast message with type : " + message + " sent");
    }

}
