package ru.hotel72.activities.adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MAX_OFF_PATH = 250;
    private final ViewConfiguration vc;
    private final int swipeMinDistance;
    private final int swipeThresholdVelocity;

    public MyGestureDetector(Context context) {
        vc = ViewConfiguration.get(context);
        swipeMinDistance = vc.getScaledTouchSlop();
        swipeThresholdVelocity = vc.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (e1.getX() - e2.getX() > swipeMinDistance && Math.abs(velocityX) > swipeThresholdVelocity)
                return false;
        } catch (Exception e) {
        }
        return false;
    }

}
