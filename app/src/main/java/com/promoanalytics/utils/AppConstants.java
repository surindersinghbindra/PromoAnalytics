package com.promoanalytics.utils;

/**
 * Created by think360 on 23/03/17.
 */

public class AppConstants {

    final public static String USER_NAME = "USER_NAME";
    final public static String FIRST_NAME = "FIRST_NAME";
    final public static String LAST_NAME = "LAST_NAME";
    final public static String USER_ID = "USER_ID";
    final public static String IMAGE_URL = "IMAGE_URL";
    final public static String PHONE_NUMBER = "PHONE_NUMBER";
    final public static String EMAIL = "EMAIL";
    final public static String IS_SOCIAL = "IS_SOCIAL";
    final public static String SOCIAL_NAME = "SOCIAL_NAME";
    final public static String SOCIAL_NAME_GOOGLE = "SOCIAL_NAME_GOOGLE";
    final public static String SOCIAL_NAME_FACEBOOK = "SOCIAL_NAME_FACEBOOK";
    final public static String IS_LOGIN = "IS_LOGIN";
    final public static String IS_REMEMBER_TAPPED = "IS_REMEMBER_TAPPED";

    final public static String FEATURED_DEALS = "1";
    final public static String UNFEATURED_DEALS = "0";


    final public static int ZOOM_LEVEL_IN_APP = 17;

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.promoanalytics";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "+18443361158";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";


    public static final String LATITUDE = "LATITUDE";
    public static final String LOCATION_NAME = "LOCATION_NAME";
}
