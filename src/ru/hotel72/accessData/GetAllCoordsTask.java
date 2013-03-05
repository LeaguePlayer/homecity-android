package ru.hotel72.accessData;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.hotel72.activities.FlatListActivity;
import ru.hotel72.activities.FlatMapActivity;
import ru.hotel72.domains.MapElement;
import ru.yandex.yandexmapkit.MapActivity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.02.13
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
public class GetAllCoordsTask extends AsyncTask<Void, Void, Void> {

    private static String url = "http://hotel72.ru/index.php/api/GetCoordHotels";
    private ArrayList<MapElement> mapElements = new ArrayList<MapElement>();
    private Context context;

    public GetAllCoordsTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.getJSONFromUrl(url);

        if(object == null)
            return null;

        JSONObject jsonObject = (JSONObject) object;

        JSONArray names = jsonObject.names();
        for (int i = 0; i < names.length(); i++) {

            MapElement element = new MapElement();

            try {
                String name = names.get(i).toString();
                JSONObject json = jsonObject.getJSONObject(name);

                element.address = name;
                element.count = json.getInt(String.valueOf(MapElementJsonNames.count));
                element.coords = parseCoords(json.getJSONArray(String.valueOf(MapElementJsonNames.coords)));
                element.hotels = parseHotels(json.getJSONArray(String.valueOf(MapElementJsonNames.hotels)));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mapElements.add(element);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        FlatMapActivity activity = (FlatMapActivity) context;
        activity.showHotels(mapElements);
    }

    private ArrayList<Integer> parseHotels(JSONArray jsonArray) throws JSONException {
        ArrayList<Integer> hotels = new ArrayList<Integer>();

        for (int i=0; i<jsonArray.length(); i++){
            hotels.add(jsonArray.getInt(i));
        }

        return hotels;
    }

    private double[] parseCoords(JSONArray jsonArray) throws JSONException {
        double[] coord = new double[2];

        coord[0] = jsonArray.getJSONObject(0).getDouble("coord1");
        coord[1] = jsonArray.getJSONObject(0).getDouble("coord2");

        return coord;
    }
}

