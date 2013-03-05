package ru.hotel72.accessData;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.hotel72.activities.FlatActivity;
import ru.hotel72.domains.Flat;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 27.02.13
 * Time: 1:36
 * To change this template use File | Settings | File Templates.
 */
public class GetFlatTask extends AsyncTask<Integer, Void, Void> {

    private Context context;
    private static String getFlatsUrl = "http://hotel72.ru/index.php/api/getJsonHotels?id=%d";
    private Flat flat;

    public GetFlatTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        String url = String.format(getFlatsUrl, integers[0]);
        JSONParser jsonParser = new JSONParser();

        Object object = jsonParser.getJSONFromUrl(url);

        if(object == null)
            return null;

        try {
            JSONObject jsonObject = (JSONObject) object;

            JSONArray names = jsonObject.names();

            for (int i = 0; i < names.length(); i++){
                try {
                    String name = names.get(i).toString();
                    JSONObject flatJson = jsonObject.getJSONObject(name);

                    flat = GetFlatHelper.parseMainFlatData(flatJson);
                    flat.photos = GetFlatHelper.parseFlatPhotos(flatJson.getJSONObject(String.valueOf(FlatJsonNames.photos)));
                    flat.coords = GetFlatHelper.parseCoords(flatJson.getJSONArray(String.valueOf(FlatJsonNames.coords)));
                    flat.options = GetFlatHelper.parseFlatOptions(flatJson.getJSONArray(String.valueOf(FlatJsonNames.options)));

                    flat.isLiked = GetFlatHelper.likedFlat.contains(flat.id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FlatActivity activity = (FlatActivity) context;
        activity.updateContent(flat);
    }
}
