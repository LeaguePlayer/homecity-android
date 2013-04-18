package ru.hotel72.accessData;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import ru.hotel72.R;
import ru.hotel72.activities.BookingActivity;
import ru.hotel72.activities.BookingUserActivity;
import ru.hotel72.activities.BoundActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 09.03.13
 * Time: 23:02
 * To change this template use File | Settings | File Templates.
 */
public class BookingTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String responseMsg;

    private final static String BOUND_URL = "http://72.admin-hotel.ru/site/registerMobileApplication";
    private HashMap<String, String> mData = new HashMap<String, String>();
    private boolean mIsOK;
    private final ProgressDialog progressDialog;

    public BookingTask(
            Context context,
            String name, String phone, String date_stay_begin, String date_stay_finish, int guests, int id_hotel) {

        mData.put(context.getString(R.string.devicetypeKey), context.getString(R.string.deviceTypeDefaultValue));
        mData.put(context.getString(R.string.passwordKey), context.getString(R.string.passwordDefaultValue));
        mData.put("phone", phone);
        mData.put("name", name);
        mData.put(context.getString(R.string.id_hotel), String.valueOf(id_hotel));
        mData.put(context.getString(R.string.date_stay_begin), date_stay_begin);
        mData.put(context.getString(R.string.date_stay_finish), date_stay_finish);
        mData.put(context.getString(R.string.guests), String.valueOf(guests));
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Бронирование...");

        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();

        if(mIsOK){
            ((BookingUserActivity)context).booked();
        }
        else {
            ((BookingUserActivity)context).bookedError(responseMsg);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {

        byte[] result = null;
        responseMsg = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(BOUND_URL);
        try {
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            mIsOK = statusLine.getStatusCode() == HttpURLConnection.HTTP_OK;

            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                is.close();
                responseMsg = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
