package com.promoanalytics.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by think360 on 23/03/17.
 */

public class BaseAppCompatActivity extends AppCompatActivity {


    protected ProgressDialog pDialog;
    protected AlertDialog.Builder alertDialog;


    protected void showProgressBar() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

    }

    protected void showProgressBarWithMessage(String message) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

    }

    protected void showDialog(String message) {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(true);

        alertDialog.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    protected void showMessageInSnackBar(View view
            , String message) {

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();


    }

    @Override
    protected void onPause() {
        if (pDialog != null) {
            pDialog.hide();
        }
        super.onPause();
    }
}
