package ru.hotel72.activities;

import android.content.Intent;
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
    private ArrayList<Flat> flats = new ArrayList<Flat>();

    private final int visibleThreshold = 5;
    private View footer;
    private static int currPosition;
    private static int page = 1;
    private static int prevTotal = 1;
    private EndlessScrollListener endlessScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ArrayList<Integer> ids = getIntent().getIntegerArrayListExtra(getString(R.string.flatIds));

        Object obj = getLastNonConfigurationInstance();
        if (null != obj) {
            flatsListAdapter = (FlatsListAdapter) obj;
        } else {
            flatsListAdapter = new FlatsListAdapter(this, R.layout.flat_list_item, flats);
        }

        setContentView(R.layout.flat_list);
        flatList = (ListView)findViewById(R.id.flatList);

        if(ids != null && ids.size() > 0){
            endlessScrollListener = new EndlessScrollListener(this);
            endlessScrollListener.setHotelIds(ids);
        } else {
            endlessScrollListener = new EndlessScrollListener(this, visibleThreshold, page, prevTotal);
        }

        footer = getLayoutInflater().inflate(R.layout.endless_list_footer, null);

        flatList.addFooterView(footer);
        flatList.setAdapter(flatsListAdapter);
        flatList.setOnScrollListener(endlessScrollListener);

        View view = findViewById(R.id.headerLayout).findViewById(R.id.returnBtn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlatListActivity.this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                FlatListActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return flatsListAdapter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        currPosition = flatList.getFirstVisiblePosition();
        page = endlessScrollListener.getPage();
        prevTotal = endlessScrollListener.getPreviousTotal();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flatList.setSelection(currPosition);

    }

    public void UpdateFlatsList(ArrayList<Flat> newFlats) {
        if (newFlats.size() < this.visibleThreshold) {
            flatList.removeFooterView(footer);
        }

        for (Flat flat : newFlats) {
            this.flats.add(flat);
        }

        flatsListAdapter.notifyDataSetChanged();
    }
}
