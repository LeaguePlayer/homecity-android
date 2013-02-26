package ru.hotel72.activities;

import android.os.Bundle;
import android.widget.TextView;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.02.13
 * Time: 0:01
 * To change this template use File | Settings | File Templates.
 */
public class FlatInfoActivity extends BaseInfoActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInfoView(R.layout.flat_info);
        TextView textView = (TextView) infoView.findViewById(R.id.textView);
        textView.setText(flat.full_desc);
    }
}
