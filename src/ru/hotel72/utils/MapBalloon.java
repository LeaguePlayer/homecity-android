package ru.hotel72.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import ru.hotel72.R;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;

import javax.xml.transform.sax.SAXResult;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 1:10
 * To change this template use File | Settings | File Templates.
 */
public class MapBalloon extends BalloonItem {
    private Context mContext;
    private TextView address;
    private TextView count;

    public MapBalloon(Context context, GeoPoint geoPoint) {
        super(context, geoPoint);
        mContext = context;
    }

    @Override
    public int compareTo(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void inflateView(Context context){
        LayoutInflater inflater = LayoutInflater.from( context );
        model = (ViewGroup)inflater.inflate(R.layout.map_balloon, null);

        address = (TextView) model.findViewById(R.id.info).findViewById(R.id.address);
        count = (TextView) model.findViewById(R.id.info).findViewById(R.id.count);
    }

    public void setAddress(CharSequence address){
        this.address.setText(address);
    }

    public void setCount(Integer count){
        String result = "Найден%s %d квартир%s";
        String value = count.toString();
        char last = value.charAt(value.length() - 1);
        switch (last){
            case '1': {
                result = String.format(result, "а", count, "а");
                break;
            }
            case '2':
            case '3':
            case '4': {
                result = String.format(result, "о", count, "ы");
                break;
            }
            default:{
                result = String.format(result, "о", count, "");
                break;
            }
        }
        this.count.setText(result);
    }

}
