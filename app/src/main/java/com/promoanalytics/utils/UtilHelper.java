package com.promoanalytics.utils;

/**
 * Created by think360 on 22/03/17.
 */

public class UtilHelper {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
