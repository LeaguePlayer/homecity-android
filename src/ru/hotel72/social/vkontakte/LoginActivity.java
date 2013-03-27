package ru.hotel72.social.vkontakte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.perm.kate.api.Api;
import com.perm.kate.api.Auth;
import ru.hotel72.R;
import ru.hotel72.social.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 23:42
 * To change this template use File | Settings | File Templates.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "Kate.LoginActivity";

    WebView webview;
    private String mMessage;
    private final Handler mHandler = new Handler();
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mContext = this;

        Intent i = getIntent();
        mMessage = i.getStringExtra(getString(R.string.vk_msg));

        webview = (WebView) findViewById(R.id.vkontakteview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.clearCache(true);

        //Чтобы получать уведомления об окончании загрузки страницы
        webview.setWebViewClient(new VkontakteWebViewClient());

        //otherwise CookieManager will fall with java.lang.IllegalStateException: CookieSyncManager::createInstance() needs to be called before CookieSyncManager::getInstance()
        CookieSyncManager.createInstance(this);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        String url=Auth.getUrl(Constants.VK_API_ID, Auth.getSettings());
        webview.loadUrl(url);
    }

    class VkontakteWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            parseUrl(url);
        }
    }

    private void parseUrl(String url) {
        try {
            if(url==null)
                return;
            Log.i(TAG, "url=" + url);
            if(url.startsWith(Auth.redirect_url))
            {
                if(!url.contains("error=")){
                    Account account = new Account();
                    String[] auth=Auth.parseRedirectUrl(url);
                    account.access_token = auth[0];
                    account.user_id = Long.parseLong(auth[1]);
                    account.save(this);

                    Api api = new Api(account.access_token, Constants.VK_API_ID);
                    postMessageVkontakte(api, account);
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postMessageVkontakte(final Api api, final Account account) {
        //Общение с сервером в отдельном потоке чтобы не блокировать UI поток
        new Thread(){
            @Override
            public void run(){
                try {
                    api.createWallPost(account.user_id, mMessage, null, null, false, false, false, null, null, null, null);
                    //Показать сообщение в UI потоке
                    mHandler.post(mUpdateNotification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    final Runnable mUpdateNotification = new Runnable() {
        public void run() {
            Toast.makeText(mContext, "Опубликовано", Toast.LENGTH_LONG).show();
        }

    };
}
