package ru.hotel72.social.facebook;

import android.os.Bundle;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class BaseDialogListener implements Facebook.DialogListener {

    @Override
    public void onComplete(Bundle values) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onFacebookError(FacebookError e) {
        e.printStackTrace();
    }

    public void onError(DialogError e) {
        e.printStackTrace();
    }

    public void onCancel() {
    }
}
