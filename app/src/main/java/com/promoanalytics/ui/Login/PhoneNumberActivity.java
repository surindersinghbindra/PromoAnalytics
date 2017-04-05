package com.promoanalytics.ui.Login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.promoanalytics.R;
import com.promoanalytics.databinding.ActivityPhoneNumberBinding;
import com.promoanalytics.ui.MainActivityAfterLogin;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.BaseAppCompatActivity;
import com.promoanalytics.utils.PromoAnalyticsServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumberActivity extends BaseAppCompatActivity {

    private ActivityPhoneNumberBinding phoneNumberActivityBinding;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            intent = getIntent();

        }
        phoneNumberActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_phone_number, null);

        phoneNumberActivityBinding.btnSignProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if (TextUtils.isEmpty(phoneNumberActivityBinding.etMobileNumber.getText())) {
                    showMessageInSnackBar(phoneNumberActivityBinding.coordinatorLayout, "Please provide mobile number");

                } else {

                    showProgressBar();
                    PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                    Call<RegisterUser> registerUserCallback = promoAnalyticsServices.registerUserWithSocial(intent.getStringExtra(AppConstants.USER_NAME), intent.getStringExtra(AppConstants.EMAIL), phoneNumberActivityBinding.etMobileNumber.getText().toString().trim(), 1);
                    registerUserCallback.enqueue(new Callback<RegisterUser>() {
                        @Override
                        public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {

                            if (response.isSuccessful()) {
                                pDialog.hide();
                                if (response.body().getStatus()) {
                                    AppController.sharedPreferencesCompat.edit().putString(AppConstants.USER_NAME, intent.getStringExtra(AppConstants.USER_NAME)).apply();
                                    AppController.sharedPreferencesCompat.edit().putString(AppConstants.EMAIL, intent.getStringExtra(AppConstants.EMAIL)).apply();
                                    AppController.sharedPreferencesCompat.edit().putString(AppConstants.USER_ID, response.body().getData().getUserId() + "").apply();
                                    AppController.sharedPreferencesCompat.edit().putBoolean(AppConstants.IS_SOCIAL, true).apply();
                                    AppController.sharedPreferencesCompat.edit().putString(AppConstants.PHONE_NUMBER, phoneNumberActivityBinding.etMobileNumber.getText().toString().trim()).apply();
                                    AppController.sharedPreferencesCompat.edit().putString(AppConstants.IMAGE_URL, intent.getStringExtra(AppConstants.IMAGE_URL)).apply();

                                    startActivity(new Intent(PhoneNumberActivity.this, MainActivityAfterLogin.class));
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                } else {

                                    showMessageInSnackBar(phoneNumberActivityBinding.coordinatorLayout, response.body().getMessage());

                                }

                            } else {
                                pDialog.hide();
                                showDialog(response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterUser> call, Throwable t) {
                            showMessageInSnackBar(phoneNumberActivityBinding.coordinatorLayout, t.getMessage());
                        }
                    });
                }
            }
        });


    }


}
