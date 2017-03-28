package com.promoanalytics.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.promoanalytics.R;
import com.promoanalytics.databinding.FragmentRegisterBinding;
import com.promoanalytics.ui.HomeActivity;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.promoanalytics.utils.UtilHelper;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

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
public class RegisterFragment extends RootFragment implements GoogleApiClient.OnConnectionFailedListener {


    private static final int RC_SIGN_IN = 9001;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private PromoAnalyticsServices promoAnalyticsServices;
    private FragmentRegisterBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Google API
    private GoogleApiClient mGoogleApiClient;

    //Facebook Login SDK
    private CallbackManager callbackManager;


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


        faceBookSDKInit();
        googleSdkInit();


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);


        binding.btnSignProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.etName.getText()) || TextUtils.isEmpty(binding.etEmail.getText()) || !UtilHelper.isValidEmail(binding.etEmail.getText()) || TextUtils.isEmpty(binding.phone.getText()) || TextUtils.isEmpty(binding.etPassword.getText()) || TextUtils.isEmpty(binding.etConfirmPassword.getText()) || !binding.etConfirmPassword.getText().toString().equals(binding.etPassword.getText().toString()) || !binding.cbAcceptTnC.isChecked()) {
                    if (TextUtils.isEmpty(binding.etName.getText())) {

                        showMessageInSnackBar("Enter Name");
                        binding.etName.requestFocus();

                    } else if (TextUtils.isEmpty(binding.etEmail.getText())) {
                        showMessageInSnackBar("Enter Email");
                        binding.etEmail.requestFocus();
                        // binding.etEmail.setError("Enter email");

                    } else if (!UtilHelper.isValidEmail(binding.etEmail.getText())) {
                        showMessageInSnackBar("Enter Valid Email");
                        binding.etEmail.requestFocus();

                    } else if (TextUtils.isEmpty(binding.phone.getText())) {

                        showMessageInSnackBar("Enter phone number");
                        binding.phone.requestFocus();

                    } else if (TextUtils.isEmpty(binding.etPassword.getText())) {

                        showMessageInSnackBar("Enter password number");
                        binding.etPassword.requestFocus();

                    } else if (TextUtils.isEmpty(binding.etConfirmPassword.getText())) {

                        showMessageInSnackBar("Enter re enter number");
                        binding.etPassword.requestFocus();


                    } else if (!binding.etConfirmPassword.getText().toString().equals(binding.etPassword.getText().toString())) {
                        showMessageInSnackBar("password doesn't match");
                        binding.etPassword.requestFocus();

                    } else {

                        showDialog("Please accept all the terms and conditions");
                        binding.cbAcceptTnC.requestFocus();

                    }
                } else {

                    showProgressBar();
                    promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                    Call<RegisterUser> registerUserCall = promoAnalyticsServices.registerUser(binding.etName.getText().toString().trim() + "", binding.etEmail.getText().toString().trim(), binding.phone.getText().toString().trim(), binding.etPassword.getText().toString().trim(), 0);
                    registerUserCall.enqueue(new Callback<RegisterUser>() {
                        @Override
                        public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {

                            if (response.isSuccessful()) {
                                pDialog.hide();

                                AppController.sharedPreferencesCompat.edit().putString(AppConstants.USER_NAME, binding.etName.getText().toString().trim()).apply();
                                AppController.sharedPreferencesCompat.edit().putString(AppConstants.EMAIL, binding.etEmail.getText().toString().toString()).apply();
                                AppController.sharedPreferencesCompat.edit().putString(AppConstants.USER_ID, response.body().getData().getUserId() + "").apply();
                                AppController.sharedPreferencesCompat.edit().putBoolean(AppConstants.IS_SOCIAL, false).apply();
                                AppController.sharedPreferencesCompat.edit().putString(AppConstants.PHONE_NUMBER, binding.phone.getText().toString().trim()).apply();


                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);


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
        });


        binding.includeRegister.btnFacebook.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(RegisterFragment.this, Arrays.asList("public_profile", "email"));

            }
        });

        binding.includeRegister.btnGoogle.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        if (mGoogleApiClient.isConnected())

        {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                Log.d(TAG, status.getStatusMessage() + "" + status.getStatus());
                            }
                        }
                    });
        }


        return binding.getRoot();
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                account.getDisplayName();

                Intent intent = new Intent(getActivity(), PhoneNumberActivity.class);
                intent.putExtra(AppConstants.USER_NAME, account.getDisplayName());
                intent.putExtra(AppConstants.EMAIL, account.getEmail());
                intent.putExtra(AppConstants.IMAGE_URL, account.getPhotoUrl());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            } else {
                showMessageInSnackBar(result.getStatus().getStatusMessage());

            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void faceBookSDKInit() {


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");

                        Profile profile = Profile.getCurrentProfile();

                        showProgressBarWithMessage("Getting Data from facebook\nPlease wait...");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                        pDialog.hide();
                                        // Application code
                                        Log.i("LoginActivity", response.toString());

                                        HashMap<String, String> fbDataHashMap = UtilHelper.getFacebookData(jsonObject);

                                        Intent intent = new Intent(getActivity(), PhoneNumberActivity.class);

                                        intent.putExtra(AppConstants.USER_NAME, fbDataHashMap.get("first_name") + " " + fbDataHashMap.get("last_name"));
                                        intent.putExtra(AppConstants.EMAIL, fbDataHashMap.get("email"));
                                        AppController.sharedPreferencesCompat.edit().putString(AppConstants.SOCIAL_NAME, AppConstants.SOCIAL_NAME_FACEBOOK).apply();
                                        AppController.sharedPreferencesCompat.edit().putString(AppConstants.SOCIAL_NAME, AppConstants.SOCIAL_NAME_FACEBOOK).apply();
                                        AppController.sharedPreferencesCompat.edit().putString(AppConstants.IMAGE_URL, fbDataHashMap.get("profile_pic"));
                                        getActivity().startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showMessageInSnackBar(exception.getMessage());
                    }
                });

    }

    private void googleSdkInit() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().requestProfile()
                .build();

        // Google Api client
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 1, RegisterFragment.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
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
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showMessageInSnackBar(connectionResult.getErrorMessage());
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
