package uhk.watchdog.watchdogmobile.gui.mainScreen.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Tob on 20. 12. 2015.
 */
public class CustomViewPager extends ViewPager {

    /**
     *
     */
    private boolean isLocked;

    /**
     *
     * @param context
     */
    public CustomViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isLocked && super.onTouchEvent(event);
    }

    /**
     *
     */
    public void toggleLock() {
        isLocked = !isLocked;
    }

    /**
     *
     * @param isLocked
     */
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     *
     * @return
     */
    public boolean isLocked() {
        return isLocked;
    }

}
