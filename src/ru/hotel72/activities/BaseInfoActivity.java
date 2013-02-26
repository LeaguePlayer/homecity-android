package ru.hotel72.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flatId = getIntent().getStringExtra(getString(R.string.dataTransferFlatId));
        flat = (Flat) DataTransfer.get(flatId);

        setActivityView(R.layout.flat_base_info);
        infoPlaceholder = (RelativeLayout) mActivityLevelView.findViewById(R.id.content);

        setButtons();
    }

    private void setButtons() {
        Button info = (Button) mActivityLevelView.findViewById(R.id.infoBtn);
        info.setOnClickListener(this);

        Button details = (Button) mActivityLevelView.findViewById(R.id.detailsBtn);
        details.setOnClickListener(this);

        Button facilities = (Button) mActivityLevelView.findViewById(R.id.facilitiesBtn);
        facilities.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        Intent intent;

        switch (view.getId()){
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
                intent = new Intent(this, FlatActivity.class);
                intent.putExtra(getString(R.string.dataTransferFlatId), flatId);
                break;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void setInfoView(int id){
        View view = getView(id);
        infoPlaceholder.addView(view);
        infoView = view;

        setButtonsState(id);
    }

    private void setButtonsState(int id) {
        switch (id){
            case R.layout.flat_info:
                Button infoBtn = (Button) mActivityLevelView.findViewById(R.id.infoBtn);
                infoBtn.setEnabled(false);
                break;
            case R.layout.flat_details:
                Button detailsBtn = (Button) mActivityLevelView.findViewById(R.id.detailsBtn);
                detailsBtn.setEnabled(false);
                break;
            case R.layout.flat_facilities:
                Button facilitiesBtn = (Button) mActivityLevelView.findViewById(R.id.facilitiesBtn);
                facilitiesBtn.setEnabled(false);
                break;
        }
    }
}
