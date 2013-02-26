package ru.hotel72.utils;

import android.content.Context;
import android.graphics.Bitmap;
import ru.hotel72.accessData.ImageDownloader;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class ImageHelper {
    private static ImageDownloader imageDownloader;

    public static ImageDownloader getImageDownloader(Context context){
        if(imageDownloader == null){
            imageDownloader = new ImageDownloader(context);
        }

        return imageDownloader;
    }

    public static Bitmap ScaleBitmap(Bitmap bm, float scalingFactor) {
        int scaleHeight = (int) (bm.getHeight() * scalingFactor);
        int scaleWidth = (int) (bm.getWidth() * scalingFactor);

        return Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, true);
    }
}
