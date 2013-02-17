package ru.hotel72.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import ru.hotel72.R;
import ru.hotel72.accessData.ImageDownloader;
import ru.hotel72.domains.Photo;
import ru.hotel72.utils.ImageHelper;

import java.util.ArrayList;

public class GalleryAdapter extends ArrayAdapter<Photo> {
    private Context context;
    private ArrayList<Photo> photos;
    private ViewHolder holder;

    public GalleryAdapter(Context context, int textViewResourceId, ArrayList<Photo> photos) {
        super(context, textViewResourceId, photos);
        this.context = context;
        this.photos = photos;
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

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imgUrl = photos.get(position).url;
        String url = String.format("http://hotel72.ru/index.php/api/GetFile?filename=%s&for=%s", imgUrl, "original");
        holder.image.setTag(url);
        ImageHelper.getImageDownloader(context).DisplayImage(url, imgUrl, (Activity) context, holder.image);

        return convertView;
    }
}
