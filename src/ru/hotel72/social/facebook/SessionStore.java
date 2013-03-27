package ru.hotel72.social.facebook;

import android.content.Context;
import android.content.SharedPreferences;
import com.facebook.android.Facebook;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 16:21
 * To change this template use File | Settings | File Templates.
 */
public class SessionStore {
    private static final String TOKEN = "access_token";
    private static final String EXPIRES = "expires_in";
    private static final String KEY = "facebook-session";

    public static boolean save(Facebook session, Context context) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putString(TOKEN, session.getAccessToken());
        editor.putLong(EXPIRES, session.getAccessExpires());
        return editor.commit();
    }

    public static boolean restore(Facebook session, Context context) {
        SharedPreferences savedSession =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        session.setAccessToken(savedSession.getString(TOKEN, null));
        session.setAccessExpires(savedSession.getLong(EXPIRES, 0));
        return session.isSessionValid();
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
