package ru.hotel72.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Gallery;
import ru.hotel72.R;
import ru.hotel72.activities.adapters.GalleryAdapter;
import ru.hotel72.domains.Flat;
import ru.hotel72.domains.Photo;
import ru.hotel72.utils.DataTransfer;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final String flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        photos = ((Flat) DataTransfer.get(flatId)).photos;

        setContentView(R.layout.flat_photo_gallery);

        Gallery gallery = (Gallery) findViewById(R.id.gallery);

        Object obj = getLastNonConfigurationInstance();
        if (null != obj) {
            galleryAdapter = (GalleryAdapter) obj;
        } else {
            galleryAdapter = new GalleryAdapter(this, R.layout.gallary_item, photos);
        }

        gallery.setAdapter(galleryAdapter);
    }

    public Object onRetainNonConfigurationInstance() {
        return galleryAdapter;
    }
}
