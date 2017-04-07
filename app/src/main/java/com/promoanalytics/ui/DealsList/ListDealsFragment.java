package com.promoanalytics.ui.DealsList;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.promoanalytics.BR;
import com.promoanalytics.R;
import com.promoanalytics.adapter.DealViewHolder;
import com.promoanalytics.databinding.FragmentHomeNewBinding;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.model.AllDeals.Detail;
import com.promoanalytics.model.Category.Datum;
import com.promoanalytics.model.SaveDealModel;
import com.promoanalytics.model.SearchLayoutModel;
import com.promoanalytics.ui.AddToFavFromList;
import com.promoanalytics.ui.CategoryNameCallBack;
import com.promoanalytics.ui.DealDetail.CouponDetailActivity;
import com.promoanalytics.ui.DealsOnMap.CategoryDialogFragmentFromMaps;
import com.promoanalytics.ui.DealsOnMap.DealsOnMapFragment;
import com.promoanalytics.ui.TabChangedOtto;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.BusProvider;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.promoanalytics.utils.UtilHelper;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by think360user on 15/3/17.
 */

public class ListDealsFragment extends RootFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, CategoryNameCallBack {


    public static final String TAG = ListDealsFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String id = "";
    private static boolean isFirstTime = false;
    private String currentLocation = "";
    private String categoryId = "";
    private FragmentHomeNewBinding fragmentHomeNewBinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FragmentManager fm;
    private PromoAnalyticsServices promoAnalyticsServices;
    private LatLng latLng = new LatLng(43.717899, -79.658251);
    private SearchLayoutModel searchLayoutModel = new SearchLayoutModel("Searching..", "Select Location", "Select Category", false);
    private TitlesOnListPage titlesOnListPage = new TitlesOnListPage("Featured Coupons", "Other Coupons");
    private CategoryDialogFragmentFromMaps editNameDialogFragment;
    private List<Detail> detailListUnFeaturedCoupons = new ArrayList<>();
    private List<Detail> detailListFeaturedCoupons = new ArrayList<>();
    private Integer pageNumberUnFeatured = 1, pageNumberFeatured = 1;
    private AllDealsRvAdapter allUnFeaturedDealsRvAdapter, allFeaturedDealsRvAdapter;

