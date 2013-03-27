package ru.hotel72.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 20.03.13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class RotateLinearLayout extends LinearLayout {

    private Matrix mForward = new Matrix();
    private Matrix mReverse = new Matrix();
    private float[] mTemp = new float[2];

    public RotateLinearLayout(Context context) {
        super(context);
    }

    public RotateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.rotate(18.4f, getWidth() / 2, getHeight() / 2);

        mForward = canvas.getMatrix();
        mForward.invert(mReverse);
        canvas.save();
        canvas.setMatrix(mForward); // This is the matrix we need to use for
        // proper positioning of touch events
        super.dispatchDraw(canvas);
        canvas.restore();
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        event.setLocation(getWidth() - event.getX(), getHeight() - event.getY());
        return super.dispatchTouchEvent(event);
    }
}