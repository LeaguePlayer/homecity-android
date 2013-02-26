package ru.hotel72.utils;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import ru.hotel72.accessData.GetFlatsTask;
import ru.hotel72.activities.FlatListActivity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.01.13
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {
    private Context context;
    private int visibleThreshold = 5;
    private int currentPage = 1;
    private int previousTotal = 1;
    private boolean loading = false;
    private ArrayList<Integer> ids;

    public int getPage(){
        return currentPage;
    }

    public int getPreviousTotal(){
        return previousTotal;
    }

    public EndlessScrollListener(Context context) {
        this.context = context;
    }

    public EndlessScrollListener(Context context, int visibleThreshold, int currentPage, int previousTotal) {
        this.context = context;
        this.visibleThreshold = visibleThreshold;
        this.currentPage = currentPage;
        this.previousTotal = previousTotal;
    }

    public void setHotelIds(ArrayList<Integer> ids){
        this.ids = ids;
    }

    public void updateContext(Context context){
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && firstVisibleItem + visibleItemCount >= totalItemCount) {
            new GetFlatsTask(context, visibleThreshold, ids).execute(currentPage);
            loading = true;
        }
    }
}
