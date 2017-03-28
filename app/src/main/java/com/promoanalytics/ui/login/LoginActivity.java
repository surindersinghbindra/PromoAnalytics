package com.promoanalytics.ui.login;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.promoanalytics.R;
import com.promoanalytics.adapter.LoginPagerAdapter;
import com.promoanalytics.databinding.LoginCpnappBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by think360user on 14/3/17.
 */

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener {

    // viewpager code
    private List<Fragment> fragments = getFragments();
    private LoginCpnappBinding login_cpn;
    private LoginPagerAdapter loginPagerAdapter;

    // fragments
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(LoginFragment.newInstance("Fragment 1", ""));
        fList.add(RegisterFragment.newInstance("Fragment 2", ""));
        return fList;
    }


    /* @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login_cpn = DataBindingUtil.setContentView(this, R.layout.login_cpnapp, null);

        loginPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager(), fragments);
        login_cpn.vpLogin.setAdapter(loginPagerAdapter);
        login_cpn.vpLogin.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setPagerSelection(0);
                        break;
                    case 1:
                        setPagerSelection(1);
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void login(View view) {
        login_cpn.vpLogin.setCurrentItem(0);
        setPagerSelection(0);
    }

    public void register(View view) {
        login_cpn.vpLogin.setCurrentItem(1);

        setPagerSelection(1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setPagerSelection(int pagerSelection) {
        switch (pagerSelection) {
            case 0:

                login_cpn.loginLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_blue_left));
                login_cpn.loginButton.setTextColor(getResources().getColor(R.color.white));

                login_cpn.registerLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_white_right));
                login_cpn.registerButton.setTextColor(getResources().getColor(R.color.appBlue));
                break;
            case 1:
                login_cpn.loginLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_white_left));
                login_cpn.loginButton.setTextColor(getResources().getColor(R.color.appBlue));

                login_cpn.registerLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_shape_blue_right));
                login_cpn.registerButton.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }


}
