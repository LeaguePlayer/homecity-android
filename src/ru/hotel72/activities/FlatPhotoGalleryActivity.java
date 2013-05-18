package ru.hotel72.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.Gallery;
import android.widget.ImageView;
import ru.hotel72.R;
import ru.hotel72.activities.adapters.GalleryAdapter;
import ru.hotel72.activities.adapters.ImagePagerAdapter;
import ru.hotel72.activities.adapters.Size;
import ru.hotel72.domains.Flat;
import ru.hotel72.domains.Photo;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.ImageHelper;
import ru.hotel72.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.02.13
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class FlatPhotoGalleryActivity extends Activity {
    private ArrayList<Photo> photos;
    private GalleryAdapter galleryAdapter;
    private Gallery gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final String flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));

        setContentView(R.layout.flat_photo_gallery);

        gallery = (Gallery) findViewById(R.id.gallery);

        ImageDownloaderType type = ImageDownloaderType.PORTRAIT;
        String url = "http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&w=%d&far=1&q=90";
        if (Utils.getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            type = ImageDownloaderType.LANDSCAPE;
            url = "http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&h=%d&far=1&q=90";
        }

//        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
//        ImagePagerAdapter adapter = new ImagePagerAdapter(this, photos, type, url, false);
//        viewPager.setAdapter(adapter);

        int position = getIntent().getIntExtra("position", 0);
        Object tmp = getLastNonConfigurationInstance();
        if (tmp != null) {
            Bundle bundle = (Bundle) tmp;
            if (bundle.containsKey("position"))
                position = bundle.getInt("position");

            if (bundle.containsKey("photos"))
                photos = bundle.getParcelableArrayList("photos");

        } else if(photos == null) {

            Object f = DataTransfer.get(flatId);
            if (f != null) {
                photos = ((Flat) f).photos;
            } else {
                photos = new ArrayList<Photo>();
            }
        }

        galleryAdapter = new GalleryAdapter(this, R.layout.gallary_item, photos, type, url, false);

        gallery.setAdapter(galleryAdapter);
        gallery.setSelection(position);
    }

    public Object onRetainNonConfigurationInstance() {

        Bundle bundle = new Bundle();
        bundle.putInt("position", gallery.getFirstVisiblePosition());
        bundle.putParcelableArrayList("photos", photos);

        return bundle;
    }
}
