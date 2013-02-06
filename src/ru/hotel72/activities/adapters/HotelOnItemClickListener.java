package ru.hotel72.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import ru.hotel72.activities.StartActivity;

public class HotelOnItemClickListener implements AdapterView.OnItemClickListener {
    private Context context;

    public HotelOnItemClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, StartActivity.class);
//        intent.putExtra("id", id);
        context.startActivity(intent);
    }

}
