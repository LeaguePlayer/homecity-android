package ru.hotel72.accessData;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.hotel72.activities.FlatListActivity;
import ru.hotel72.domains.Flat;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 20.01.13
 * Time: 22:46
 * To change this template use File | Settings | File Templates.
 */

public class GetFlatsTask extends AsyncTask<Integer, Void, Void> {

    private Context context;
    private int visibleItemCount;
    private static String getFlatsUrl = "http://hotel72.ru/index.php/api/getJsonHotels?%spage=%d&rows_count=%d";

    private ArrayList<Flat> flats = new ArrayList<Flat>();
    private String idsParameter = "";

    public GetFlatsTask(Context context, int visibleItemCount, ArrayList<Integer> ids){

        this.context = context;
        this.visibleItemCount = visibleItemCount;
        if (ids != null && ids.size() > 0) {
            idsParameter += "id=" + ids.get(0).toString();
            for (int i=1; i < ids.size(); i++){
                idsParameter += "," + ids.get(i).toString();
            }
            idsParameter += "&";
        }
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        tryGetFlats(integers[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FlatListActivity activity = (FlatListActivity) context;
        activity.UpdateFlatsList(flats);
    }

    private Boolean tryGetFlats(Integer pageNumber) {
        String url = String.format(getFlatsUrl, idsParameter, pageNumber, visibleItemCount);
        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.getJSONFromUrl(url);

        if(object == null)
            return false;

        try {
            JSONObject jsonObject = (JSONObject) object;

            JSONArray names = jsonObject.names();

            for (int i = 0; i < names.length(); i++){
                try {
                    String name = names.get(i).toString();
                    JSONObject flatJson = jsonObject.getJSONObject(name);

                    Flat flat = GetFlatHelper.parseMainFlatData(flatJson);
                    flat.photos = GetFlatHelper.parseFlatPhotos(flatJson.getJSONObject(String.valueOf(FlatJsonNames.photos)));
                    flat.coords = GetFlatHelper.parseCoords(flatJson.getJSONArray(String.valueOf(FlatJsonNames.coords)));
                    flat.options = GetFlatHelper.parseFlatOptions(flatJson.getJSONArray(String.valueOf(FlatJsonNames.options)));

                    flat.isLiked = GetFlatHelper.likedFlat.contains(flat.id);

                    flats.add(flat);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
}

