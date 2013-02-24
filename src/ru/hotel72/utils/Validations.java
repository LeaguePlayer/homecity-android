package ru.hotel72.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Evgeny
 * Date: 24.02.13
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class Validations {
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
