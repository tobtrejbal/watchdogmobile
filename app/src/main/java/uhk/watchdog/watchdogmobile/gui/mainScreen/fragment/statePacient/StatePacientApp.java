package uhk.watchdog.watchdogmobile.gui.mainScreen.fragment.statePacient;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uhk.watchdog.watchdogmobile.app.AppCore;
import uhk.watchdog.watchdogmobile.app.AppState;
import uhk.watchdog.watchdogmobile.R;
import uhk.watchdog.watchdogmobile.gui.mainScreen.fragment.FragmentListener;

/**
 * Created by Ondra on 31. 1. 2016.
 */
public class StatePacientApp extends Fragment implements FragmentListener {

    /**
     *
     */
    private AppCore mAppCore;

    /**
     *
     */
    private AppState mAppState;

    /**
     *
     */
    private Handler mHandler;

    /**
     *
     */
    private Runnable mRefreshScreenPeriodically;

    /**
     *
     */
    LayoutInflater mInflater;

    /**
     *
     */
    public StatePacientApp() {
        this.mAppCore = AppCore.getInstance();
        this.mAppState = AppState.getInstance();
        this.mHandler = new Handler();

        this.mRefreshScreenPeriodically = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 10 * 1000);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.fragment_state_pacient, container, false);

        this.mInflater = inflater;

        return V;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRefreshScreenPeriodically);
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void refreshFragment() {

    }
}
