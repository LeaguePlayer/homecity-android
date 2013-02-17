package ru.hotel72.activities;

import android.content.Intent;
import android.os.Bundle;
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
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetFlatHelper.likedFlat = new DBHelper(this).getLikedFlats();

        SplashActivity.this.finish();
        Intent mainIntent = new Intent(SplashActivity.this, StartActivity.class);
        SplashActivity.this.startActivity(mainIntent);
    }
}
