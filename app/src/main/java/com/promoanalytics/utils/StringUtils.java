package com.promoanalytics.utils;

/**
 * Created by think360 on 04/04/17.
 */

public class StringUtils {


    public static String formatToPecentage(String sentence) {
        String result = "";
        if (sentence != null) {
            result = String.format("%s%s\nOFF", sentence, "%");
        } else {
            return result;
        }
        return result;
    }

    public static String formatAddress(String address) {
        String result = "";
        if (address != null) {

            // result = String.format("%s%s off", sentence, "%");
            result = String.format("Address: %s", address);
        } else {
            return result;
        }
        return result;
    }
}
