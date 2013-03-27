package ru.hotel72.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import ru.hotel72.R;
import ru.hotel72.accessData.DBHelper;
import ru.hotel72.accessData.GetFlatHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 9:53
 * To change this template use File | Settings | File Templates.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetFlatHelper.likedFlat = new DBHelper(this).getLikedFlats();
        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);
        String city = pref.getString(getString(R.string.cityName), "");

        if(city == null || city == ""){
            SharedPreferences.Editor ed = pref.edit();
            ed.putString(getString(R.string.cityName), getString(R.string.defaultCityName));
            ed.commit();
        }

        SplashActivity.this.finish();
        Intent mainIntent = new Intent(SplashActivity.this, StartActivity.class);
        SplashActivity.this.startActivity(mainIntent);
    }
}
