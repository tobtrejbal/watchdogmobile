package uhk.watchdog.watchdogmobile.gui.mainScreen.fragment.stateApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import uhk.watchdog.watchdogmobile.app.AppConfig;
import uhk.watchdog.watchdogmobile.app.AppState;
import uhk.watchdog.watchdogmobile.R;
import uhk.watchdog.watchdogmobile.core.communication.constants.NetworkCommunication;
import uhk.watchdog.watchdogmobile.gui.mainScreen.fragment.FragmentListener;

/**
 * Created by Tobous on 9. 11. 2015.
 */
public class StateAppFragment extends Fragment implements FragmentListener {

    /**
     *
     */
    private AppState mAppState;

    /**
     *
     */
    private AppConfig mAppConfig;

    /**
     *
     */
    private Handler mHandler;

    /**
     *
     */
    private TextView mTxtLastSend;

    /**
     *
     */
    private TextView mTxtLastSendSuccess;

    /**
     *
     */
    private TextView mTxtSampleFrequency;

    /**
     *
     */
    private TextView mTxtClearBufferFrequency;

    /**
     *
     */
    private TextView mTxtSampleTakenCount;

    /**
     *
     */
    private TextView mTxtDatabaseCount;

    /**
     *
     */
    private Button mBtnOff;

    /**
     *
     */
    private Runnable mRefreshScreenPeriodically;

    /**
     *
     */
    private LayoutInflater mInflater;

    /**
     *
     */
    public StateAppFragment() {
        this.mAppState = AppState.getInstance();
        this.mAppConfig = AppConfig.getInstance();
        this.mHandler = new Handler();

        mRefreshScreenPeriodically = new Runnable() {
            @Override
            public void run() {
                mTxtLastSend.setText(mAppState.getLastDataSend().toString());
                if(mAppState.isLastSendSuccess()) {
                    mTxtLastSendSuccess.setText("úspěšný");
                } else {
                    mTxtLastSendSuccess.setText("neúspěšný");
                }
                mTxtSampleFrequency.setText(String.valueOf(mAppConfig.getSampleRate()));
                mTxtClearBufferFrequency.setText(String.valueOf(mAppConfig.getClearBufferRate()));
                mTxtSampleTakenCount.setText(String.valueOf(mAppState.getSampleSendCounter()));
                mTxtDatabaseCount.setText(String.valueOf(mAppState.getDatabaseCount()));
                mHandler.postDelayed(this, 2 * 1000);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.fragment_state_app, container, false);

        this.mTxtLastSend = (TextView) V.findViewById(R.id.app_status_txt_last_send);
        this.mTxtLastSendSuccess = (TextView) V.findViewById(R.id.app_status_txt_success_send);
        this.mTxtSampleFrequency = (TextView) V.findViewById(R.id.app_status_txt_sample_frequency);
        this.mTxtClearBufferFrequency = (TextView) V.findViewById(R.id.app_status_txt_clear_buffer_frequency);
        this.mTxtSampleTakenCount = (TextView) V.findViewById(R.id.app_status_txt_samle_taken_counter);
        this.mTxtDatabaseCount = (TextView) V.findViewById(R.id.app_status_txt_database_count);

        this.mBtnOff = (Button) V.findViewById(R.id.shutdown);

            mBtnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentMessage = new Intent(NetworkCommunication.INTENT_FILTER_WATCH_DOG_MAIN);
                    intentMessage.putExtra(NetworkCommunication.BROADCASTS_TYPE + "", NetworkCommunication.BROADCASTS_KILL);
                    getActivity().sendBroadcast(intentMessage);
                }
            });

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
        mHandler.post(mRefreshScreenPeriodically);
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
