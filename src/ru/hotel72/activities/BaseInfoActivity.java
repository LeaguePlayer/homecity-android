package ru.hotel72.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.02.13
 * Time: 1:30
 * To change this template use File | Settings | File Templates.
 */
public class BaseInfoActivity extends BaseHeaderActivity implements View.OnClickListener {

    public RelativeLayout infoPlaceholder;
    public View infoView;

    protected Flat flat;
    private String flatId;
    private Button infoBtn;
    private Button detailsBtn;
    private Button facilitiesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        setActivityView(R.layout.flat_base_info);
        infoPlaceholder = (RelativeLayout) mActivityLevelView.findViewById(R.id.content);

        setButtons();

        final int viewId = getIntent().getIntExtra(getString(R.string.info_viewId), 0);

        setInfoView(viewId);
    }

    private void setButtons() {
        infoBtn = (Button) mActivityLevelView.findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(this);

        detailsBtn = (Button) mActivityLevelView.findViewById(R.id.detailsBtn);
        detailsBtn.setOnClickListener(this);

        facilitiesBtn = (Button) mActivityLevelView.findViewById(R.id.facilitiesBtn);
        facilitiesBtn.setOnClickListener(this);
    }

    private void fillDetails(){
        TextView square = (TextView) infoView.findViewById(R.id.squareLayout).findViewById(R.id.textSquare);
        square.setText(flat.square.toString() + " кв.м.");

        TextView rooms = (TextView) infoView.findViewById(R.id.roomsLayout).findViewById(R.id.textRooms);
        rooms.setText(flat.rooms.toString());

        TextView cost = (TextView) infoView.findViewById(R.id.costLayout).findViewById(R.id.textCost);
        cost.setText(flat.cost.toString() + " р");
    }

    private void fillFacilities() {
        LinearLayout layout = (LinearLayout) infoView.findViewById(R.id.list);

        for(int i=0; i < flat.options.size(); i++){
            View item = getView(R.layout.flat_facilities_item);
            TextView textView = (TextView) item.findViewById(R.id.itemText);
            textView.setText(flat.options.get(i));

            layout.addView(item);
        }
    }

    private void fillInfo() {
        TextView textView = (TextView) infoView.findViewById(R.id.textView);
        textView.setText(flat.full_desc);
    }

    private void setContent(int id){
        infoBtn.setEnabled(true);
        detailsBtn.setEnabled(true);
        facilitiesBtn.setEnabled(true);

        switch (id){
            case R.layout.flat_info:
                infoBtn.setEnabled(false);
                fillInfo();
                break;
            case R.layout.flat_details:
                detailsBtn.setEnabled(false);
                fillDetails();
                break;
            case R.layout.flat_facilities:
                facilitiesBtn.setEnabled(false);
                fillFacilities();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.infoBtn:
                setInfoView(R.layout.flat_info);
                break;

            case R.id.detailsBtn:
                setInfoView(R.layout.flat_details);
                break;

            case R.id.facilitiesBtn:
                setInfoView(R.layout.flat_facilities);
                break;
        }
    }
    public void setInfoView(int id){
        View view = getView(id);
        infoPlaceholder.removeAllViews();
        infoPlaceholder.addView(view);
        infoView = view;

        setContent(id);
    }
}
