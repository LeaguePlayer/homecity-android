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
import ru.hotel72.activities.BoundActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 23.02.13
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class BoundTask extends AsyncTask<Void, Void, String> {
    private final static String BOUND_URL = "http://dev.admin-hotel.ru/site/registerDeviceWithPhone";

    private HashMap<String, String> mData = new HashMap<String, String>();
    private String responseMsg;
    private Context context;
    private final ProgressDialog progressDialog;
    private boolean mIsOK;
    private InputStream is;
    private String mOkToastMsg;

    public BoundTask(String phone, String device, String name, String email, Context context, boolean isUserBounded) {
        this.context = context;
        mData.put(context.getString(R.string.devicetypeKey), context.getString(R.string.deviceTypeDefaultValue));
        mData.put(context.getString(R.string.passwordKey), context.getString(R.string.passwordDefaultValue));
        mData.put("phone", phone);
        mData.put("device", device);
        mData.put("name", name);
        mData.put("email", email);
        progressDialog = new ProgressDialog(context);

        if(isUserBounded) {
            progressDialog.setMessage(context.getString(R.string.bound_updating));
            mOkToastMsg = context.getString(R.string.data_updated);
        } else {
            progressDialog.setMessage(context.getString(R.string.bounding));
            mOkToastMsg = context.getString(R.string.bounded);
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
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
            is = httpEntity.getContent();

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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();

        String toastMsg = mIsOK ? mOkToastMsg : responseMsg;
        Toast.makeText(context, toastMsg, 0).show();

        if(mIsOK){
            ((BoundActivity)context).dataBounded(mData);
        }
    }
}
