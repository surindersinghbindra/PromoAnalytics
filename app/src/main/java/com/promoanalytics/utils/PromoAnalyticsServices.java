package com.promoanalytics.utils;

import com.google.gson.GsonBuilder;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.model.Category.CategoryModel;
import com.promoanalytics.model.SaveDealModel;
import com.promoanalytics.ui.login.RegisterUser;
import com.promoanalytics.ui.login.User;
import com.promoanalytics.ui.login.forgetpassword.OtpModel;

import okhttp3.MultipartBody;
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
    @POST("user_profile/")
    Call<RegisterUser> getProfile(@Field("user_id") String userid);


    @FormUrlEncoded
    @POST("register/")
    Call<RegisterUser> registerUserWithSocial(@Field("name") String name,
                                              @Field("email") String email,
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


  /*  @Multipart
    @POST("edit_profile/")
    Call<User> editUserWithoutPassword(@Part("name") RequestBody name,
                        @Part("user_id") RequestBody userid,
                        @Part("image") RequestBody file);*/

    @Multipart
    @POST("edit_profile/")
    Call<User> editUserProfile(@Part("user_id") RequestBody userid,
                               @Part("name") RequestBody name,
                               @Part("password") RequestBody password,
                               @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("user_fav/")
    Call<SaveDealModel> saveToFavourite(@Field("user_id") String userid,
                                        @Field("deal_id") String deal_id,
                                        @Field("status") int status);


    @FormUrlEncoded
    @POST("fav_list/")
    Call<AllDeals> getSavedCoupons(@Field("user_id") String userid);


    @POST("deal_category/")
    Call<CategoryModel> getCategories();


    @FormUrlEncoded
    @POST("deal_all_map/")
    Call<AllDeals> getAllDealsOnMap(@Field("category") String category,
                                    @Field("latitude") String latitude,
                                    @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("forget_password/")
    Call<OtpModel> getOtp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("resend_otp/")
    Call<OtpModel> resendOtp(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("forget_password/")
    Call<OtpModel> verifyOtp(@Field("otp") String otp, @Field("user_id") String user_id);


}
