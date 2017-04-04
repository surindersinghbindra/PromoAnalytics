package com.promoanalytics.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.promoanalytics.R;
import com.promoanalytics.databinding.FragmentCouponDetailsBinding;
import com.promoanalytics.model.DealDetail.DetalDetail;
import com.promoanalytics.model.SaveDealModel;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.BusProvider;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.promoanalytics.utils.UtilHelper;
import com.squareup.otto.Produce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CouponDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CouponDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CouponDetailFragment extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String id = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DetalDetail.Data data;
    private OnFragmentInteractionListener mListener;
    private FragmentCouponDetailsBinding fragmentCouponDetailsBinding;
    private int isFavLocal = 0;

    public CouponDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CouponDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CouponDetailFragment newInstance(String param1, String param2) {
        CouponDetailFragment fragment = new CouponDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        fragmentCouponDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_coupon_details, container, false);

        fragmentCouponDetailsBinding.toolbar.setNavigationIcon(R.drawable.ic_back);
        fragmentCouponDetailsBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityAfterLogin.self.onBackPressed();
            }
        });

        fragmentCouponDetailsBinding.toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
       /* getActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setSupportActionBar(myToolbar);


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

                        styledString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.appOrange)), 11, styledString.length(), 0);
                        fragmentCouponDetailsBinding.validity.setText(styledString);
                    } else {
                        showMessageInSnackBar(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetalDetail> call, Throwable t) {
                showMessageInSnackBar(t.getMessage());
            }
        });

        fragmentCouponDetailsBinding.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeFavStatus(isFavLocal);
            }
        });

        return fragmentCouponDetailsBinding.getRoot();
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
                        showMessageInSnackBar(response.body().getMessage());
                    }
                    if (response.body().getMessage().contains("Add")) {
                        data.setIsFav(1);
                        isFavLocal = 0;
                        id = data.getId();
                        BusProvider.getInstance().post(addToFav());
                        UtilHelper.animateOverShoot(fragmentCouponDetailsBinding.ivHeart);
                        showMessageInSnackBar(response.body().getMessage());
                    }

                } else {
                    showMessageInSnackBar(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SaveDealModel> call, Throwable t) {
                showMessageInSnackBar(t.getMessage());
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Produce
    public AddToFavFromDetail addToFav() {

        return new AddToFavFromDetail(id);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
