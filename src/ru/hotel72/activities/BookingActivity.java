package ru.hotel72.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.accessData.ImageDownloader;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;
import ru.hotel72.utils.ImageHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class BookingActivity extends BaseHeaderActivity implements View.OnClickListener {
    private Flat flat;
    private String flatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityView(R.layout.booking);

        flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        setContent();
        setBtns();
    }

    private void setBtns() {
    }

    private void setContent() {
        View subHeader = mActivityLevelView.findViewById(R.id.subHeader);

        setImg(subHeader);

        TextView rooms = (TextView) subHeader.findViewById(R.id.rooms);
        rooms.setText(flat.rooms.toString() + " комн.");

        TextView address = (TextView) subHeader.findViewById(R.id.address);
        address.setText(flat.street);

        TextView cost = (TextView) subHeader.findViewById(R.id.cost);
        cost.setText(flat.cost.toString());
    }

    private void setImg(View container) {
        if(flat.photos.size() == 0)
            return;

        ImageView image = (ImageView) container.findViewById(R.id.imageView);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String imgUrl = flat.photos.get(0).url;
        String url = String.format("http://hotel72.ru/index.php/api/GetFile?filename=%s&for=%s", imgUrl, "original");
        ImageHelper.getImageDownloader(this).DisplayImage(url, imgUrl, this, image);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.booking:
                break;
        }
    }
}
