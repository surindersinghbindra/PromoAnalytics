package com.promoanalytics.login.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.promoanalytics.R;
import com.promoanalytics.databinding.ActivityPhoneNumberBinding;
import com.promoanalytics.login.BaseAppCompatActivity;
import com.promoanalytics.login.HomeActivity;
import com.promoanalytics.modules.MyApplication;
import com.promoanalytics.utils.AppConstants;
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


        if (TextUtils.isEmpty(phoneNumberActivityBinding.etMobileNumber.getText())) {
            showMessageInSnackBar("Please provide mobile number");
        } else {


            PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
            Call<RegisterUser> registerUserCallback = promoAnalyticsServices.registerUserWithSocial(intent.getStringExtra(AppConstants.USER_NAME), intent.getStringExtra(AppConstants.EMAIL), "+" + getCountryISDCode() + phoneNumberActivityBinding.etMobileNumber.getText().toString().trim(), 1);
            registerUserCallback.enqueue(new Callback<RegisterUser>() {
                @Override
                public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {

                    if (response.isSuccessful()) {

                        MyApplication.sharedPreferencesCompat.edit().putString(AppConstants.USER_NAME, intent.getStringExtra(AppConstants.USER_NAME)).apply();
                        MyApplication.sharedPreferencesCompat.edit().putString(AppConstants.EMAIL, intent.getStringExtra(AppConstants.EMAIL)).apply();
                        MyApplication.sharedPreferencesCompat.edit().putString(AppConstants.USER_ID, response.body().getData().getUserId() + "").apply();
                        MyApplication.sharedPreferencesCompat.edit().putBoolean(AppConstants.IS_SOCIAL, true).apply();
                        MyApplication.sharedPreferencesCompat.edit().putString(AppConstants.PHONE_NUMBER, phoneNumberActivityBinding.etMobileNumber.getText().toString().trim()).apply();

                        startActivity(new Intent(PhoneNumberActivity.this, HomeActivity.class));

                    } else {
                        showDialog(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<RegisterUser> call, Throwable t) {
                    showMessageInSnackBar(t.getMessage());
                }
            });
        }
    }

    String getCountryISDCode() {

        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

}
