package ru.hotel72.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import ru.hotel72.R;
import ru.hotel72.domains.Photo;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.ImageHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryAdapter extends ArrayAdapter<Photo> {
    private Context context;
    private ArrayList<Photo> photos;
    private ImageDownloaderType type;
    private ViewHolder holder;
    private static HashMap<ImageDownloaderType, Size> sizeHashMap;
    private String urlPattern = "http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&w=%d&h=%d&zc=1&q=90";
    private Boolean useStab;

    public GalleryAdapter(Context context, int textViewResourceId, ArrayList<Photo> photos, ImageDownloaderType type) {
        this(context, textViewResourceId, photos, type, null, true);
    }

    public GalleryAdapter(Context context, int textViewResourceId, ArrayList<Photo> photos, ImageDownloaderType type, String urlPattern, Boolean useStab) {
        super(context, textViewResourceId, photos);
        this.useStab = useStab;

        if(urlPattern != null && urlPattern.trim() != ""){
            this.urlPattern = urlPattern;
        }

        this.context = context;
        this.photos = photos;
        this.type = type;

        if(sizeHashMap == null){
            sizeHashMap = new HashMap<ImageDownloaderType, Size>();
        }
    }

    public static class ViewHolder{
        public ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null){
            final LayoutInflater mInflate = LayoutInflater.from(context);
            convertView = mInflate.inflate(R.layout.gallary_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.galleryImage);

            if (type == ImageDownloaderType.LANDSCAPE || type == ImageDownloaderType.PORTRAIT) {
                holder.image.setScaleType(ImageView.ScaleType.CENTER);
            } else {
                holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }


            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String imgUrl = photos.get(position).url;

        if (!sizeHashMap.containsKey(type)) {
            holder.image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Size size = new Size();
                    size.width = holder.image.getWidth();
                    size.height = holder.image.getHeight();
                    sizeHashMap.put(type, size);

                    setImage(holder.image, imgUrl);
                    holder.image.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        } else {
            setImage(holder.image, imgUrl);
        }


        return convertView;
    }

    private void setImage(ImageView view, String imgUrl){
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
        ImageHelper.getImageDownloader(context, type).DisplayImage(url, imgUrl, (Activity) context, view, useStab);
    }
}
