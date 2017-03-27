package com.promoanalytics.splash;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.promoanalytics.R;
import com.promoanalytics.login.LoginActivity;
import com.viksaa.sssplash.lib.model.ConfigSplash;


/**
 * Created by varsovski on 28-Sep-15.
 */
public class SplashActivity extends AwesomeSplash {

    private static final int REQUEST_CAMERA = 0;
    private String TAG = SplashActivity.class.getSimpleName();
    //private RelativeLayout splashMain;
    private ConfigSplash cs2;

    @Override
    public void initSplash(ConfigSplash configSplash) {
        getAndSetSplashValues(configSplash);
    }

    @Override
    public void animationsFinished() {
        //wait 5 sec and then go back to MainActivity

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(SplashActivity.this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Contacts permissions have not been granted.
                    Log.i(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
                    requestExternalStoragePermission();
                } else {
                    ChangeActivityHelper.changeActivity(SplashActivity.this, LoginActivity.class, true);
                }

            }
        }, Constants.SPLASH_DELAY);
    }

    private void requestExternalStoragePermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mRlReveal, "Please allow all the permissions to proceed further in App",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(SplashActivity.this,
                                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission
                                            .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for Camera permission request.");
            Log.e("JAMM", permissions[0] + permissions[1] + permissions[2]);
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager
                    .PERMISSION_GRANTED) {

                ChangeActivityHelper.changeActivity(SplashActivity.this, LoginActivity.class, true);
            } else {
                requestExternalStoragePermission();
                Log.e("JAMM", "Else");

            }


        }

    }


    public void getAndSetSplashValues(ConfigSplash cs1) {

        cs2 = new ConfigSplash();
        cs2.setLogoSplash(R.drawable.logocpn);
        cs2.setPathSplashStrokeSize(10);
        cs2.setTitleSplash(getString(R.string.app_name));
        cs2.setTitleTextSize(25.0f);

        // ConfigSplash cs2 = (ConfigSplash) getIntent().getExtras().getSerializable(Constants.CONFIG);
        if (cs2 != null) {
            cs1.setAnimCircularRevealDuration(900);
            cs1.setRevealFlagX(cs2.getRevealFlagX());
            cs1.setRevealFlagY(cs2.getRevealFlagY());
            cs1.setBackgroundColor(R.color.colorAccent);

            cs1.setLogoSplash(cs2.getLogoSplash());
            cs1.setAnimLogoSplashTechnique(cs2.getAnimLogoSplashTechnique());
            cs1.setAnimLogoSplashDuration(cs2.getAnimLogoSplashDuration());

            cs1.setPathSplash(cs2.getPathSplash());
            cs1.setPathSplashStrokeSize(cs2.getPathSplashStrokeSize());
            cs1.setPathSplashStrokeColor(cs2.getPathSplashStrokeColor());
            cs1.setPathSplashFillColor(cs2.getPathSplashFillColor());
            cs1.setOriginalHeight(cs2.getOriginalHeight());
            cs1.setOriginalWidth(cs2.getOriginalWidth());
            cs1.setAnimPathStrokeDrawingDuration(cs2.getAnimPathStrokeDrawingDuration());
            cs1.setAnimPathFillingDuration(cs2.getAnimPathFillingDuration());

            cs1.setTitleSplash(cs2.getTitleSplash());
            cs1.setAnimTitleDuration(cs2.getAnimTitleDuration());
            cs1.setAnimTitleTechnique(cs2.getAnimTitleTechnique());
            cs1.setTitleTextSize(cs2.getTitleTextSize());
            cs1.setTitleTextColor(cs2.getTitleTextColor());
            cs1.setTitleFont(cs2.getTitleFont());
        }
    }


}
