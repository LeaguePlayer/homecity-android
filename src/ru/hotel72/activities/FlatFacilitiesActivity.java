package ru.hotel72.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.02.13
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public class FlatFacilitiesActivity extends BaseInfoActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInfoView(R.layout.flat_facilities);
        
        setContent();
    }

    private void setContent() {
        LinearLayout layout = (LinearLayout) infoView.findViewById(R.id.list);

        for(int i=0; i < flat.options.size(); i++){
            View item = getView(R.layout.flat_facilities_item);
            TextView textView = (TextView) item.findViewById(R.id.itemText);
            textView.setText(flat.options.get(i));

            layout.addView(item);
        }
    }
}
