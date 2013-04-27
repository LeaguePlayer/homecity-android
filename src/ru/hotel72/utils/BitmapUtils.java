package ru.hotel72.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 23.04.13
 * Time: 5:11
 * To change this template use File | Settings | File Templates.
 */
public class BitmapUtils {

    private static String TAG = "BitmapUtils";

    public static long calcAvailableMemory(){
        return calcAvailableMemory("calcAvailableMemory");
    }

    public static long calcAvailableMemory(String msg)
    {
        long value = Runtime.getRuntime().maxMemory();
        String type = "";
        if (android.os.Build.VERSION.SDK_INT >= 11)
        {
            value = (value / 1024) - (Runtime.getRuntime().totalMemory() / 1024);
            type = "JAVA";
        }
        else
        {
            value = (value / 1024) - (Debug.getNativeHeapAllocatedSize() / 1024);
            type = "NATIVE";
        }
        Log.i(TAG, msg + ", size = " + value + ", type = " + type);
        return value;
    }

    public static Bitmap scaleBitmap(byte[] data) throws IOException {

        calcAvailableMemory();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        System.gc();
        calcAvailableMemory("inJustDecodeBounds");

        int MAX_IMG_SIZE = 1280;

        int sample = 1;
        //Scale image to screen resolution
        if (options.outHeight > MAX_IMG_SIZE || options.outWidth > MAX_IMG_SIZE)
        {
            int heightRatio = Math.round((float) options.outHeight / (float) MAX_IMG_SIZE);
            int widthRatio = Math.round((float) options.outWidth / (float) MAX_IMG_SIZE);
            sample = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        long maxImageMemory = calcAvailableMemory();

        //Scale image to stay within memory limitations
        while (calcBitmapSize(options.outWidth, options.outHeight, sample) > maxImageMemory)
        {
            sample++;
        }

        options.inSampleSize = sample;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = false;

        Bitmap bitmap;

        Log.i(TAG, "scaleBitmap, scaleSample = " + sample);
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        System.gc();
        calcAvailableMemory("BitmapFactory.decodeStream");

        return bitmap;
    }

    private static long calcBitmapSize(int width, int height, int sample)
    {
        long value = ((width/sample) * (height/sample) * 4) / 1024;
        Log.i(TAG, "calcBitmapSize, size = " + value);
        return value;
    }
}
