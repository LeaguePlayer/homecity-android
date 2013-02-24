package ru.hotel72.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 24.02.13
 * Time: 16:46
 * To change this template use File | Settings | File Templates.
 */
public class SingleSlideGallery extends Gallery {

    public SingleSlideGallery(Context context) {
        super(context);
    }

    public SingleSlideGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SingleSlideGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int kEvent;
        //Check if scrolling left
        if (isScrollingLeft(e1, e2)) {
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        }
        //Otherwise scrolling right
        else {
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);
        return true;
    }
}
