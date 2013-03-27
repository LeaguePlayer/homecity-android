package ru.hotel72.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 19.02.13
 * Time: 23:38
 * To change this template use File | Settings | File Templates.
 */
public class MoreDetailsActivity extends BaseHeaderActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = this;

        setActivityView(R.layout.more_details);
    }
}
