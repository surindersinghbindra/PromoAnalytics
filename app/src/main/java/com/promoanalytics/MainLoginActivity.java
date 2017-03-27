/*
package com.promoanalytics;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.promoanalytics.login.LoginActivity;
import com.promoanalytics.login.login.LoginFragment;
import com.promoanalytics.login.login.RegisterFragment;

public class MainLoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,RegisterFragment.OnFragmentInteractionListener,PhoneNumberFragment.OnFragmentInteractionListener {


    private LoginActivity mainFragmentStudent;
    private boolean doubleBackToExitPressedOnce = false;
    //    private TextView mMessageView;
    public static MainLoginActivity self;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        self = this;
        setContentView(R.layout.activity_main_login);


        if (savedInstanceState == null) {
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserving the fragment stack inside each tab
            initScreen();

        } else {
            // restoring the previously created fragment
            // and getting the reference

            mainFragmentStudent = (LoginActivity) getSupportFragmentManager().getFragments().get(0);
        }



    }

    private void initScreen() {
        // Creating the ViewPager container fragment once
        mainFragmentStudent = new LoginActivity();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mainFragmentStudent)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {

        if (!mainFragmentStudent.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class

            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
                super.onBackPressed();
//                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;

            Toast.makeText(this, "Please BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        } else {
            // carousel handled the back pressed task
            // do not call super


        }
    }
}
*/
