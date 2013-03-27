package ru.hotel72.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ru.hotel72.R;
import ru.hotel72.utils.InfoDialog;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 19.02.13
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends BaseHeaderActivity implements View.OnClickListener {

    private boolean mIsUserBounded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActivityView(R.layout.settings);
        setButtons();

        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);
        String city = pref.getString(getString(R.string.cityName), "");
        setContent(city);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);
        mIsUserBounded = pref.getBoolean(getString(R.string.is_user_bound), false);
        Button boundBtn = (Button) findViewById(R.id.mainLayout).findViewById(R.id.bound);
        if(mIsUserBounded){
            boundBtn.setText(R.string.update_bound);
        } else {
            boundBtn.setText(R.string.bound_number);
        }
    }

    private void setContent(String city) {
        if(city != null && city != ""){
            TextView cityName = (TextView) mActivityLevelView.
                    findViewById(R.id.mainLayout).findViewById(R.id.cityLayout).findViewById(R.id.citeName);
            cityName.setText(city);
        }
    }

    private void setButtons() {
        View mainLayout = mActivityLevelView.findViewById(R.id.mainLayout);

        View citySelection = mainLayout.findViewById(R.id.cityLayout);
        citySelection.setOnClickListener(this);

        View moreDetails = mainLayout.findViewById(R.id.moreDetails);
        moreDetails.setOnClickListener(this);

        View boundBnt = mainLayout.findViewById(R.id.bound);
        boundBnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        Intent intent = null;
        switch (view.getId()){
            case R.id.cityLayout:
                intent = new Intent(this, CitySelectionActivity.class);
                break;
            case R.id.moreDetails:
                intent = new Intent(this, MoreDetailsActivity.class);
                break;
            case R.id.bound:
                intent = new Intent(this, BoundActivity.class);
                intent.putExtra(getString(R.string.is_user_bound), mIsUserBounded);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                return;
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            boolean show = data.getBooleanExtra(getString(R.string.showDialog), false);
            if (show) {
                String msg = data.getStringExtra(getString(R.string.dialogMessage));
                InfoDialog.showDialog(this, null, msg);
            }
        }
    }
}
