package ru.hotel72.social;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.perm.kate.api.Api;
import com.streetobjects.android.twitter.Twitter;
import com.streetobjects.android.twitter.TwitterUtils;
import ru.hotel72.R;
import ru.hotel72.social.facebook.FacebookConnector;
import ru.hotel72.social.facebook.SessionEvents;
import ru.hotel72.social.vkontakte.Account;
import ru.hotel72.social.vkontakte.LoginActivity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class ShareDialog implements View.OnClickListener {

    private Context mContext;
    private String mHotelUrl;
    private AlertDialog mAlertDialog;
    private FacebookConnector facebookConnector;
    private static final String FACEBOOK_PERMISSION = "publish_stream";
    private final Handler mHandler = new Handler();
    private Twitter mTwitter;

    public ShareDialog(Context context, String hotel_url) {
        mContext = context;
        mHotelUrl = hotel_url;
    }

    public void showDialog() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(mContext);
        builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.social_share, null);
        View vkBtn = view.findViewById(R.id.vk_share);
        vkBtn.setOnClickListener(this);

        View fbBtn = view.findViewById(R.id.facebook_share);
        fbBtn.setOnClickListener(this);

        View twBtn = view.findViewById(R.id.twitter_share);
        twBtn.setOnClickListener(this);

        builder.setView(view);

        mAlertDialog = builder.create();
        mAlertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.vk_share: {
                shareVk();
                break;
            }
            case R.id.facebook_share: {
                shareFb();
                break;
            }
            case R.id.twitter_share: {
                shareTwitter();
                break;
            }
        }

        mAlertDialog.dismiss();
    }

    private void shareTwitter() {
        Activity activity = (Activity) mContext;
        if (TwitterUtils.isAuthenticated(
                activity,
                ru.hotel72.social.twitter.Constants.CONSUMER_KEY,
                ru.hotel72.social.twitter.Constants.CONSUMER_SECRET)) {
            postMessageTwitter();
        } else {
            mTwitter = new Twitter(activity,
                    ru.hotel72.social.twitter.Constants.CONSUMER_KEY,
                    ru.hotel72.social.twitter.Constants.CONSUMER_SECRET);
            mTwitter.setAuthListener(new Twitter.TwitterAuthListener() {
                @Override
                public void onAuthenticated() {
                    try {
                        postMessageTwitter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mTwitter.authenticate();
        }
    }

    public void postMessageTwitter() {
        Thread t = new Thread() {
            public void run() {

                try {
                    TwitterUtils.sendTweet(
                            (Activity) mContext,
                            ru.hotel72.social.twitter.Constants.CONSUMER_KEY,
                            ru.hotel72.social.twitter.Constants.CONSUMER_SECRET,
                            mContext.getString(R.string.shareMsg) + " " + mHotelUrl);
                    mHandler.post(mUpdateNotification);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        };
        t.start();
    }

    private void shareVk() {
        Activity activity = (Activity) mContext;
        //Восстановление сохранённой сессии
        Account account = new Account();
        account.restore(mContext);

        //Если сессия есть создаём API для обращения к серверу
        Api api;
        if(account.access_token!=null){
            api=new Api(account.access_token, Constants.VK_API_ID);
            postMessageVkontakte(api, account);
        } else {
            Intent i = new Intent(activity.getApplicationContext(), LoginActivity.class);
            i.putExtra(mContext.getString(R.string.vk_msg), mContext.getString(R.string.shareMsg) + " " + mHotelUrl);
            activity.startActivity(i);
        }
    }

    private void postMessageVkontakte(final Api api, final Account account) {
        //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
        new Thread(){
            @Override
            public void run(){
                try {
                    String text= mContext.getString(R.string.shareMsg);
                    ArrayList<String> attach = new ArrayList<String>();
                    attach.add(mHotelUrl);
                    api.createWallPost(account.user_id, text, attach, null, false, false, false, null, null, null, null);
                    //Показать сообщение в UI потоке
                    mHandler.post(mUpdateNotification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void shareFb() {
        Activity activity = (Activity) mContext;
        this.facebookConnector = new FacebookConnector(Constants.FB_API_ID, activity, activity.getApplicationContext(),
                new String[] {FACEBOOK_PERMISSION});

        if (facebookConnector.getFacebook().isSessionValid()) {
            postMessageFacebook();
        } else {
            SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {

                @Override
                public void onAuthSucceed() {
                    postMessageFacebook();
                }

                @Override
                public void onAuthFail(String error) {

                }
            };
            SessionEvents.addAuthListener(listener);
            facebookConnector.login();
        }
    }

    private void postMessageFacebook() {
        Thread t = new Thread() {
            public void run() {

                try {
                    facebookConnector.postMessageOnWall(mContext.getString(R.string.shareMsg), mHotelUrl);
                    mHandler.post(mUpdateNotification);
                } catch (Exception ex) {
                    Log.e("Share via facebook", "Error sending msg", ex);
                }
            }
        };
        t.start();
    }

    final Runnable mUpdateNotification = new Runnable() {
        public void run() {
            Toast.makeText(mContext, "Опубликовано", Toast.LENGTH_LONG).show();
        }

    };
}
