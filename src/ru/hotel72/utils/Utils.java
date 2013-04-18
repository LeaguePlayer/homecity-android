package ru.hotel72.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.view.Display;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.03.13
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static int getScreenOrientation(Activity activity)
    {
        Display getOrient = activity.getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    public static String getCostString(Integer cost){
        String costStr = cost.toString();
        Integer temp = (cost / 1000);
        String one = temp.toString();
        String two = costStr.replace(one, "");
        return String.format("%s %s", one, two);
    }

    public static final int IO_BUFFER_SIZE = 8 * 1024;

    private Utils() {};

    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

}
