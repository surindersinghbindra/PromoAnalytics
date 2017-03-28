package com.promoanalytics.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by think360 on 28/03/17.
 */

public class AppController extends Application {

    public static SharedPreferences sharedPreferencesCompat;

    public static Context context;

    public static Context getAppContext() {
        return context;
    }

    public static SharedPreferences.Editor getSharedPrefEditor() {
        return sharedPreferencesCompat.edit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPreferencesCompat = getSharedPreferences("APP_PREF", MODE_PRIVATE);
    }
}
