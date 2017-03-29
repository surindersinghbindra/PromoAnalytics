package com.promoanalytics.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.promoanalytics.R;
import com.promoanalytics.ui.login.LoginActivity;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {


    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        handler = new Handler();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (AppController.sharedPreferencesCompat.getBoolean(AppConstants.IS_LOGIN, false) && AppController.sharedPreferencesCompat.getBoolean(AppConstants.IS_REMEMBER_TAPPED, false)) {
                    startActivity(new Intent(SplashActivity.this, MainActivityAfterLogin.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                }
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();

            }
        }, 3000);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        // delayedHide(100);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // something left here for
    }
}
