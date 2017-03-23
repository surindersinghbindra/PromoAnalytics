package com.promoanalytics.activity;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.promoanalytics.R;
import com.promoanalytics.activity.login.LoginFragment;
import com.promoanalytics.activity.login.LoginPagerAdapter;
import com.promoanalytics.activity.login.RegisterFragment;
import com.promoanalytics.databinding.LoginCpnappBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by think360user on 14/3/17.
 */

public class Login_Cpn extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {
    TextView registers;
    AppCompatButton btn_signup;
    TextInputLayout femail, fpassword;
    EditText input_name, input_password;
    ImageView iv_chck, iv_unchck;
    String emails, phones;

    //viewpager code
    private ViewPager vp_Login;
    private TextView loginButton;
    private List<Fragment> fragments = getFragments();
    private LoginCpnappBinding login_cpn;

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(LoginFragment.newInstance("Fragment 1", ""));
        fList.add(RegisterFragment.newInstance("Fragment 2", ""));
        return fList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login_cpn = DataBindingUtil.setContentView(Login_Cpn.this, R.layout.login_cpnapp);

        //ViewPager for login
        //vp_Login = (ViewPager) findViewById(R.id.vp_Login);
        login_cpn.vpLogin.setAdapter(new LoginPagerAdapter(getSupportFragmentManager(), fragments));
        // loginButton = (TextView) findViewById(R.id.loginButton);
        // registers = (TextView) findViewById(R.id.registers);


      /*  femail = (TextInputLayout) findViewById(R.id.femail);
        fpassword = (TextInputLayout) findViewById(R.id.fpassword);
        input_name = (EditText) findViewById(R.id.input_name);
        input_password = (EditText) findViewById(R.id.input_password);
        iv_chck = (ImageView) findViewById(R.id.iv_chck);

        iv_unchck = (ImageView) findViewById(R.id.iv_unchck);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);
        registers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Cpn.this, Register_cpn.class);
                startActivity(intent);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emails = input_name.getText().toString().trim();
                phones = input_password.getText().toString().trim();
                if (emails.equals("")) {
                    femail.setError("Enter email/mobile no");
                } else if (phones.equals("")) {
                    fpassword.setError("Enter password");
                }*//*else if (iv_chck.getVisibility() != View.VISIBLE) {

                    femail.setError("");

                    fpassword.setError("");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Login_Cpn.this);
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

                }
*//* else {

                    LoginAPiCpn loginAPiCpn = new LoginAPiCpn(Login_Cpn.this, emails, phones);
                    loginAPiCpn.addQueue();
                }


            }
        });
        */
  /*      input_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    input_name.setHint("");
                fpassword.setError("");


            }
        });
        input_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    input_password.setHint("");
                femail.setError("");


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
        });*/
    }

    public void login(View view) {
        login_cpn.vpLogin.setCurrentItem(0);
        login_cpn.loginLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_blue_left));
        login_cpn.loginButton.setTextColor(getResources().getColor(R.color.white));


        login_cpn.registerLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_white_right));
        login_cpn.registerButton.setTextColor(getResources().getColor(R.color.appBlue));
    }

    public void register(View view) {
        login_cpn.vpLogin.setCurrentItem(1);

        login_cpn.loginLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_white_left));
        login_cpn.loginButton.setTextColor(getResources().getColor(R.color.appBlue));


        login_cpn.registerLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_blue_right));
        login_cpn.registerButton.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
