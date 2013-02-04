package ru.hotel72.activities.adapters;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MAX_OFF_PATH = 250;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
        } catch (Exception e) {
        }
        return false;
    }

}
