package ru.hotel72.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.accessData.GetAllCoordsTask;
import ru.hotel72.accessData.SearchTask;
import ru.hotel72.domains.MapElement;
import ru.hotel72.utils.MapBalloon;
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
    private String mCity;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.map);

        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);
        mCity = pref.getString(getString(R.string.cityName), "");

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

        final EditText search = (EditText) findViewById(R.id.mapSearch).findViewById(R.id.searchText);
        search.setImeActionLabel("Найти", KeyEvent.KEYCODE_ENTER);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    // hide virtual keyboard
                    InputMethodManager imm =
                            (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                    searchPlace();
                    return true;
                }
                return false;
            }
        });
    }


    public void showHotels(ArrayList<MapElement> mapElements) {
        elementHashMap = new HashMap<GeoPoint, MapElement>();

        mOverlay = new Overlay(mMapController);
        Resources res = getResources();

        for (int i = 0; i < mapElements.size(); i++) {
            MapElement element = mapElements.get(i);

            if (element.coords == null)
                continue;

            OverlayItem point = new OverlayItem(
                    new GeoPoint(element.coords[1], element.coords[0]),
                    res.getDrawable(R.drawable.h72_map_point));

            MapBalloon balloon = new MapBalloon(this, point.getGeoPoint());
            balloon.setAddress(element.address);
            balloon.setCount(element.count);
            balloon.setOnBalloonListener(this);
            point.setBalloonItem(balloon);

            mOverlay.addOverlayItem(point);

            elementHashMap.put(point.getGeoPoint(), element);
        }

        mOverlayManager.addOverlay(mOverlay);
        setZoomSpan();
    }

    private void setZoomSpan() {
        List<OverlayItem> list = mOverlay.getOverlayItems();
        double maxLat, minLat, maxLon, minLon;
        maxLat = maxLon = Double.MIN_VALUE;
        minLat = minLon = Double.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
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
        switch (view.getId()) {
            case R.id.returnBtn:
                intent = new Intent(this, StartActivity.class);
                break;
            default:
                intent = new Intent(this, StartActivity.class);
                break;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void searchPlace() {
        TextView searchText = (TextView) findViewById(R.id.mapSearch).findViewById(R.id.searchText);
        String search = String.format("%s %s", mCity, searchText.getText().toString().trim());
        new SearchTask(this, search).execute();
    }

    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view) {

        OverlayItem item = balloonItem.getOverlayItem();
        if (elementHashMap.containsKey(item.getGeoPoint())) {
            MapElement element = elementHashMap.get(item.getGeoPoint());

            Intent intent;

            if (element.count == 1) {
                intent = new Intent(this, FlatActivity.class);
                intent.putExtra(getString(R.string.dataTransferFlatId), element.hotels.get(0));
            } else {
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
