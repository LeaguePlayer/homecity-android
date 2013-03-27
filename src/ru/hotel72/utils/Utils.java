package ru.hotel72.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.Display;

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

}
