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

    private static ImageDownloader downloader;

    public static ImageDownloader getImageDownloader(Context context, ImageDownloaderType type){
        if(downloader == null){
            downloader = new ImageDownloader(context, type);
        }

        return downloader;
    }
}
