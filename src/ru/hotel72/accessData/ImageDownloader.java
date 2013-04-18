package ru.hotel72.accessData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import com.jakewharton.disklrucache.DiskLruImageCache;
import ru.hotel72.R;
import ru.hotel72.utils.ImageDownloaderType;

import java.io.*;
import java.net.URL;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 28.01.13
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */
public class ImageDownloader {

    private static DiskLruImageCache cache;

    private boolean useStab;
    private Context context;
    private ImageDownloaderType downloaderType;

    public ImageDownloader(Context context, ImageDownloaderType type) {
        this.context = context;
        downloaderType = type;

        if(cache == null)
            cache = new DiskLruImageCache(context, "hotel72", 104857600, Bitmap.CompressFormat.JPEG, 100);

        //Make the background thread low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
    }

    final int stub_id = R.drawable.substrate;

    public void DisplayImage(String url, String profilePic, Activity activity, ImageView imageView) {
        DisplayImage(url, profilePic, activity, imageView, true);
    }

    public void DisplayImage(String url, String profilePic, Activity activity, ImageView imageView, boolean useStab) {
        this.useStab = useStab;

        String key = String.format("%s_%s", downloaderType.name(), profilePic.replace(".jpg", "")).toLowerCase();

        if (cache.containsKey(key)){
            Bitmap bitmap = cache.getBitmap(key);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }
        else {
            queuePhoto(url, activity, imageView, key);
            if(useStab) {
                imageView.setImageResource(stub_id);}
            else {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private float getBitmapScalingFactor(Bitmap bm, ImageView image, Activity activity) {
        // Get display width from device
        int displayWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

//        // Get margin to use it for calculating to max width of the ImageView
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) image.getLayoutParams();
//        int leftMargin = layoutParams.leftMargin;
//        int rightMargin = layoutParams.rightMargin;
//
//        // Calculate the max width of the imageView
//        int imageViewWidth = displayWidth - (leftMargin + rightMargin);

        // Calculate scaling factor and return it
        return ( (float) displayWidth / (float) bm.getWidth() );
    }

    private void queuePhoto(String url, Activity activity, ImageView imageView, String profilePic) {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(url, imageView, profilePic, activity);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        if (photoLoaderThread.getState() == Thread.State.NEW)
            photoLoaderThread.start();
    }

    private Bitmap getBitmap(String url, String key) {

        //from SD cache
        if (cache.containsKey(key)) {
            Bitmap b = cache.getBitmap(key);
            if (b != null)
                return b;
        }

        //from web
        try {
            Bitmap bitmap;
            InputStream is = new URL(url).openStream();
            bitmap = decodeFile(is);
            is.close();

            cache.put(key, bitmap);

            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(InputStream stream) {

        //decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, o);

        //Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = 70;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        //decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(new BufferedInputStream(stream), null, o2);
    }

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public String key;
        public Activity activity;

        public PhotoToLoad(String u, ImageView i, String key, Activity activity) {
            this.url = u;
            this.imageView = i;
            this.key = key;
            this.activity = activity;
        }
    }

    PhotosQueue photosQueue = new PhotosQueue();

    public void stopThread() {
        photoLoaderThread.interrupt();
    }

    //stores list of photos to download
    class PhotosQueue {
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

        //removes all instances of this ImageView
        public void Clean(ImageView image) {
            for (int j = 0; j < photosToLoad.size(); ) {
                if (photosToLoad.get(j).imageView == image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }

    class PhotosLoader extends Thread {
        public void run() {
            try {
                while (true) {
                    //thread waits until there are any images to load in the queue
                    if (photosQueue.photosToLoad.size() == 0)
                        synchronized (photosQueue.photosToLoad) {
                            photosQueue.photosToLoad.wait();
                        }
                    if (photosQueue.photosToLoad.size() != 0) {
                        PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToLoad) {
                            photoToLoad = photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp = getBitmap(photoToLoad.url, photoToLoad.key);

                        Activity a = (Activity) photoToLoad.imageView.getContext();

                        Object tag = photoToLoad.imageView.getTag();
                        if (tag != null && ((String) tag).equals(photoToLoad.url)) {
                            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView, photoToLoad.activity);
                            a.runOnUiThread(bd);
                        }
                    }
                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }

    PhotosLoader photoLoaderThread = new PhotosLoader();

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;
        private Activity activity;

        public BitmapDisplayer(Bitmap b, ImageView i, Activity activity) {
            bitmap = b;
            imageView = i;
            this.activity = activity;
        }

        public void run() {
            if (bitmap != null) {
//                float scalingFactor = getBitmapScalingFactor(bitmap, imageView, activity);
//                Bitmap newBitmap = ImageHelper.ScaleBitmap(bitmap, scalingFactor);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } else {
                if(useStab) {
                    imageView.setImageResource(stub_id);}
                else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
