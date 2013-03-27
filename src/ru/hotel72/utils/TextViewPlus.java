package ru.hotel72.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import ru.hotel72.R;

public class TextViewPlus extends TextView {
    private static final String TAG = "TextView";
    private float mAngle;
    private float mPivotX;
    private float mPivotY;

    public TextViewPlus(Context context) {
        super(context);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        setCustomFont(context ,array);
        setAngle(array);
        array.recycle();
    }

    private void setCustomFont(Context ctx, TypedArray array) {
        String customFont = array.getString(R.styleable.TextViewPlus_customFont);
        setCustomFont(ctx, customFont);
    }

    private void setAngle(TypedArray array) {
        mAngle = array.getFloat(R.styleable.TextViewPlus_angle, 0.0f);
        mPivotX = array.getFloat(R.styleable.TextViewPlus_pivotX, 0.0f);
        mPivotY = array.getFloat(R.styleable.TextViewPlus_pivotY, 0.0f);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttrs(context, attrs);
    }

    public boolean setCustomFont(Context ctx, String customFont) {
        Typeface tf = null;
        try {
            tf = Typefaces.get(ctx, customFont);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(mAngle, mPivotX, mPivotY);
//        canvas.rotate(18.4f, 0.0f, 0.0f);
        super.onDraw(canvas);
        canvas.restore();
    }
}
