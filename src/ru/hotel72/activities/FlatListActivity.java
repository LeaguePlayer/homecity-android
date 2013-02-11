package ru.hotel72.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import ru.hotel72.R;
import ru.hotel72.accessData.GetFlatsTask;
import ru.hotel72.activities.adapters.FlatsListAdapter;
import ru.hotel72.domains.Flat;
import ru.hotel72.utils.EndlessScrollListener;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.01.13
 * Time: 2:42
 * To change this template use File | Settings | File Templates.
 */
public class FlatListActivity extends BaseActivity {
    private FlatsListAdapter flatsListAdapter;
    private ListView flatList;
    private static ArrayList<Flat> flats = new ArrayList<Flat>();

    private static int visibleThreshold = 5;
    private static View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Object obj = getLastNonConfigurationInstance();
        if (null != obj) {
            flatsListAdapter = (FlatsListAdapter) obj;
        } else {
            flatsListAdapter = new FlatsListAdapter(this, R.layout.flat_list_item, flats);
        }

        setContentView(R.layout.flat_list);
        flatList = (ListView)findViewById(R.id.flatList);

        footer = getLayoutInflater().inflate(R.layout.endless_list_footer, null);
        flatList.addFooterView(footer);
        flatList.setAdapter(flatsListAdapter);
        flatList.setOnScrollListener(new EndlessScrollListener(this, visibleThreshold));
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return flatsListAdapter;
    }

    public void UpdateFlatsList(ArrayList<Flat> newFlats) {
//        final int prevSize = this.flats.size();

        if (newFlats.size() < this.visibleThreshold) {
            flatList.removeFooterView(footer);
        }

        for (Flat flat : newFlats) {
            this.flats.add(flat);
        }

        flatsListAdapter.notifyDataSetChanged();

//        for (Flat flat : newFlats) {
//            flatsListAdapter.add(flat);
//        }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = prevSize, j = flats.size(); i < j; i++) {
//                    View view = flatList.getChildAt(i);
//                    flatList.getAdapter().getView(i, view, flatList);
//                }
//            }
//        });

    }
}
