package ru.hotel72.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 24.01.13
 * Time: 0:15
 * To change this template use File | Settings | File Templates.
 */
public class StartActivity extends BaseActivity {
    private StartActivity context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        setContentView(R.layout.start);

        Button flatListBtn = (Button)findViewById(R.id.btn_group).findViewById(R.id.flatList_btn_layout).findViewById(R.id.flatList);
        flatListBtn.setOnClickListener(onClickListener);

        Button settingsBtn = (Button)findViewById(R.id.settings);
        settingsBtn.setOnClickListener(onClickListener);

        Button mapBtn = (Button)findViewById(R.id.btn_group).findViewById(R.id.map_btn_layout).findViewById(R.id.map);
        mapBtn.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            Intent intent;
            switch(v.getId()){
                case R.id.flatList:
                    intent = new Intent(context, FlatListActivity.class);
                    break;
                case R.id.settings:
                    intent = new Intent(context, SettingsActivity.class);
                    break;
                case R.id.map:
                    intent = new Intent(context, FlatMapActivity.class);
                    break;
                default:
                    intent = new Intent(context, StartActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };
}