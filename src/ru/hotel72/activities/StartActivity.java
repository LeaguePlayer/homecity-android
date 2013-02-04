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

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button:
                    Intent intent = new Intent(context, FlatListActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}