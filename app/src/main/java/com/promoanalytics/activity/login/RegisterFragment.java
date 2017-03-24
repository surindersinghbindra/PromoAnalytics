package com.promoanalytics.activity.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.promoanalytics.R;
import com.promoanalytics.activity.ListDealsFragment;
import com.promoanalytics.activity.Login_Cpn;
import com.promoanalytics.databinding.FragmentRegisterBinding;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.post.RegisterCPnApi;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.promoanalytics.utils.UtilHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends RootFragment implements View.OnFocusChangeListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OnFragmentInteractionListener mListener;
    private PromoAnalyticsServices promoAnalyticsServices;
    private FragmentRegisterBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        binding.name.setOnFocusChangeListener(this);
        binding.name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.name.setHint("");
                binding.flname.setError("");

                binding.femail.setError("");
                binding.fphone.setError("");
                binding.fpassword.setError("");

            }
        });
        binding.lname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.lname.setHint("");
                binding.fname.setError("");

                binding.femail.setError("");
                binding.fphone.setError("");
                binding.fpassword.setError("");

            }
        });
        binding.email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.email.setHint("");
                binding.flname.setError("");
                binding.fname.setError("");


                binding.fphone.setError("");
                binding.fpassword.setError("");

            }
        });
        binding.phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.phone.setHint("");

                binding.flname.setError("");
                binding.fname.setError("");

                binding.femail.setError("");

                binding.fpassword.setError("");

            }
        });
        binding.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.password.setHint("");
                binding.flname.setError("");
                binding.fname.setError("");

                binding.femail.setError("");
                binding.fphone.setError("");

            }
        });

        binding.btnSignProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.name.getText()) || TextUtils.isEmpty(binding.lname.getText()) || TextUtils.isEmpty(binding.email.getText()) || UtilHelper.isValidEmail(binding.email.getText()) || TextUtils.isEmpty(binding.phone.getText()) || !binding.ivChck.isChecked()) {
                    if (TextUtils.isEmpty(binding.name.getText())) {
                        binding.name.setError("Enter Name");

                    } else if (TextUtils.isEmpty(binding.lname.getText())) {
                        binding.lname.setError("Enter last name");

                    } else if (TextUtils.isEmpty(binding.email.getText())) {
                        binding.email.setError("Enter email");

                    } else if (UtilHelper.isValidEmail(binding.email.getText())) {
                        binding.email.setError("Enter Valid Email");

                    } else if (TextUtils.isEmpty(binding.phone.getText())) {
                        binding.phone.setError("Enter phone number");
                    } else if (!binding.ivChck.isChecked()) {
                        showDialog("Please accept all the terms and conditions");
                    } else {
                        showProgressBar();
                        promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                        Call<RegisterUser> registerUserCall = promoAnalyticsServices.registerUser(binding.name.getText().toString().trim() + "" + binding.lname.getText().toString().trim(), binding.email.getText().toString().trim(), binding.phone.getText().toString().trim(), binding.password.getText().toString().trim(), 1);
                        registerUserCall.enqueue(new Callback<RegisterUser>() {
                            @Override
                            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {


                                if (response.isSuccessful()) {


                                    Call<AllDeals> getAllDealsCall = promoAnalyticsServices.getAllDeals("", "30.7360306", "76.7328649", "0", response.body().getData().getUserId() + "", "1");
                                    getAllDealsCall.enqueue(new Callback<AllDeals>() {
                                        @Override
                                        public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {
                                            if (response.body().getStatus()) {
                                                Log.d("RETRO_GETALLDEALS", response.body().getStatus() + "");
                                                Log.d("RETRO_GETALLDEALS", response.body().getData().getDetail().get(0).getCategoryPic());
                                                Intent intent = new Intent(getActivity(), ListDealsFragment.class);
                                                intent.putParcelableArrayListExtra("LIST_DEALS", (ArrayList<? extends Parcelable>) response.body().getData().getDetail());
                                                getActivity().startActivity(intent);

                                            } else {
                                                showDialog(response.body().getMessage());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<AllDeals> call, Throwable t) {

                                        }
                                    });

                                } else {
                                    showDialog(response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<RegisterUser> call, Throwable t) {

                            }
                        });

                    }

                }


                String fnames = binding.name.getText().toString().trim();
                String lnames = binding.lname.getText().toString().trim();
                String emails = binding.email.getText().toString().trim();
                String phones = binding.phone.getText().toString().trim();

                String passwords = binding.password.getText().toString().trim();
                String Expn =
                        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                if (fnames.equals("")) {
                    binding.fname.setError("Enter First name");
                    binding.flname.setError("");
                    binding.femail.setError("");
                    binding.fphone.setError("");
                    binding.fpassword.setError("");
                } else if (lnames.equals("")) {
                    binding.flname.setError("Enter Last name");
                    binding.fname.setError("");
                    binding.femail.setError("");
                    binding.fphone.setError("");
                    binding.fpassword.setError("");
                } else if (emails.equals("")) {
                    binding.femail.setError("Enter Email");
                    binding.flname.setError("");
                    binding.fname.setError("");
                    binding.fphone.setError("");
                    binding.fpassword.setError("");
                } else if (!(emails.matches(Expn) && emails.length() > 0)) {
                    binding.femail.setError("Enter valid email");

                } else if (phones.equals("")) {
                    binding.fphone.setError("Enter phone no");
                    binding.flname.setError("");
                    binding.femail.setError("");
                    binding.fname.setError("");
                    binding.fpassword.setError("");
                } else if (passwords.equals("")) {
                    binding.fpassword.setError("Enter Password");
                    binding.flname.setError("");
                    binding.femail.setError("");
                    binding.fphone.setError("");
                    binding.fname.setError("");
                } else if (binding.ivChck.getVisibility() != View.VISIBLE) {
                    binding.flname.setError("");
                    binding.femail.setError("");
                    binding.fphone.setError("");
                    binding.fname.setError("");
                    binding.fpassword.setError("");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Please accept all the terms and conditions");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {
                    String fullname = fnames + " " + lnames;
                    RegisterCPnApi registerCPnApi = new RegisterCPnApi(getActivity(), fullname, emails, phones, passwords);
                    registerCPnApi.addQueue();
                }
            }
        });

        binding.btnSignProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login_Cpn.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
         /*   case v.getId()==binding.name.getId():

                break;*/
        }

    }

    public void saveClick(View view) {
        Log.v("saveClick", "jghg");
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
