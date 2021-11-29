package uhk.watchdog.watchdogmobile.gui.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Tob on 1. 2. 2016.
 */
public class RecyclerAdapterListener implements RecyclerView.OnItemTouchListener {

    /**
     *
     */
    private GestureDetector mGestureDetector;

    /**
     *
     */
    private RecyclerItemTouchListener mRecyclerItemTouchListener;

    /**
     *
     */
    private View mChildView;

    /**
     *
     */
    private int childViewPosition;

    /**
     *
     * @param context
     * @param listener
     */
    public RecyclerAdapterListener(Context context, RecyclerItemTouchListener listener) {
        this.mGestureDetector = new GestureDetector(context, new GestureListener());
        this.mRecyclerItemTouchListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mChildView = rv.findChildViewUnder(e.getX(), e.getY());
        childViewPosition = rv.getChildAdapterPosition(mChildView);
        mGestureDetector.onTouchEvent(e);

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    protected class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (mChildView != null) {
                mRecyclerItemTouchListener.onShortTouch(childViewPosition);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if (mChildView != null) {
                mRecyclerItemTouchListener.onLongTouch(childViewPosition);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            // Best practice to always return true here.
            // http://developer.android.com/training/gestures/detector.html#detect
            return true;
        }
    }
}
