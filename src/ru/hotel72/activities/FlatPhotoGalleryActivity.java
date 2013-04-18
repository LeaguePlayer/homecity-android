package ru.hotel72.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Gallery;
import ru.hotel72.R;
import ru.hotel72.activities.adapters.GalleryAdapter;
import ru.hotel72.domains.Flat;
import ru.hotel72.domains.Photo;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.Utils;

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
    private Gallery gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final String flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        photos = ((Flat) DataTransfer.get(flatId)).photos;

        setContentView(R.layout.flat_photo_gallery);

        gallery = (Gallery) findViewById(R.id.gallery);

        ImageDownloaderType type = ImageDownloaderType.PORTRAIT;
        String url = "http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&w=%d&far=1&q=90";
        if (Utils.getScreenOrientation(this) == Configuration.ORIENTATION_LANDSCAPE) {
            type = ImageDownloaderType.LANDSCAPE;
            url = "http://hotel72.ru/lib/thumb/phpThumb.php?src=/uploads/gallery/hotels/%s&h=%d&far=1&q=90";
        }

        galleryAdapter = new GalleryAdapter(this, R.layout.gallary_item, photos, type, url, false);

        gallery.setAdapter(galleryAdapter);
        Object position = getLastNonConfigurationInstance();
        if (position != null) {
            gallery.setSelection((Integer) position);
        }
    }

    public Object onRetainNonConfigurationInstance() {
        return gallery.getFirstVisiblePosition();
    }
}
