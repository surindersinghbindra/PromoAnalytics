package com.promoanalytics.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.promoanalytics.R;
import com.promoanalytics.databinding.FragmentCouponDetailsBinding;
import com.promoanalytics.model.DealDetail.DetalDetail;
import com.promoanalytics.model.SaveDealModel;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.BaseAppCompatActivity;
import com.promoanalytics.utils.BusProvider;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.UtilHelper;
import com.squareup.otto.Produce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponDetailActivity extends BaseAppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String id = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DetalDetail.Data data;
    private FragmentCouponDetailsBinding fragmentCouponDetailsBinding;
    private int isFavLocal = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent() != null) {
            mParam1 = getIntent().getStringExtra(ARG_PARAM1);

        }

        fragmentCouponDetailsBinding = DataBindingUtil.setContentView(CouponDetailActivity.this, R.layout.fragment_coupon_details);

        fragmentCouponDetailsBinding.toolbar.setNavigationIcon(R.drawable.ic_back);
        fragmentCouponDetailsBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fragmentCouponDetailsBinding.toolbar.setTitleTextColor(ContextCompat.getColor(CouponDetailActivity.this, android.R.color.white));
      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(fragmentCouponDetailsBinding.toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        fragmentCouponDetailsBinding.progressbarDetail.setVisibility(View.VISIBLE);
        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
        Call<DetalDetail> detalDetailCall = promoAnalyticsServices.getDealDetail(mParam1, AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, ""));
        detalDetailCall.enqueue(new Callback<DetalDetail>() {
            @Override
            public void onResponse(Call<DetalDetail> call, Response<DetalDetail> response) {

                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        fragmentCouponDetailsBinding.progressbarDetail.setVisibility(View.GONE);
                        fragmentCouponDetailsBinding.toolbar.setTitle(response.body().getData().getName());
                        isFavLocal = response.body().getData().getIsFav() == 0 ? 1 : 0;
                        data = response.body().getData();
                        fragmentCouponDetailsBinding.setDetail(data);
                        fragmentCouponDetailsBinding.addresses.setText(Html.fromHtml("<b> Address:</b> " + data.getAddress()));

                        SpannableString styledString = new SpannableString("Valid from " + response.body().getData().getValid());

                        styledString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(CouponDetailActivity.this, R.color.appOrange)), 11, styledString.length(), 0);
                        fragmentCouponDetailsBinding.validity.setText(styledString);
                    } else {
                        showMessageInSnackBar(fragmentCouponDetailsBinding.flMain, response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetalDetail> call, Throwable t) {
                showMessageInSnackBar(fragmentCouponDetailsBinding.flMain, t.getMessage());
            }
        });

        fragmentCouponDetailsBinding.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeFavStatus(isFavLocal);
            }
        });

    }


    private void changeFavStatus(int temp) {


        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
        Call<SaveDealModel> saveDealModelCall = promoAnalyticsServices.saveToFavourite(AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, ""), mParam1, temp);
        saveDealModelCall.enqueue(new Callback<SaveDealModel>() {
            @Override
            public void onResponse(Call<SaveDealModel> call, Response<SaveDealModel> response) {
                if (response.body().getStatus()) {

                    if (response.body().getMessage().contains("Remove")) {
                        data.setIsFav(0);
                        isFavLocal = 1;
                        id = data.getId();
                        BusProvider.getInstance().post(addToFav());
                        UtilHelper.animateOverShoot(fragmentCouponDetailsBinding.ivHeart);
                        showMessageInSnackBar(fragmentCouponDetailsBinding.flMain, response.body().getMessage());
                    }
                    if (response.body().getMessage().contains("Add")) {
                        data.setIsFav(1);
                        isFavLocal = 0;
                        id = data.getId();
                        BusProvider.getInstance().post(addToFav());
                        UtilHelper.animateOverShoot(fragmentCouponDetailsBinding.ivHeart);
                        showMessageInSnackBar(fragmentCouponDetailsBinding.flMain, response.body().getMessage());
                    }

                } else {
                    showMessageInSnackBar(fragmentCouponDetailsBinding.flMain, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SaveDealModel> call, Throwable t) {
                showMessageInSnackBar(fragmentCouponDetailsBinding.flMain, t.getMessage());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.from_right_to_left);
    }

    @Produce
    public AddToFavFromDetail addToFav() {

        return new AddToFavFromDetail(id);
    }

}
