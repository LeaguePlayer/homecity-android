package ru.hotel72.accessData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.jakewharton.disklrucache.DiskLruImageCache;
import ru.hotel72.R;
import ru.hotel72.utils.BitmapUtils;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.Utils;

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

    private DiskLruImageCache mDiskCache;
    private LruCache<String, Bitmap> mMemoryCache;

    private PhotosLoader photoLoaderThread = new PhotosLoader();

    private boolean useStab;
    private ImageDownloaderType downloaderType;

    public ImageDownloader(Context context) {

        mDiskCache = new DiskLruImageCache(context, "hotel72", 104857600, Bitmap.CompressFormat.JPEG, 100);
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };


        //Make the background thread low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
    }

    final int stub_id = R.drawable.substrate;

    public void DisplayImage(String url, String profilePic, ImageView imageView, ImageDownloaderType type) {
        DisplayImage(url, profilePic, imageView, type, true);
    }

    public void DisplayImage(String url, String profilePic, ImageView imageView, ImageDownloaderType type, boolean useStab) {
        this.downloaderType = type;
        this.useStab = useStab;

        String key = String.format("%s_%s", downloaderType.name(), profilePic.replace(".jpg", "")).toLowerCase();

        if (mMemoryCache.snapshot().containsKey(key)) {
            imageView.setImageBitmap(mMemoryCache.get(key));
            imageView.setVisibility(View.VISIBLE);
        } else {
            queuePhoto(url, imageView, key);
            if (useStab) {
                imageView.setImageResource(stub_id);
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void queuePhoto(String url, ImageView imageView, String profilePic) {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(url, imageView, profilePic);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        if (photoLoaderThread.getState() == Thread.State.NEW)
            photoLoaderThread.start();
    }

    private Bitmap getBitmap(String url, String key) {

        try {
            Bitmap bitmap;

            if (mDiskCache.containsKey(key)) {
                bitmap = mDiskCache.getBitmap(key);
            } else {

                InputStream is = new URL(url).openStream();
                byte[] data = Utils.readBytes(is);
                is.close();
                bitmap = BitmapUtils.scaleBitmap(data);
                mDiskCache.put(key, bitmap);
            }

            mMemoryCache.put(key, bitmap);

            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public String key;
        public Activity activity;

        public PhotoToLoad(String u, ImageView i, String key) {
            this.url = u;
            this.imageView = i;
            this.key = key;
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

            System.gc();
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
                        final PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToLoad) {
                            photoToLoad = photosQueue.photosToLoad.pop();
                        }

                        final Bitmap bmp = getBitmap(photoToLoad.url, photoToLoad.key);

                        final Context context = photoToLoad.imageView.getContext();
                        Activity a = (Activity) context;

                        Object tag = photoToLoad.imageView.getTag();
                        if (tag != null && tag.equals(photoToLoad.url)) {
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (bmp != null) {
                                            ImageViewAnimatedChange(context, photoToLoad.imageView, bmp);
                                        photoToLoad.imageView.setVisibility(View.VISIBLE);
                                    } else {
                                        if(useStab) {
                                            photoToLoad.imageView.setImageResource(stub_id);
                                        } else {
                                            photoToLoad.imageView.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
            System.gc();
        }
    }

    private static void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}
