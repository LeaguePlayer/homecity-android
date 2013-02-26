package ru.hotel72.accessData;

import android.os.AsyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 26.02.13
 * Time: 21:30
 * To change this template use File | Settings | File Templates.
 */
public class GetAllCoordsTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }
}

enum MapElementJsonNames {
    count, coords, hotels
}
