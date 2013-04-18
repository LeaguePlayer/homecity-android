package ru.hotel72.accessData;

import android.content.Context;
import com.infteh.calendarview.GetUnselectableDaysTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 08.03.13
 * Time: 17:59
 * To change this template use File | Settings | File Templates.
 */
public class GetBookingDatesTask extends GetUnselectableDaysTask {

    private static String requestUrl = "http://72.admin-hotel.ru/api/getJsonOrder?id=%d&month=%d&year=%d";
    private int id;

    public GetBookingDatesTask(Context context, int id) {
        super(context);
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        JSONParser jsonParser = new JSONParser();

        try {

            String url = String.format(requestUrl, id, month, year);
            Object object = jsonParser.getJSONFromUrl(url);

            if (object == null)
                return null;

            JSONObject jsonObject = (JSONObject) object;

            try {
                JSONArray ordersDays = jsonObject.getJSONArray("orderDays");

                for (int i = 0; i < ordersDays.length(); i++) {
                    unselectableDays.add(ordersDays.getInt(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
