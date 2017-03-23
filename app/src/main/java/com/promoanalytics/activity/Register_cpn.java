/*
package com.promoanalytics.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.promoanalytics.R;
import com.promoanalytics.post.RegisterCPnApi;


*/
/**
 * Created by think360user on 14/3/17.
 *//*


public class Register_cpn extends Activity {
    TextView logins;
    TextInputLayout fname, flname, femail, fphone, fpassword;
    EditText name, lname, email, phone, password;
    Switch mySwitch;
    ImageView iv_chck, iv_unchck;
    AppCompatButton btn_signup;
    String fnames, lnames, emails, phones, passwords, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_cpnapp);
        logins = (TextView) findViewById(R.id.logins);
        fname = (TextInputLayout) findViewById(R.id.fname);
        flname = (TextInputLayout) findViewById(R.id.flname);
        femail = (TextInputLayout) findViewById(R.id.femail);
        fphone = (TextInputLayout) findViewById(R.id.fphone);
        fpassword = (TextInputLayout) findViewById(R.id.fpassword);
        name = (EditText) findViewById(R.id.name);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);
        iv_chck = (ImageView) findViewById(R.id.iv_chck);
        iv_unchck = (ImageView) findViewById(R.id.iv_unchck);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    name.setHint("");
                flname.setError("");

                femail.setError("");
                fphone.setError("");
                fpassword.setError("");

            }
        });
        lname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    lname.setHint("");
                fname.setError("");

                femail.setError("");
                fphone.setError("");
                fpassword.setError("");

            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    email.setHint("");
                flname.setError("");
                fname.setError("");


                fphone.setError("");
                fpassword.setError("");

            }
        });
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    phone.setHint("");
                flname.setError("");
                fname.setError("");

                femail.setError("");

                fpassword.setError("");

            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    password.setHint("");
                flname.setError("");
                fname.setError("");

                femail.setError("");
                fphone.setError("");

            }
        });
        iv_chck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_unchck.setVisibility(View.VISIBLE);
                iv_chck.setVisibility(View.GONE);
            }
        });
        iv_unchck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_unchck.setVisibility(View.GONE);
                iv_chck.setVisibility(View.VISIBLE);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnames = name.getText().toString().trim();
                lnames = lname.getText().toString().trim();
                emails = email.getText().toString().trim();
                phones = phone.getText().toString().trim();

                passwords = password.getText().toString().trim();
                String Expn =
                        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                if (fnames.equals("")) {
                    fname.setError("Enter First name");
                    flname.setError("");
                    femail.setError("");
                    fphone.setError("");
                    fpassword.setError("");
                } else if (lnames.equals("")) {
                    flname.setError("Enter Last name");
                    fname.setError("");
                    femail.setError("");
                    fphone.setError("");
                    fpassword.setError("");
                } else if (emails.equals("")) {
                    femail.setError("Enter Email");
                    flname.setError("");
                    fname.setError("");
                    fphone.setError("");
                    fpassword.setError("");
                } else if (!(emails.matches(Expn) && emails.length() > 0)) {
                    femail.setError("Enter valid email");

                } else if (phones.equals("")) {
                    fphone.setError("Enter phone no");
                    flname.setError("");
                    femail.setError("");
                    fname.setError("");
                    fpassword.setError("");
                } else if (passwords.equals("")) {
                    fpassword.setError("Enter Password");
                    flname.setError("");
                    femail.setError("");
                    fphone.setError("");
                    fname.setError("");
                } else if (iv_chck.getVisibility() != View.VISIBLE) {
                    flname.setError("");
                    femail.setError("");
                    fphone.setError("");
                    fname.setError("");
                    fpassword.setError("");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Register_cpn.this);
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
                    fullname = fnames + " " + lnames;
                    RegisterCPnApi registerCPnApi = new RegisterCPnApi(Register_cpn.this, fullname, emails, phones, passwords);
                    registerCPnApi.addQueue();
                }
            }
        });

        logins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_cpn.this, Login_Cpn.class);
                startActivity(intent);
            }
        });
    }
}
*/
