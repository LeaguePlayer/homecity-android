package ru.hotel72.utils;

import android.content.Context;
import android.graphics.Bitmap;
import ru.hotel72.accessData.ImageDownloader;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class ImageHelper {

    private static HashMap<ImageDownloaderType, ImageDownloader> downloader = new HashMap<ImageDownloaderType, ImageDownloader>();
//    private static ImageDownloader imageDownloader;

    public static ImageDownloader getImageDownloader(Context context, ImageDownloaderType type){
        if(downloader.get(type) == null){
            downloader.put(type, new ImageDownloader(context, type));
        }

        return downloader.get(type);

//        if(imageDownloader == null){
//            imageDownloader = new ImageDownloader(context);//, type);
//        }
//
//        return imageDownloader;
    }

    public static Bitmap ScaleBitmap(Bitmap bm, float scalingFactor) {
        int scaleHeight = (int) (bm.getHeight() * scalingFactor);
        int scaleWidth = (int) (bm.getWidth() * scalingFactor);

        return Bitmap.createScaledBitmap(bm, scaleWidth, scaleHeight, true);
    }
}
