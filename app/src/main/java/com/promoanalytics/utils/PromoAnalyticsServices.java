package com.promoanalytics.utils;

import com.google.gson.GsonBuilder;
import com.promoanalytics.login.login.RegisterUser;
import com.promoanalytics.login.login.User;
import com.promoanalytics.model.AllDeals.AllDeals;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by think360 on 22/03/17.
 */

public interface PromoAnalyticsServices {


    Retrofit retrofit = new Retrofit.Builder()
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
    @POST("register/")
    Call<RegisterUser> registerUserWithSocial(@Field("name") String name,
                                              @Field("email") String email,
                                              @Field("ccode") String ccode,
                                              @Field("mobile") String mobile,
                                              @Field("is_social") int isSocial);


    @FormUrlEncoded
    @POST("deal_all/")
    Call<AllDeals> getAllDeals(@Field("category") String category,
                               @Field("latitude") String latitude,
                               @Field("longitude") String longitude,
                               @Field("feature") String feature,
                               @Field("user_id") String user_id,
                               @Field("page") String page);


    @Multipart
    @POST("edit_profile/")
    Call<User> editUser(@Part("name") RequestBody name,
                        @Part("user_id") RequestBody userid,
                        @Part("password") RequestBody password,
                        @Part("image") RequestBody file);


    @FormUrlEncoded
    @POST("fav_list/")
    Call<AllDeals> getSavedCoupons(@Field("user_id") String userid);


}
