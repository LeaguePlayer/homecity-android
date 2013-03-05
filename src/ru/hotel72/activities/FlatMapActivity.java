package ru.hotel72.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.accessData.GetAllCoordsTask;
import ru.hotel72.accessData.SearchTask;
import ru.hotel72.domains.MapElement;
import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.02.13
 * Time: 22:28
 * To change this template use File | Settings | File Templates.
 */
public class FlatMapActivity extends BaseActivity implements View.OnClickListener, OnBalloonListener {
    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private Overlay mOverlay;
    private HashMap<GeoPoint, MapElement> elementHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);

        final MapView mapView = (MapView) findViewById(R.id.map);
        mMapController = mapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        mOverlayManager.getMyLocation().setEnabled(true);

        setButtons();

        new GetAllCoordsTask(this).execute();
    }

    private void setButtons() {
        View returnBtn = findViewById(R.id.headerLayout).findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(this);

        View searchBtn = findViewById(R.id.mapSearch).findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
    }


    public void showHotels(ArrayList<MapElement> mapElements) {
        elementHashMap = new HashMap<GeoPoint, MapElement>();

        mOverlay = new Overlay(mMapController);
        Resources res = getResources();

        for (int i = 0; i < mapElements.size(); i++) {
            MapElement element = mapElements.get(i);

            if(element.coords == null)
                continue;

            OverlayItem point = new OverlayItem(
                    new GeoPoint(element.coords[1], element.coords[0]),
                    res.getDrawable(R.drawable.a));

            BalloonItem balloon = new BalloonItem(this, point.getGeoPoint());
            balloon.setText(element.address + " (" + element.count + ")");
            balloon.setOnBalloonListener(this);
            point.setBalloonItem(balloon);

            mOverlay.addOverlayItem(point);

            elementHashMap.put(point.getGeoPoint(), element);
        }

        mOverlayManager.addOverlay(mOverlay);
        setZoomSpan();
    }

    private void setZoomSpan(){
        List<OverlayItem> list = mOverlay.getOverlayItems();
        double maxLat, minLat, maxLon, minLon;
        maxLat = maxLon = Double.MIN_VALUE;
        minLat = minLon = Double.MAX_VALUE;
        for (int i = 0; i < list.size(); i++){
            GeoPoint geoPoint = list.get(i).getGeoPoint();
            double lat = geoPoint.getLat();
            double lon = geoPoint.getLon();

            maxLat = Math.max(lat, maxLat);
            minLat = Math.min(lat, minLat);
            maxLon = Math.max(lon, maxLon);
            minLon = Math.min(lon, minLon);
        }
        mMapController.setZoomToSpan(maxLat - minLat, maxLon - minLon);
        mMapController.setPositionNoAnimationTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));
    }

    @Override
    public void onClick(View view) {

        Intent intent;
        switch (view.getId()){
            case R.id.returnBtn:
                intent = new Intent(this, StartActivity.class);
                break;
            case R.id.searchBtn:
                searchPlace();
                return;
            default:
                intent = new Intent(this, StartActivity.class);
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void searchPlace() {
        TextView searchText = (TextView) findViewById(R.id.mapSearch).findViewById(R.id.searchText);
        new SearchTask(this, searchText.getText().toString().trim()).execute();
    }

    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view) {

        OverlayItem item = balloonItem.getOverlayItem();
        if(elementHashMap.containsKey(item.getGeoPoint())){
            MapElement element = elementHashMap.get(item.getGeoPoint());

            Intent intent;

            if(element.count == 1){
                intent = new Intent(this, FlatActivity.class);
                intent.putExtra(getString(R.string.dataTransferFlatId), element.hotels.get(0));
            }
            else {
                intent = new Intent(this, FlatListActivity.class);
                intent.putIntegerArrayListExtra(getString(R.string.flatIds), element.hotels);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onBalloonShow(BalloonItem balloonItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBalloonHide(BalloonItem balloonItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBalloonAnimationStart(BalloonItem balloonItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem balloonItem) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void centrefyMap(double[] coords) {
        float zoom = mMapController.getZoomCurrent();
        mMapController.setPositionNoAnimationTo(new GeoPoint(coords[1], coords[0]));
    }
}
