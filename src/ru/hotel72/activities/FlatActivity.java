package ru.hotel72.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.activities.adapters.GalleryAdapter;
import ru.hotel72.activities.adapters.HotelGestureDetector;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.HorizontalListView;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 06.02.13
 * Time: 23:02
 * To change this template use File | Settings | File Templates.
 */
public class FlatActivity extends BaseActivity implements View.OnClickListener {

    private Flat flat;
    private GalleryAdapter galleryAdapter;
    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private String flatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        setContentView(R.layout.flat);

        setBtns();
        setContent();

        initGallery();
        initMap();
    }

    private void setContent() {
        TextView textView = (TextView) this.findViewById(R.id.scrollView)
                .findViewById(R.id.mainLayout)
                .findViewById(R.id.infoLayout)
                .findViewById(R.id.shortDesc);

        textView.setText(flat.short_desc);
    }

    private void initGallery() {
        HorizontalListView gallery = (HorizontalListView) findViewById(R.id.gallery);

        Object obj = getLastNonConfigurationInstance();
        if (null != obj) {
            galleryAdapter = (GalleryAdapter) obj;
        } else {
            galleryAdapter = new GalleryAdapter(this, R.layout.gallary_item, flat.photos);
        }

        gallery.setAdapter(galleryAdapter);

        final GestureDetector gestureDetector = new GestureDetector(new HotelGestureDetector(this));
        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        gallery.setOnTouchListener(gestureListener);
    }

    private void initMap() {
        final MapView mapView = (MapView) findViewById(R.id.map);
        mMapController = mapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        mOverlayManager.getMyLocation().setEnabled(false);
        showBalloon(flat.coords);
    }

    private void setBtns() {

        View mainL = this.findViewById(R.id.scrollView).findViewById(R.id.mainLayout);

        View returnBtn = mainL.findViewById(R.id.headerLayout)
                .findViewById(R.id.returnBtn);

        returnBtn.setOnClickListener(this);

        View infoLayout = mainL.findViewById(R.id.infoLayout);

        View callBtn = infoLayout.findViewById(R.id.footerLayout).findViewById(R.id.phoneBooking);
        callBtn.setOnClickListener(this);

        View bookingBtn = mainL.findViewById(R.id.booking);
        bookingBtn.setOnClickListener(this);
    }

    public void showBalloon(double[] coords){
        // Create a balloon model for the object
        BalloonItem balloon = new BalloonItem(this, new GeoPoint(coords[1] , coords[0]));
        balloon.setText(flat.street);
        mMapController.showBalloon(balloon);
        mMapController.setPositionNoAnimationTo(new GeoPoint(coords[1], coords[0]), 15);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.returnBtn:
                Intent intent = new Intent(this, FlatListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.phoneBooking:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                callIntent.setData(Uri.parse("tel:" + getString(R.string.bookingNumber)));
                startActivity(callIntent);
                break;

            case R.id.booking:
                Intent bookingIntent = new Intent(this, BookingActivity.class);
                bookingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bookingIntent.putExtra("flatId", flatId);
                startActivity(bookingIntent);
                break;
        }
    }
}
