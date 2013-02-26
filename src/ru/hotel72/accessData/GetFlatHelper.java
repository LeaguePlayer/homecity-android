package ru.hotel72.accessData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.hotel72.domains.Flat;
import ru.hotel72.domains.Photo;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 17.02.13
 * Time: 19:26
 * To change this template use File | Settings | File Templates.
 */
public class GetFlatHelper {
    public static ArrayList<Integer> likedFlat;

    public static ArrayList<String> parseFlatOptions(JSONArray jsonArray) {
        ArrayList<String> result = new ArrayList<String>();

        for(int i=0; i < jsonArray.length(); i++){
            try {
                result.add(jsonArray.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static double[] parseFlatCoors(JSONArray jsonArray) throws JSONException {
        double[] coord = new double[2];

        coord[0] = Double.parseDouble(jsonArray.get(0).toString());
        coord[1] = Double.parseDouble(jsonArray.get(1).toString());

        return coord;
    }

    public static ArrayList<Photo> parseFlatPhotos(JSONObject photosJson) throws JSONException {
        ArrayList<Photo> photos = new ArrayList<Photo>();

        JSONArray names = photosJson.names();
        for (int i = 0; i < names.length(); i++){
            String name = names.get(i).toString();
            int id = Integer.parseInt(name);
            photos.add(parsePhoto(id, photosJson.getJSONObject(name)));
        }

        return photos;
    }

    public static Photo parsePhoto(int id, JSONObject jsonObject) throws JSONException {
        Photo photo = new Photo();

        photo.id = id;
        photo.data_sort = jsonObject.getInt(String.valueOf(PhotoJsonNames.data_sort));
        photo.type_photo = jsonObject.getInt(String.valueOf(PhotoJsonNames.type_photo));
        photo.url = jsonObject.getString(String.valueOf(PhotoJsonNames.url));

        return photo;
    }

    public static Flat parseMainFlatData(JSONObject flatJson) throws JSONException {
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
