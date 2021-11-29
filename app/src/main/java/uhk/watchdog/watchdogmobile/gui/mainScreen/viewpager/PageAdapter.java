package uhk.watchdog.watchdogmobile.gui.mainScreen.viewpager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import uhk.watchdog.watchdogmobile.gui.mainScreen.fragment.stateApp.StateAppFragment;
import uhk.watchdog.watchdogmobile.gui.mainScreen.fragment.statePacient.StatePacientApp;

/**
 * Created by Tobous on 5. 2. 2015.
 */
public class PageAdapter extends FragmentPagerAdapter {

    /**
     *
     */
    private String[] tags;

    /**
     *
     */
    private StateAppFragment mStateAppFragment;

    /**
     *
     */
    private StatePacientApp mStatePacientApp;

    /**
     *
     * @param fm
     * @param tags
     */
    public PageAdapter(FragmentManager fm, String[] tags) {
        super(fm);
        this.tags = tags;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                if (mStateAppFragment == null) {
                    mStateAppFragment = new StateAppFragment();
                    Log.v("returning", "fragment " + index);
                    return mStateAppFragment;
                } else {
                    Log.v("returning", "fragment " + index);
                    return mStateAppFragment;
                }
            case 1:
                if (mStatePacientApp == null) {
                    mStatePacientApp = new StatePacientApp();
                    Log.v("returning", "fragment " + index);
                    return mStatePacientApp;
                } else {
                    Log.v("returning", "fragment " + index);
                    return mStatePacientApp;
                }
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return tags[0];
            case 1:
                return tags[1];
        }
        return null;
    }
}
