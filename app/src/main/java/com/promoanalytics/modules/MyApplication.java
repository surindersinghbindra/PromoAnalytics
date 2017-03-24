package com.promoanalytics.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by think360user on 2/4/2016.
 */
public class MyApplication extends Application {
    public static final String TAG = ImgController.class.getSimpleName();
    public static SharedPreferences sharedPreferencesCompat;
    private static Context context;
    private static MyApplication mInstance;
    String city, category, member, valueforbecome;
    String usersid;
    String nextpage = "1";
    RequestQueue queue;
    private String valuetstsngle;

    public static SharedPreferences.Editor getSharedPrefEditor() {

        return sharedPreferencesCompat.edit();
    }
    // String checkOrigin = utils.getNextpage();

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getValueforbecome() {
        return valueforbecome;
    }

    public void setValueforbecome(String valueforbecome) {
        this.valueforbecome = valueforbecome;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNextpage() {
        return nextpage;
    }

    public void setNextpage(String nextpage) {
        this.nextpage = nextpage;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        sharedPreferencesCompat = getSharedPreferences("APP_PREF", MODE_PRIVATE);
        queue = Volley.newRequestQueue(getApplicationContext());
        mInstance = this;


    }

    public RequestQueue getRequestQueue() {
        return queue;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }


    public String getUsersid() {
        return usersid;
    }

    public void setUsersid(String usersid) {
        this.usersid = usersid;
    }
}