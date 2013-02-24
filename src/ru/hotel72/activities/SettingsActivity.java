package ru.hotel72.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 19.02.13
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private boolean mIsUserBounded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);
        setButtons();

        String city = getIntent().getStringExtra(getString(R.string.cityName));
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
            TextView cityName = (TextView) findViewById(R.id.mainLayout).findViewById(R.id.cityLayout).findViewById(R.id.citeName);
            cityName.setText(city);
        }
    }

    private void setButtons() {
        View returnBtn = findViewById(R.id.headerLayout).findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(this);

        View mainLayout = findViewById(R.id.mainLayout);

        View citySelection = mainLayout.findViewById(R.id.cityLayout);
        citySelection.setOnClickListener(this);

        View moreDetails = mainLayout.findViewById(R.id.moreDetails);
        moreDetails.setOnClickListener(this);

        View boundBnt = mainLayout.findViewById(R.id.bound);
        boundBnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.returnBtn:
                intent = new Intent(this, StartActivity.class);
                break;
            case R.id.cityLayout:
                intent = new Intent(this, CitySelectionActivity.class);
                break;
            case R.id.moreDetails:
                intent = new Intent(this, MoreDetailsActivity.class);
                break;
            case R.id.bound:
                intent = new Intent(this, BoundActivity.class);
                intent.putExtra(getString(R.string.is_user_bound), mIsUserBounded);
                break;
            default:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