    public static ListDealsFragment newInstance(String param1, String param2) {
        ListDealsFragment fragment = new ListDealsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), 3, this)
                    .addConnectionCallbacks(ListDealsFragment.this)
                    .addOnConnectionFailedListener(this).addApi(LocationServices.API)
                    .build();
        }


        fragmentHomeNewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_new, container, false);
        fragmentHomeNewBinding.searchLayout.setData(searchLayoutModel);
        fragmentHomeNewBinding.searchLayout.ivResetCategory.setOnClickListener(this);

        fragmentHomeNewBinding.rvFeaturedCoupons.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragmentHomeNewBinding.rvNormalCoupons.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        fragmentHomeNewBinding.rvFeaturedCoupons.setHasFixedSize(true);
        fragmentHomeNewBinding.rvNormalCoupons.setHasFixedSize(true);
        promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);


        // Register a listener that receives callbacks when a suggestion has been selected
        fragmentHomeNewBinding.searchLayout.autoCategorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editNameDialogFragment = CategoryDialogFragmentFromMaps.newInstance();
                editNameDialogFragment.setCallBack(ListDealsFragment.this);
                editNameDialogFragment.show(fm, "fragment_edit_name");

            }
        });

        fragmentHomeNewBinding.searchLayout.autoCompleteLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });
        fragmentHomeNewBinding.rvNormalCoupons.setNestedScrollingEnabled(false);
        fragmentHomeNewBinding.rvFeaturedCoupons.setNestedScrollingEnabled(false);

        return fragmentHomeNewBinding.getRoot();

    }


    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place Selected: " + place.getName());

                this.currentLocation = place.getName() + "";

                // Format the place's details and display them in the TextView.
                searchLayoutModel.setLocationSearchTitle(place.getName() + "");

                latLng = place.getLatLng();


                clearLists();
                showFeaturedCoupons(categoryId, latLng);
                showUnFeaturedCoupons(categoryId, latLng);


                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    // mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
                } else {
                    //  mPlaceAttribution.setText("");
                }

                Log.i(TAG, "Place details received: " + place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    private void clearLists() {
        detailListFeaturedCoupons.clear();
        detailListUnFeaturedCoupons.clear();
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


    private void showFeaturedCoupons(@NonNull final String categoryId, @NonNull final LatLng latLng) {

        Call<AllDeals> getAllDealsCall = promoAnalyticsServices.getAllDeals(categoryId, latLng.latitude + "", latLng.longitude + "", AppConstants.FEATURED_DEALS, AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), pageNumberFeatured + "");
        getAllDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                try {


                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            if (response.body().getData().getDetail().size() > 0) {

                                fragmentHomeNewBinding.tvNoFeaturedCoupons.setVisibility(View.GONE);
                                fragmentHomeNewBinding.rvFeaturedCoupons.setVisibility(View.VISIBLE);

                                if (allFeaturedDealsRvAdapter == null) {
                                    allFeaturedDealsRvAdapter = new AllDealsRvAdapter(detailListFeaturedCoupons);
                                    fragmentHomeNewBinding.rvFeaturedCoupons.setAdapter(allFeaturedDealsRvAdapter);

                                }
                                detailListFeaturedCoupons.addAll(response.body().getData().getDetail());
                                allFeaturedDealsRvAdapter.notifyDataSetChanged();
                                pageNumberFeatured = response.body().getData().getNextPage();
                                if (pageNumberFeatured != 0) {
                                    showFeaturedCoupons(categoryId, latLng);
                                } else {
                                    pageNumberFeatured = 1;
                                }


                            }
                        } else {
                            fragmentHomeNewBinding.tvNoFeaturedCoupons.setVisibility(View.VISIBLE);
                            fragmentHomeNewBinding.rvFeaturedCoupons.setVisibility(View.GONE);
                        }


                    } else {
                        // showDialog(response.body().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {
                showMessageInSnackBar(t.getMessage());
            }
        });
    }


    private void showUnFeaturedCoupons(@NonNull final String categoryId, @NonNull final LatLng latLng) {

        try {


            Call<AllDeals> getAllFeaturedDealsCall = promoAnalyticsServices.getAllDeals(categoryId, latLng.latitude + "", latLng.longitude + "", AppConstants.UNFEATURED_DEALS, AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), pageNumberUnFeatured + "");
            getAllFeaturedDealsCall.enqueue(new Callback<AllDeals>() {
                @Override
                public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus()) {
                                if (response.body().getData().getDetail().size() > 0) {
                                    fragmentHomeNewBinding.tvNoUnFeaturedCoupons.setVisibility(View.GONE);
                                    fragmentHomeNewBinding.rvNormalCoupons.setVisibility(View.VISIBLE);
                                    if (allUnFeaturedDealsRvAdapter == null) {
                                        allUnFeaturedDealsRvAdapter = new AllDealsRvAdapter(detailListUnFeaturedCoupons);
                                        fragmentHomeNewBinding.rvNormalCoupons.setAdapter(allUnFeaturedDealsRvAdapter);
                                    }
                                    detailListUnFeaturedCoupons.addAll(response.body().getData().getDetail());
                                    allUnFeaturedDealsRvAdapter.notifyDataSetChanged();
                                    pageNumberUnFeatured = response.body().getData().getNextPage();
                                    if (pageNumberUnFeatured != 0) {
                                        showUnFeaturedCoupons(categoryId, latLng);
                                    } else {
                                        pageNumberUnFeatured = 1;
                                    }
                                }
                            } else {
                                fragmentHomeNewBinding.tvNoUnFeaturedCoupons.setVisibility(View.VISIBLE);
                                fragmentHomeNewBinding.rvNormalCoupons.setVisibility(View.GONE);
                            }


                        } else {
                            //  showDialog(response.body().getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AllDeals> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {


            if (!isFirstTime) {
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                showFeaturedCoupons("", latLng);
                showUnFeaturedCoupons("", latLng);
                isFirstTime = true;
            }


        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        BusProvider.getInstance().unregister(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirstTime = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        BusProvider.getInstance().register(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fm = getChildFragmentManager();

    }

    @Subscribe
    public void wantToChangeTab(TabChangedOtto tabChangedOtto) {

        if (tabChangedOtto.getDetail() != null) {
            Log.i("IN_TAB_2", tabChangedOtto.getDetail().getCategoryName());
            searchLayoutModel.setCategorySearchTitle(tabChangedOtto.getDetail().getCategoryName());
            searchLayoutModel.setLocationSearchTitle(tabChangedOtto.getLocation());

            this.categoryId = tabChangedOtto.getDetail().getCategoryId();
            searchLayoutModel.setOnAllCategory(true);

            latLng = new LatLng(Double.parseDouble(tabChangedOtto.getDetail().getLatitude()), Double.parseDouble(tabChangedOtto.getDetail().getLongitude()));
            clearLists();
            showFeaturedCoupons(tabChangedOtto.getDetail().getCategoryId(), latLng);
            showUnFeaturedCoupons(tabChangedOtto.getDetail().getCategoryId(), latLng);
            DealsOnMapFragment.tabSelected = 0;
            DealsOnMapFragment.mDetail = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivResetCategory:
                searchLayoutModel.setCategorySearchTitle("Select Category");
                searchLayoutModel.setOnAllCategory(false);
                categoryId = "";
                clearLists();
                showUnFeaturedCoupons("", latLng);
                showFeaturedCoupons("", latLng);

                break;
        }

    }

    @Produce
    public AddToFavFromList addToFav() {

        return new AddToFavFromList(id);
    }

    @Override
    public void sendMeBackCategoryIdAndName(Datum datum) {

        if (datum != null) {
            editNameDialogFragment.dismiss();
            searchLayoutModel.setOnAllCategory(true);
            searchLayoutModel.setCategorySearchTitle(datum.getName());
            this.categoryId = datum.getId();
            clearLists();
            showFeaturedCoupons(datum.getId(), latLng);
            showUnFeaturedCoupons(datum.getId(), latLng);
            Log.i("CATEGORY_ID", datum.getName());
        }


    }

    private class AllDealsRvAdapter extends RecyclerView.Adapter<DealViewHolder> {

        private ArrayList<Detail> arrayListDeals;

        public AllDealsRvAdapter(List<Detail> arrayList) {
            this.arrayListDeals = (ArrayList<Detail>) arrayList;

        }


        @Override
        public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_item_deal, parent, false);
            return new DealViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DealViewHolder holder, int position) {
            final Detail singleDeal = arrayListDeals.get(position);


            if (singleDeal.getIsFav() != 0) {
                holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));
            } else {
                holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.hrtunfilled));
            }
            holder.cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
                    intent.putExtra(CouponDetailActivity.ARG_PARAM1, singleDeal.getId());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });

            holder.tvDiscount.setText(String.format("%s%s OFF", singleDeal.getDiscount(), "%"));

            holder.tvDealDetail.setText(singleDeal.getDescription());
            Picasso.with(getActivity()).load(String.valueOf(arrayListDeals.get(position).getLogo())).placeholder(R.drawable.logo_placeholder).error(R.drawable.logo_placeholder).into(holder.ivDeal);


            holder.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (singleDeal.getIsFav() != 1) {
                        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                        Call<SaveDealModel> saveDealModelCall = promoAnalyticsServices.saveToFavourite(AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, ""), singleDeal.getId(), 1);
                        saveDealModelCall.enqueue(new Callback<SaveDealModel>() {
                            @Override
                            public void onResponse(Call<SaveDealModel> call, Response<SaveDealModel> response) {
                                if (response.body().getStatus()) {

                                    singleDeal.setIsFav(1);
                                    id = singleDeal.getId();
                                    BusProvider.getInstance().post(addToFav());
                                    UtilHelper.animateOverShoot(holder.ivHeart);
                                    holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));

                                } else {
                                    showMessageInSnackBar(response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<SaveDealModel> call, Throwable t) {
                                showMessageInSnackBar(t.getMessage());
                            }
                        });
                    } else {
                        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                        Call<SaveDealModel> saveDealModelCall = promoAnalyticsServices.saveToFavourite(AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, ""), singleDeal.getId(), 0);
                        saveDealModelCall.enqueue(new Callback<SaveDealModel>() {
                            @Override
                            public void onResponse(Call<SaveDealModel> call, Response<SaveDealModel> response) {
                                if (response.body().getStatus()) {

                                    singleDeal.setIsFav(0);
                                    id = singleDeal.getId();
                                    BusProvider.getInstance().post(addToFav());
                                    UtilHelper.animateOverShoot(holder.ivHeart);
                                    holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.hrtunfilled));

                                } else {
                                    showMessageInSnackBar(response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<SaveDealModel> call, Throwable t) {
                                showMessageInSnackBar(t.getMessage());
                            }
                        });
                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayListDeals.size();
        }
    }

    public class TitlesOnListPage extends BaseObservable {
        public String featuredTitle, unFeaturedTitle;

        public TitlesOnListPage(String featuredTitle, String unFeaturedTitle) {
        }

        @Bindable
        public String getFeaturedTitle() {
            return featuredTitle;
        }

        public void setFeaturedTitle(String featuredTitle) {
            this.featuredTitle = featuredTitle;
            notifyPropertyChanged(BR.featuredTitle);
        }

        @Bindable
        public String getUnFeaturedTitle() {
            return unFeaturedTitle;
        }

        public void setUnFeaturedTitle(String unFeaturedTitle) {
            this.unFeaturedTitle = unFeaturedTitle;
            notifyPropertyChanged(BR.unFeaturedTitle);
        }
    }
}
