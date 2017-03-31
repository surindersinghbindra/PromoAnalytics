package com.promoanalytics.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.promoanalytics.databinding.FragmentLoginBinding;
import com.promoanalytics.ui.ForgetPasswordActivity;
import com.promoanalytics.ui.MainActivityAfterLogin;
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
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends RootFragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //Google Login SDK
    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;
    private static final int LOGIN_WITH_EMAIL = 0;
    private static final int LOGIN_WITH_SOCIAL = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private FragmentLoginBinding binding;
    //Google API
    private GoogleApiClient mGoogleApiClient;
    //Facebook Login SDK
    private CallbackManager callbackManager;
    private PromoAnalyticsServices promoAnalyticsServices;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        // Inflate the layout for this fragment with data Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        binding.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.inputName.getText()) || TextUtils.isEmpty(binding.inputPassword.getText())) {
                    if (TextUtils.isEmpty(binding.inputName.getText())) {
                        showMessageInSnackBar("Please provide registered Email or Mobile number for login");
                    } else if (TextUtils.isEmpty(binding.inputPassword.getText())) {
                        showMessageInSnackBar("Please provide password for login");
                    }
                } else {
                    showProgressBar();
                    logIn(binding.inputName.getText().toString().trim(), binding.inputPassword.getText().toString().trim(), LOGIN_WITH_EMAIL);

                }

            }
        });
        binding.includeLogin.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(LoginFragment.this, Arrays.asList("public_profile", "email"));

            }
        });

        binding.includeLogin.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

        LoginManager.getInstance().logOut();
        return binding.getRoot();
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(LoginFragment.TAG, status.getStatusMessage());
                    }
                });
    }

    private void logIn(String email, String password, final int isSocial) {


        promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
        Call<User> call = null;
        switch (isSocial) {
            case 0:
                call = promoAnalyticsServices.loginUser(email, password, 0);
                break;
            case 1:
                call = promoAnalyticsServices.loginUser(email, password, 1);
                break;
        }

        assert call != null;
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    pDialog.hide();
                    if (response.body().getStatus()) {
                        // saving User data to shared preferences
                        AppController.getSharedPrefEditor().putBoolean(AppConstants.IS_SOCIAL, isSocial != 0).apply();
                        AppController.getSharedPrefEditor().putBoolean(AppConstants.IS_REMEMBER_TAPPED, binding.switchGprs.isChecked()).apply();
                        AppController.getSharedPrefEditor().putString(AppConstants.USER_ID, response.body().getData().getUserId()).apply();
                        AppController.getSharedPrefEditor().putString(AppConstants.USER_NAME, response.body().getData().getName()).apply();
                        AppController.getSharedPrefEditor().putString(AppConstants.EMAIL, response.body().getData().getEmail()).apply();
                        AppController.getSharedPrefEditor().putString(AppConstants.PHONE_NUMBER, response.body().getData().getPhone()).apply();

                        getActivity().startActivity(new Intent(getActivity(), MainActivityAfterLogin.class));
                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                        getActivity().finish();
                    } else {
                        showMessageInSnackBar(response.body().getMessage());
                    }


                } else {
                    System.out.print(response.errorBody());
                    showDialog(response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                pDialog.hide();
                showMessageInSnackBar(t.getMessage());
                t.printStackTrace();
            }
        });

    }


    private void googleSdkInit() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Google Api client
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0, LoginFragment.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }


    private void faceBookSDKInit() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        showProgressBarWithMessage("Getting Data from facebook\nPlease wait...");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        // Application code
                                        Log.i("LoginActivity", response.toString());
                                        HashMap<String, String> fbDataHashMap = UtilHelper.getFacebookData(object);
                                        logIn(fbDataHashMap.get("email"), "", LOGIN_WITH_SOCIAL);


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


    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {


                showProgressBarWithMessage("Getting Data from Google");
                GoogleSignInAccount account = result.getSignInAccount();
                logIn(account.getEmail(), "", LOGIN_WITH_SOCIAL);

            } else {
                showMessageInSnackBar(result.getStatus().getStatusMessage());


            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getActivity(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        signOut();
    }

    @Override
    public void onConnectionSuspended(int i) {

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
