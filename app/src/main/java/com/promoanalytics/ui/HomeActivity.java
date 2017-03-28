package com.promoanalytics.ui;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.promoanalytics.R;
import com.promoanalytics.adapter.LoginPagerAdapter;
import com.promoanalytics.databinding.ActivityHomeBinding;
import com.promoanalytics.ui.DealsOnMap.DealsOnMapFragment;
import com.promoanalytics.ui.SavedCoupons.SavedDealsFragment;
import com.promoanalytics.ui.dealslist.ListDealsFragment;
import com.promoanalytics.ui.profile.EditProfileFragment;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.BaseAppCompatActivity;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseAppCompatActivity implements DealsOnMapFragment.OnFragmentInteractionListener {

    private boolean doubleBackToExitPressedOnce = false;

    private ActivityHomeBinding activityHomeBinding;


    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(DealsOnMapFragment.newInstance("Fragment 1", ""));
        fList.add(ListDealsFragment.newInstance("Fragment 2", ""));
        fList.add(SavedDealsFragment.newInstance("Fragment 2", ""));
        fList.add(EditProfileFragment.newInstance("Fragment 2", ""));
        return fList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        AppController.sharedPreferencesCompat.edit().putBoolean(AppConstants.IS_LOGIN, true).apply();

        activityHomeBinding.vpHomeActivity.setOffscreenPageLimit(4);
        activityHomeBinding.vpHomeActivity.setAdapter(new LoginPagerAdapter(getSupportFragmentManager(), getFragments()));
        activityHomeBinding.vpHomeActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        activityHomeBinding.bottomBar.selectTabWithId(R.id.tab_map);
                        break;
                    case 1:
                        activityHomeBinding.bottomBar.selectTabWithId(R.id.tab_list);
                        break;
                    case 2:
                        activityHomeBinding.bottomBar.selectTabWithId(R.id.tab_saved);
                        break;
                    case 3:
                        activityHomeBinding.bottomBar.selectTabWithId(R.id.tab_user);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        activityHomeBinding.bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Log.e("FRAG", tabId + "");
                switch (tabId) {
                    case R.id.tab_map:
                        activityHomeBinding.vpHomeActivity.setCurrentItem(0);
                        break;
                    case R.id.tab_list:
                        activityHomeBinding.vpHomeActivity.setCurrentItem(1);
                        break;
                    case R.id.tab_saved:
                        activityHomeBinding.vpHomeActivity.setCurrentItem(2);
                        break;
                    case R.id.tab_user:
                        activityHomeBinding.vpHomeActivity.setCurrentItem(3);
                        break;
                    default:
                        break;
                }

            }
        });

        activityHomeBinding.bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {

            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //showMessageInSnackBar(activityHomeBinding.vpHomeActivity, "Please BACK again to exit");
        Toast.makeText(this, "Please BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce = false;
            }
        }, 2000);


    }

}
