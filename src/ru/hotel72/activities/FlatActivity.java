package ru.hotel72.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.accessData.GetFlatTask;
import ru.hotel72.activities.adapters.GalleryAdapter;
import ru.hotel72.domains.Flat;
import ru.hotel72.social.ShareDialog;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.ImageDownloaderType;
import ru.hotel72.utils.InfoDialog;
import ru.hotel72.utils.Utils;
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
    private Integer flatId;
    private FlatActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        flatId = getIntent().getIntExtra(getString(R.string.dataTransferFlatId), -1);
        flat = (Flat) DataTransfer.get(flatId.toString());

        setActivityView(R.layout.flat);

        if (flat != null) {
            setContent();
            initGallery();
            initMap();
            setButtons();
        } else {
            new GetFlatTask(this).execute(new Integer[]{flatId});
        }
    }

    private void setContent() {
        TextView textView = (TextView) mActivityLevelView
                .findViewById(R.id.infoLayout)
                .findViewById(R.id.shortDesc);

        textView.setText(flat.short_desc);

        View costLayout = mActivityLevelView
                .findViewById(R.id.tag_layout)
                .findViewById(R.id.cost_layout);

        TextView cost = (TextView) costLayout.findViewById(R.id.cost);

        SpannableString spanString = new SpannableString(Utils.getCostString(flat.cost));
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        cost.setText(spanString);
    }

    private void initGallery() {
        Gallery gallery = (Gallery) mActivityLevelView.findViewById(R.id.gallery);

        Object obj = getLastNonConfigurationInstance();
        if (null != obj) {
            galleryAdapter = (GalleryAdapter) obj;
        } else {
            galleryAdapter = new GalleryAdapter(this, R.layout.gallary_item, flat.photos, ImageDownloaderType.FLAT);
        }

        gallery.setAdapter(galleryAdapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context, FlatPhotoGalleryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("flatId", flat.id.toString());
                intent.putExtra("position", i);
                DataTransfer.put(flat.id.toString(), flat);

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

        View facilitiesBtn = infoLayout.findViewById(R.id.facilitiesBtn);
        facilitiesBtn.setOnClickListener(this);

        View callBtn = infoLayout.findViewById(R.id.footerLayout).findViewById(R.id.phoneBooking);
        callBtn.setOnClickListener(this);

        View bookingBtn = mActivityLevelView.findViewById(R.id.booking);
        bookingBtn.setOnClickListener(this);

        View share = mActivityLevelView.findViewById(R.id.share);
        share.setOnClickListener(this);
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
                DataTransfer.put(flatId.toString(), flat);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId.toString());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                return;

            case R.id.infoBtn:
                intent = new Intent(this, BaseInfoActivity.class);
                DataTransfer.put(flatId.toString(), flat);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId.toString());
                intent.putExtra(getString(R.string.info_viewId), R.layout.flat_info);
                break;

            case R.id.detailsBtn:
                intent = new Intent(this, BaseInfoActivity.class);
                DataTransfer.put(flatId.toString(), flat);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId.toString());
                intent.putExtra(getString(R.string.info_viewId), R.layout.flat_details);
                break;

            case R.id.facilitiesBtn:
                intent = new Intent(this, BaseInfoActivity.class);
                DataTransfer.put(flatId.toString(), flat);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId.toString());
                intent.putExtra(getString(R.string.info_viewId), R.layout.flat_facilities);
                break;

            case R.id.share:{
                ShareDialog dialog = new ShareDialog(this, flat.hotel_url);
                dialog.showDialog();
                return;
            }

            default:
                intent = new Intent(this, StartActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void updateContent(Flat flat) {
        this.flat = flat;
        setContent();
        initGallery();
        initMap();
        setButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            boolean show = data.getBooleanExtra(getString(R.string.showDialog), false);
            if (show) {
                String msg = data.getBooleanExtra(getString(R.string.error), false)
                        ? data.getStringExtra(getString(R.string.errorMsg))
                        : getString(R.string.booked);
                InfoDialog.showDialog(this, null, msg);
            }
        }
    }
}
