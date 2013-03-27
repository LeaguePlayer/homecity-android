package ru.hotel72.social.vkontakte;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */
public class Account {
    public String access_token;
    public long user_id;
    private static final String KEY = "vkontakte-session";

    public void save(Context context){
        SharedPreferences.Editor editor =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString("access_token", access_token);
        editor.putLong("user_id", user_id);
        editor.commit();
    }

    public void restore(Context context){
        SharedPreferences savedSession =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        access_token=savedSession.getString("access_token", null);
        user_id=savedSession.getLong("user_id", 0);
    }
}
