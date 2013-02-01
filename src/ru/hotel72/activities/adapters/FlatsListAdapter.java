package ru.hotel72.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.*;
import ru.hotel72.R;
import ru.hotel72.accessData.ImageDownloader;
import ru.hotel72.domains.Flat;
import ru.hotel72.domains.Photo;
import ru.hotel72.domains.extension.FlatListExtension;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 27.01.13
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
public class FlatsListAdapter extends ArrayAdapter<Flat> {

    private Context context;
    private ArrayList<Flat> flats;
    private final ImageDownloader imgDownloader;
    private ViewHolder holder;
    private GalleryAdapter galleryAdapter;

    public FlatsListAdapter(Context context, int textViewResourceId, ArrayList<Flat> flats) {
        super(context, textViewResourceId, flats);
        this.context = context;
        this.flats = flats;
        this.imgDownloader = new ImageDownloader(context);
    }

    public static class ViewHolder{
        public TextView cost;
//        public ImageView imageView;
        public Gallery gallery;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null || convertView.getTag() == null) {
            final LayoutInflater mInflate = LayoutInflater.from(context);
            convertView = mInflate.inflate(R.layout.flat_list_item, parent, false);

            holder = new ViewHolder();
            holder.cost = (TextView) convertView.findViewById(R.id.cost);
            holder.gallery = (Gallery) convertView.findViewById(R.id.gallery);
//            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Flat flat = flats.get(position);
        if (flat != null) {

            if (flat.cost != Double.NaN && holder.cost != null) {
                holder.cost.setText(flat.cost.toString());
            }
            galleryAdapter = new GalleryAdapter(context, R.layout.gallary_item, flat.photos);
//            imgDownloader.DisplayImage(url, imgUrl, (Activity)getContext(), holder.imageView);
            holder.gallery.setAdapter(galleryAdapter);
            gestureDetector = new GestureDetector(new MyGestureDetector());
            gestureListener = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            };
            holder.gallery.setOnTouchListener(gestureListener);
        }

        return convertView;
    }

    @Override
    public void add(Flat object) {
        if(flats == null){
            flats = new ArrayList<Flat>();
        }

        if(!FlatListExtension.Contains(flats, object)){
            flats.add(object);
        }
    }

    private class GalleryAdapter extends ArrayAdapter<Photo> {
        private ArrayList<Photo> photos;

        public GalleryAdapter(Context context, int textViewResourceId, ArrayList<Photo> photos) {
            super(context, textViewResourceId, photos);

            this.photos = photos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView image = new ImageView(context);
            String imgUrl = photos.get(position).url;
            String url = String.format("http://hotel72.ru/index.php/api/GetFile?filename=%s&for=%s", imgUrl, "original");
            image.setTag(url);
            imgDownloader.DisplayImage(url, imgUrl, (Activity)getContext(), image);

            return image;
        }
    }

    private static final int SWIPE_MAX_OFF_PATH = 250;
    private GestureDetector gestureDetector;
    private View.OnTouchListener gestureListener;

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
            } catch (Exception e) {
            }
            return false;
        }

    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        private Context context;

        public MyOnItemClickListener(Context context){
            this.context = context;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }

    }
}
