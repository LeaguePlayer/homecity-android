package com.streetobjects.android.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import android.app.Activity;
import twitter4j.http.AccessToken;

public class TwitterUtils {

	public static boolean isAuthenticated(Activity context, String apiKey, String secretKey) {
		
		OAuthDTO oauth = new OAuthDTO();
		SessionStore.restoreTwitterOAuth(oauth, context);
		
		String token = oauth.getToken();
		String secret = oauth.getTokenSecret();
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(apiKey, secretKey);
		twitter.setOAuthAccessToken(a);
		
		try {
			twitter.getAccountSettings();
			return true;
		} catch (RuntimeException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void sendTweet(Activity context, String apiKey, String secretKey, String msg) throws Exception {
		OAuthDTO oauth = new OAuthDTO();
		SessionStore.restoreTwitterOAuth(oauth, context);
		
		String token = oauth.getToken();
		String secret = oauth.getTokenSecret();
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(apiKey, secretKey);
		twitter.setOAuthAccessToken(a);
        twitter.updateStatus(msg);
	}	
}
