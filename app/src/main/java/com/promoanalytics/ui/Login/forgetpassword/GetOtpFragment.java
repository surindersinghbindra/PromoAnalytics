package com.promoanalytics.ui.Login.forgetpassword;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.promoanalytics.R;
import com.promoanalytics.databinding.FragmentGetOtpBinding;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GetOtpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GetOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetOtpFragment extends RootFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private FragmentGetOtpBinding fragmentGetOtpBinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GetOtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetOtpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetOtpFragment newInstance(String param1, String param2) {
        GetOtpFragment fragment = new GetOtpFragment();
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
        // Inflate the layout for this fragment

        fragmentGetOtpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_otp, container, false);

        fragmentGetOtpBinding.btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(fragmentGetOtpBinding.etMobileNumber.getText().toString())) {

                    showDialog("Please provide your registred email id or  mobile number");

                } else {
                    showProgressBarWithMessage("Please wait!\n while we are sending OTP");
                    PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                    Call<OtpModel> optModelCall = promoAnalyticsServices.getOtp(fragmentGetOtpBinding.etMobileNumber.getText().toString().trim());
                    optModelCall.enqueue(new Callback<OtpModel>() {
                        @Override
                        public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                            pDialog.hide();
                            if (response.body().getStatus()) {
                                showMessageInSnackBar(response.body().getMessage());
                                trasactFragment(R.id.container, VerifyOtpFragment.newInstance(response.body().getData().getUserId(), ""));

                            } else {
                                showMessageInSnackBar(response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<OtpModel> call, Throwable t) {

                        }
                    });

                }
            }
        });


        return fragmentGetOtpBinding.getRoot();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
