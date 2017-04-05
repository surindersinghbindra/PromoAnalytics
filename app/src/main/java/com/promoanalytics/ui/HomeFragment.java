package com.promoanalytics.ui;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.promoanalytics.R;
import com.promoanalytics.adapter.LoginPagerAdapter;
import com.promoanalytics.databinding.ActivityHomeBinding;
import com.promoanalytics.ui.DealsList.ListDealsFragment;
import com.promoanalytics.ui.DealsOnMap.DealsOnMapFragment;
import com.promoanalytics.ui.SavedCoupons.SavedDealsFragment;
import com.promoanalytics.ui.profile.EditProfileFragment;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.BusProvider;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements DealsOnMapFragment.OnFragmentInteractionListener {

    private boolean doubleBackToExitPressedOnce = false;

    private ActivityHomeBinding activityHomeBinding;
    private LoginPagerAdapter loginPagerAdapter;

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(DealsOnMapFragment.newInstance("Fragment 1", ""));
        fList.add(ListDealsFragment.newInstance("Fragment 2", ""));
        fList.add(SavedDealsFragment.newInstance("Fragment 2", ""));
        fList.add(EditProfileFragment.newInstance("Fragment 2", ""));
        return fList;
    }

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        activityHomeBinding = DataBindingUtil.inflate(inflater, R.layout.activity_home, container, false);

        AppController.sharedPreferencesCompat.edit().putBoolean(AppConstants.IS_LOGIN, true).apply();

        activityHomeBinding.vpHomeActivity.setOffscreenPageLimit(4);

        loginPagerAdapter = new LoginPagerAdapter(getChildFragmentManager(), getFragments());
        activityHomeBinding.vpHomeActivity.setAdapter(loginPagerAdapter);
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

        return activityHomeBinding.getRoot();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) loginPagerAdapter.getRegisteredFragment(activityHomeBinding.vpHomeActivity.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }

    @Subscribe
    public void wantToChangeTab(TabChangedOtto tabChangedOtto) {

        switch (tabChangedOtto.getTabSelected()) {
            case R.id.tab_list:
                activityHomeBinding.bottomBar.selectTabWithId(R.id.tab_list);
                break;
            default:
                break;

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }
}
