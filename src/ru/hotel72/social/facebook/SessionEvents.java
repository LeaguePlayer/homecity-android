package ru.hotel72.social.facebook;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 25.03.13
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class SessionEvents {

    private static LinkedList<AuthListener> mAuthListeners =
            new LinkedList<AuthListener>();
    private static LinkedList<LogoutListener> mLogoutListeners =
            new LinkedList<LogoutListener>();

    /**
     * Associate the given listener with this Facebook object. The listener's
     * callback interface will be invoked when authentication events occur.
     *
     * @param listener The callback object for notifying the application when auth
     *                 events happen.
     */
    public static void addAuthListener(AuthListener listener) {
        mAuthListeners.add(listener);
    }

    /**
     * Remove the given listener from the list of those that will be notified
     * when authentication events occur.
     *
     * @param listener The callback object for notifying the application when auth
     *                 events happen.
     */
    public static void removeAuthListener(AuthListener listener) {
        mAuthListeners.remove(listener);
    }

    /**
     * Associate the given listener with this Facebook object. The listener's
     * callback interface will be invoked when logout occurs.
     *
     * @param listener The callback object for notifying the application when log out
     *                 starts and finishes.
     */
    public static void addLogoutListener(LogoutListener listener) {
        mLogoutListeners.add(listener);
    }

    /**
     * Remove the given listener from the list of those that will be notified
     * when logout occurs.
     *
     * @param listener The callback object for notifying the application when log out
     *                 starts and finishes.
     */
    public static void removeLogoutListener(LogoutListener listener) {
        mLogoutListeners.remove(listener);
    }

    public static void onLoginSuccess() {
        for (AuthListener listener : mAuthListeners) {
            listener.onAuthSucceed();
        }
    }

    public static void onLoginError(String error) {
        for (AuthListener listener : mAuthListeners) {
            listener.onAuthFail(error);
        }
    }

    public static void onLogoutBegin() {
        for (LogoutListener l : mLogoutListeners) {
            l.onLogoutBegin();
        }
    }

    public static void onLogoutFinish() {
        for (LogoutListener l : mLogoutListeners) {
            l.onLogoutFinish();
        }
    }

    /**
     * Callback interface for authorization events.
     */
    public static interface AuthListener {

        /**
         * Called when a auth flow completes successfully and a valid OAuth
         * Token was received.
         * <p/>
         * Executed by the thread that initiated the authentication.
         * <p/>
         * API requests can now be made.
         */
        public void onAuthSucceed();

        /**
         * Called when a login completes unsuccessfully with an error.
         * <p/>
         * Executed by the thread that initiated the authentication.
         */
        public void onAuthFail(String error);
    }

    /**
     * Callback interface for logout events.
     */
    public static interface LogoutListener {
        /**
         * Called when logout begins, before session is invalidated.
         * Last chance to make an API call.
         * <p/>
         * Executed by the thread that initiated the logout.
         */
        public void onLogoutBegin();

        /**
         * Called when the session information has been cleared.
         * UI should be updated to reflect logged-out state.
         * <p/>
         * Executed by the thread that initiated the logout.
         */
        public void onLogoutFinish();
    }

}
