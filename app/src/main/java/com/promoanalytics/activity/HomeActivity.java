package com.promoanalytics.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.promoanalytics.DealsOnMap.DealsOnMapFragment;
import com.promoanalytics.R;
import com.promoanalytics.SavedCoupons.SavedDealsFragment;
import com.promoanalytics.adapter.LoginPagerAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DealsOnMapFragment.OnFragmentInteractionListener {


    private BottomBar bottomBar;
    private FragmentTransaction fragmentTransaction;
    private ViewPager vpHomeActivity;

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(DealsOnMapFragment.newInstance("Fragment 1", ""));
        fList.add(ListDealsFragment.newInstance("Fragment 2", ""));
        fList.add(SavedDealsFragment.newInstance("Fragment 2", ""));
        fList.add(EditProfile.newInstance("Fragment 2", ""));
        return fList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        vpHomeActivity = (ViewPager) findViewById(R.id.vpHomeActivity);
        vpHomeActivity.setOffscreenPageLimit(4);
        vpHomeActivity.setAdapter(new LoginPagerAdapter(getSupportFragmentManager(), getFragments()));
        vpHomeActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomBar.selectTabWithId(R.id.tab_map);
                        break;
                    case 1:
                        bottomBar.selectTabWithId(R.id.tab_list);
                        break;
                    case 2:
                        bottomBar.selectTabWithId(R.id.tab_saved);
                        break;
                    case 3:
                        bottomBar.selectTabWithId(R.id.tab_user);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Log.e("FRAG", tabId + "");
                switch (tabId) {
                    case R.id.tab_map:
                        vpHomeActivity.setCurrentItem(0);
                        break;
                    case R.id.tab_list:
                        vpHomeActivity.setCurrentItem(1);
                        break;
                    case R.id.tab_saved:
                        vpHomeActivity.setCurrentItem(2);
                        break;
                    case R.id.tab_user:
                        vpHomeActivity.setCurrentItem(3);
                        break;
                    default:
                        break;
                }

            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {

            }
        });


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
