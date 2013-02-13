package ru.hotel72.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import ru.hotel72.R;
import ru.hotel72.activities.adapters.GalleryAdapter;
import ru.hotel72.activities.adapters.HotelGestureDetector;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.HorizontalListView;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        setContentView(R.layout.flat);

        setButtonsListener();

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

        final MapView mapView = (MapView) findViewById(R.id.map);

        mMapController = mapView.getMapController();

        mOverlayManager = mMapController.getOverlayManager();
        // Disable determining the user's location
        mOverlayManager.getMyLocation().setEnabled(false);

        // A simple implementation of map objects
        showBalloon(flat.coords);
    }

    private void setButtonsListener() {
        View a = this.findViewById(R.id.scrollView);
        View b = a.findViewById(R.id.mainLayout);
        View c = b.findViewById(R.id.headerLayout);
        View d = findViewById(R.id.returnBtn);
        d.setOnClickListener(this);
    }

    public void showBalloon(double[] coords){
        // Load required resources
        Resources res = getResources();
        // Create a layer of objects for the map
        Overlay overlay = new Overlay(mMapController);

        // Create an object for the layer
        OverlayItem kremlin = new OverlayItem(new GeoPoint(coords[1] , coords[0]), res.getDrawable(R.drawable.icon));
        // Create a balloon model for the object
        BalloonItem balloonKremlin = new BalloonItem(this,kremlin.getGeoPoint());
        balloonKremlin.setText("Московский Кремль. Здесь можно ещё много о чём написать.");
        //        // Add the balloon model to the object
        kremlin.setBalloonItem(balloonKremlin);
        // Add the object to the layer
        overlay.addOverlayItem(kremlin);

        // Add the layer to the map
        mOverlayManager.addOverlay(overlay);

        mMapController.setZoomToSpan(coords[1] , coords[0]);
        mMapController.setPositionAnimationTo(new GeoPoint(coords[1] , coords[0]));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.returnBtn:
                Intent intent = new Intent(this, FlatListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            break;
        }
    }
}
