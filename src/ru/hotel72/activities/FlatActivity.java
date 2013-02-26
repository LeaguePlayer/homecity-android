package ru.hotel72.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.activities.adapters.GalleryAdapter;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;
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
public class FlatActivity extends BaseHeaderActivity implements View.OnClickListener {

    private Flat flat;
    private GalleryAdapter galleryAdapter;
    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private String flatId;
    private FlatActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        setActivityView(R.layout.flat);

        setButtons();
        setContent();

        initGallery();
        initMap();
    }

    private void setContent() {
        TextView textView = (TextView) mActivityLevelView
                .findViewById(R.id.infoLayout)
                .findViewById(R.id.shortDesc);

        textView.setText(flat.short_desc);
    }

    private void initGallery() {
        Gallery gallery = (Gallery) mActivityLevelView.findViewById(R.id.gallery);

        Object obj = getLastNonConfigurationInstance();
        if (null != obj) {
            galleryAdapter = (GalleryAdapter) obj;
        } else {
            galleryAdapter = new GalleryAdapter(this, R.layout.gallary_item, flat.photos);
        }

        gallery.setAdapter(galleryAdapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, FlatPhotoGalleryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String flatId = "flat" + flat.id;
                intent.putExtra("flatId", flatId);

                context.startActivity(intent);
            }
        });

//        final GestureDetector gestureDetector = new GestureDetector(new HotelGestureDetector(this));
//        View.OnTouchListener gestureListener = new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event);
//            }
//        };
//        gallery.setOnTouchListener(gestureListener);
    }

    public Object onRetainNonConfigurationInstance() {
        return galleryAdapter;
    }

    private void initMap() {
        final MapView mapView = (MapView) mActivityLevelView.findViewById(R.id.map);
        mMapController = mapView.getMapController();
        mMapController.setEnabled(false);
        mOverlayManager = mMapController.getOverlayManager();
        mOverlayManager.getMyLocation().setEnabled(false);
        showBalloon(flat.coords);
    }

    private void setButtons() {

        View infoLayout = mActivityLevelView.findViewById(R.id.infoLayout);

        View infoBtn = infoLayout.findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(this);

        View detailsBtn = infoLayout.findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(this);

        View callBtn = infoLayout.findViewById(R.id.footerLayout).findViewById(R.id.phoneBooking);
        callBtn.setOnClickListener(this);

        View bookingBtn = mActivityLevelView.findViewById(R.id.booking);
        bookingBtn.setOnClickListener(this);
    }

    public void showBalloon(double[] coords) {
        // Create a balloon model for the object
        BalloonItem balloon = new BalloonItem(this, new GeoPoint(coords[1], coords[0]));
        balloon.setText(flat.street);
        mMapController.showBalloon(balloon);
        mMapController.setPositionNoAnimationTo(new GeoPoint(coords[1], coords[0]), 15);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        Intent intent;

        switch (view.getId()) {
            case R.id.phoneBooking:
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + getString(R.string.bookingNumber)));
                break;

            case R.id.booking:
                intent = new Intent(this, BookingActivity.class);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId);
                break;

            case R.id.infoBtn:
                intent = new Intent(this, FlatInfoActivity.class);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId);
                break;

            case R.id.detailsBtn:
                intent = new Intent(this, FlatDetailsActivity.class);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId);
                break;

            case R.id.facilitiesBtn:
                intent = new Intent(this, FlatFacilitiesActivity.class);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId);
                break;

            default:
                intent = new Intent(this, StartActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
