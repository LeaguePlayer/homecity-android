package com.infteh.calendarview;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 10.03.13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class GetUnselectableDaysTask extends AsyncTask<Void, Void, Void> {
    protected int month;
    protected int year;
    protected ArrayList<Integer> unselectableDays = new ArrayList<Integer>();
    private Context context;
    private ProgressDialog progressDialog;
    private OnDaysLoaded callBack;

    public GetUnselectableDaysTask(Context context) {
        this.context = context;
    }

    /**
     @month 1-12, 1st январь
     */
    public void setMonth(int month){

        this.month = month;
    }
    public void setYear(int year){

        this.year = year;
    }

    public void setCallBack(OnDaysLoaded callBack){
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Загрузка данных по брони...");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        callBack.onDaysLoaded(unselectableDays);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    public interface OnDaysLoaded {
        void onDaysLoaded (ArrayList<Integer> days);
    }
}
