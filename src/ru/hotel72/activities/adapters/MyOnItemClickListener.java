package ru.hotel72.activities.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

public class MyOnItemClickListener implements AdapterView.OnItemClickListener {
    private Context context;

    public MyOnItemClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

}
