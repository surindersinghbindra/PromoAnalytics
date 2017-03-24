package com.promoanalytics.activity.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.promoanalytics.AppConstants;
import com.promoanalytics.R;
import com.promoanalytics.activity.HomeActivity;
import com.promoanalytics.databinding.FragmentLoginBinding;
import com.promoanalytics.modules.MyApplication;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;

import org.json.JSONObject;

import java.util.Arrays;

import im.delight.android.location.SimpleLocation;
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
public class LoginFragment extends RootFragment implements GoogleApiClient.OnConnectionFailedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //Google Login SDK
    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private FragmentLoginBinding binding;
    private GoogleApiClient mGoogleApiClient;
    private SimpleLocation location;
    //Facebook Login SDK
    private CallbackManager callbackManager;

    private PromoAnalyticsServices promoAnalyticsServices;
    // [START declare_auth]
    //  private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    // private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
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


        // construct a new instance of SimpleLocation
        // location = new SimpleLocation(getActivity());

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");

                        Profile profile = Profile.getCurrentProfile();
                        profile.getFirstName();

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // Application code
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getActivity(), "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_bold.ttf");
        binding.btnSignIn.setTypeface(font);


        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), LoginFragment.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emails = binding.inputName.getText().toString().trim();
                String phones = binding.inputPassword.getText().toString().trim();
                if (emails.equals("")) {
                    binding.femail.setError("Enter email/mobile no");
                } else if (phones.equals("")) {
                    binding.fpassword.setError("Enter password");
                } else {
                    showProgressBar();

                    promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);

                    Call<User> call = promoAnalyticsServices.loginUser(emails, phones, 1);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            if (response.isSuccessful()) {
                                pDialog.hide();

                          /*      // if we can't access the location yet
                                if (!location.hasLocationEnabled()) {
                                    // ask the user to enable location access
                                    SimpleLocation.openSettings(getActivity());
                                }

                                final double latitude = location.getLatitude();
                                final double longitude = location.getLongitude();*/


                                response.body().toString();
                                Log.d("RES_RETRO", response.body().getStatus() + "");
                                Log.d("RES_RETRO", response.body().getData().getUserId() + "");

                                MyApplication.getSharedPrefEditor().putString(AppConstants.USER_ID, response.body().getData().getUserId()).apply();
                                MyApplication.getSharedPrefEditor().putString(AppConstants.USER_NAME, response.body().getData().getName()).apply();
                                MyApplication.getSharedPrefEditor().putString(AppConstants.EMAIL, response.body().getData().getEmail()).apply();
                                MyApplication.getSharedPrefEditor().putString(AppConstants.PHONE_NUMBER, response.body().getData().getPhone()).apply();

                                getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));


                            } else {
                                System.out.print(response.errorBody());
                                showDialog(response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                            t.printStackTrace();
                        }
                    });


                    //  LoginAPiCpn loginAPiCpn = new LoginAPiCpn(getActivity(), emails, phones);
                    // loginAPiCpn.addQueue();
                }
            }
        });
        binding.includeLogin.btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));

            }
        });

        binding.includeLogin.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        binding.inputName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.inputName.setHint("");
                binding.fpassword.setError("");


            }
        });
        binding.inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    binding.inputPassword.setHint("");
                binding.femail.setError("");


            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        //   location.beginUpdates();
    }

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();

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
                //   firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                //  updateUI(null);
                // [END_EXCLUDE]
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
        //  location.endUpdates();
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
