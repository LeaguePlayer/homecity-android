package ru.hotel72.accessData;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import ru.hotel72.R;
import ru.hotel72.activities.FlatMapActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 04.03.13
 * Time: 0:03
 * To change this template use File | Settings | File Templates.
 */
public class SearchTask extends AsyncTask<Void, Void, Void> {

    private final String SEARCH_URL = "http://hotel72.ru/index.php/api/getCoordsFromYandexMap?text=%s&results=1&source=psearch";
    private Context context;
    private String searchText;
    private double[] coords;
    private final ProgressDialog progressDialog;

    public SearchTask(Context context, String text) {
        this.context = context;
        this.searchText = text;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.search_msg));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String query = "";
        try {
            query = URLEncoder.encode(searchText, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(SEARCH_URL, query);
        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.getJSONFromUrl(url);

        if (object == null)
            return null;

        try {
            JSONArray jsonArray = (JSONArray) object;

            if (jsonArray.length() > 0) {
                try {
                    coords = GetFlatHelper.parseCoords(jsonArray.getJSONObject(0).getJSONArray("coords"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        if (coords == null || coords.length == 0) {
            String text = "Неудалось найти " + searchText;
            Toast.makeText(context, text, 0).show();
        } else {
            ((FlatMapActivity) context).centrefyMap(coords);
        }

    }
}
