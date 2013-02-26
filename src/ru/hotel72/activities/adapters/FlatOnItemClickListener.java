package ru.hotel72.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import ru.hotel72.activities.FlatActivity;
import ru.hotel72.activities.FlatListActivity;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.DataTransfer;

public class FlatOnItemClickListener implements AdapterView.OnItemClickListener {
    private Context context;
    private Flat flat;

    public FlatOnItemClickListener(Context context, Flat flat){
        this.context = context;
        this.flat = flat;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, FlatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra("flatId", flat.id);
        DataTransfer.put(flat.id.toString(), flat);
        context.startActivity(intent);
    }

}
