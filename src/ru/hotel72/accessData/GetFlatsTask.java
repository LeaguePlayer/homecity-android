package ru.hotel72.accessData;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.hotel72.activities.FlatListActivity;
import ru.hotel72.domains.Flat;
import ru.hotel72.domains.Photo;

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
    private static String getFlatsUrl = "http://hotel72.ru/index.php/api/getJsonHotels?page=%d&rows_count=%d"; //TODO добавить в преверенсис

    private ArrayList<Flat> flats = new ArrayList<Flat>();

    public GetFlatsTask(Context context, int visibleItemCount){

        this.context = context;
        this.visibleItemCount = visibleItemCount;
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
        String url = String.format(getFlatsUrl, pageNumber, visibleItemCount);
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = jsonParser.getJSONFromUrl(url);

        if(jsonObject == null)
            return false;

        JSONArray names = jsonObject.names();

        for (int i = 0; i < names.length(); i++){
            try {
                String name = names.get(i).toString();
                JSONObject flatJson = jsonObject.getJSONObject(name);

                Flat flat = parseMainFlatData(flatJson);
                flat.photos = parseFlatPhotos(flatJson.getJSONObject(String.valueOf(FlatJsonNames.photos)));
                flat.coords = parseFlatCoors(flatJson.getJSONArray(String.valueOf(FlatJsonNames.coords)));

                flats.add(flat);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private double[] parseFlatCoors(JSONArray jsonArray) throws JSONException {
        double[] coord = new double[2];

        coord[0] = Double.parseDouble(jsonArray.get(0).toString());
        coord[1] = Double.parseDouble(jsonArray.get(1).toString());

        return coord;
    }

    private ArrayList<Photo> parseFlatPhotos(JSONObject photosJson) throws JSONException {
        ArrayList<Photo> photos = new ArrayList<Photo>();

        JSONArray names = photosJson.names();
        for (int i = 0; i < names.length(); i++){
            String name = names.get(i).toString();
            int id = Integer.parseInt(name);
            photos.add(parsePhoto(id, photosJson.getJSONObject(name)));
        }

        return photos;
    }

    private Photo parsePhoto(int id, JSONObject jsonObject) throws JSONException {
        Photo photo = new Photo();

        photo.id = id;
        photo.data_sort = jsonObject.getInt(String.valueOf(PhotoJsonNames.data_sort));
        photo.type_photo = jsonObject.getInt(String.valueOf(PhotoJsonNames.type_photo));
        photo.url = jsonObject.getString(String.valueOf(PhotoJsonNames.url));

        return photo;
    }

    private Flat parseMainFlatData(JSONObject flatJson) throws JSONException {
        Flat flat = new Flat();

        flat.hotel_url = flatJson.getString(String.valueOf(FlatJsonNames.hotel_url));
        flat.id = flatJson.getInt(String.valueOf(FlatJsonNames.id));
        flat.street = flatJson.getString(String.valueOf(FlatJsonNames.street));
        flat.full_desc = flatJson.getString(String.valueOf(FlatJsonNames.full_desc));
        flat.square = flatJson.getDouble(String.valueOf(FlatJsonNames.square));
        flat.post_id = flatJson.getInt(String.valueOf(FlatJsonNames.post_id));
        flat.short_desc = flatJson.getString(String.valueOf(FlatJsonNames.short_desc));
        flat.cost = flatJson.getDouble(String.valueOf(FlatJsonNames.cost));
        flat.rooms = flatJson.getInt(String.valueOf(FlatJsonNames.rooms));

        return flat;
    }
}

enum FlatJsonNames {
    photos, hotel_url, id, coords, street, full_desc, square, post_id, short_desc, cost, options, rooms
}

enum PhotoJsonNames {
    data_sort, type_photo, url
}
