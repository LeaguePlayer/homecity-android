package ru.hotel72.activities.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import ru.hotel72.R;
import ru.hotel72.domains.Photo;
import ru.hotel72.utils.BitmapUtils;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.ImageHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 23.04.13
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Photo> photos;
    private ImageDownloaderType type;
    private String urlPattern = "http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&w=%d&h=%d&zc=1&q=90";
    private Boolean useStab;
    private static HashMap<ImageDownloaderType, Size> sizeHashMap;

    public ImagePagerAdapter(Context context, ArrayList<Photo> photos, ImageDownloaderType type, String urlPattern, Boolean useStab) {
        this.context = context;
        this.photos = photos;
        this.type = type;
        this.useStab = useStab;

        if(urlPattern != null && urlPattern.trim() != ""){
            this.urlPattern = urlPattern;
        }

        if(sizeHashMap == null){
            sizeHashMap = new HashMap<ImageDownloaderType, Size>();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        final LayoutInflater mInflate = LayoutInflater.from(context);
        View convertView = mInflate.inflate(R.layout.gallary_item, null, false);

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.galleryImage);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        container.addView(convertView, 0);

        container.getWidth();

        final String imgUrl = photos.get(position).url;

        if (!sizeHashMap.containsKey(type)) {
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Size size = new Size();
                    size.width = imageView.getWidth();
                    size.height = imageView.getHeight();
                    sizeHashMap.put(type, size);

                    setImage(imageView, imgUrl);
                    imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        } else {
            setImage(imageView, imgUrl);
        }

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        BitmapUtils.calcAvailableMemory("before recycle");

        Drawable drawable = ((ImageView)((View)object).findViewById(R.id.galleryImage)).getDrawable();

        if(drawable != null){
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap.recycle();
            }
        }
        container.removeView((View) object);
        System.gc();

        BitmapUtils.calcAvailableMemory("after recycle");
    }

    private void setImage(ImageView view, String imgUrl){

        Drawable drawable = view.getDrawable();

        if(drawable != null){
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap.recycle();
            }
        }

        Size size = sizeHashMap.get(type);
        String url = String.format(
                urlPattern,
                imgUrl, size.width, size.height);

        if (type == ImageDownloaderType.PORTRAIT) {
            url = String.format(urlPattern, imgUrl, size.width);
        } else if (type == ImageDownloaderType.LANDSCAPE) {
            url = String.format(urlPattern, imgUrl, size.height);
        }

        view.setTag(url);
        ImageHelper.getImageDownloader(context).DisplayImage(url, imgUrl, view, type, useStab);
    }
}