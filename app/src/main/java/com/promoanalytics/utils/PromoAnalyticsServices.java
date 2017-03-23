package com.promoanalytics.utils;

import com.google.gson.GsonBuilder;
import com.promoanalytics.activity.login.RegisterUser;
import com.promoanalytics.activity.login.User;
import com.promoanalytics.model.AllDeals;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by think360 on 22/03/17.
 */

public interface PromoAnalyticsServices {


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://think360.in/bluedint/api/index.php/12345/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .build();

    @FormUrlEncoded
    @POST("login/")
    Call<User> loginUser(@Field("mobile") String name,
                         @Field("password") String password,
                         @Field("is_social") int isSocial);

    @FormUrlEncoded
    @POST("register/")
    Call<RegisterUser> registerUser(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("mobile") String mobile,
                                    @Field("password") String password,
                                    @Field("is_social") int isSocial);


    @FormUrlEncoded
    @POST("deal_all/")
    Call<AllDeals> getAllDeals(@Field("category") String category,
                               @Field("latitude") String latitude,
                               @Field("longitude") String longitude,
                               @Field("feature") String feature,
                               @Field("user_id") String user_id,
                               @Field("page") String page);

}
