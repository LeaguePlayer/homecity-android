package ru.hotel72.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.hotel72.R;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 19.02.13
 * Time: 23:06
 * To change this template use File | Settings | File Templates.
 */
public class CitySelectionActivity extends ListActivity {

    private final String[] values = new String[] { "Тюмень" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView list, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        CitySelectionActivity.this.finish();

        SharedPreferences pref = getSharedPreferences(getString(R.string.userDataCache), MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(getString(R.string.cityName), item);
        ed.commit();

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
