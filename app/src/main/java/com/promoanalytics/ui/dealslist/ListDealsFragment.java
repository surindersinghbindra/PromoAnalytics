package com.promoanalytics.ui.dealslist;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.promoanalytics.R;
import com.promoanalytics.adapter.DealViewHolder;
import com.promoanalytics.databinding.FragmentHomeNewBinding;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.model.AllDeals.Detail;
import com.promoanalytics.model.Category.CategoryModel;
import com.promoanalytics.model.Category.Datum;
import com.promoanalytics.model.SaveDealModel;
import com.promoanalytics.model.SearchLayoutModel;
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.modules.GPSTracker;
import com.promoanalytics.ui.CategoryDialogFragment;
import com.promoanalytics.ui.CouponDetailFragment;
import com.promoanalytics.ui.DealsOnMap.CategoryChange;
import com.promoanalytics.ui.SavedCoupons.SavedDealsFragment;
import com.promoanalytics.ui.TabChangedOtto;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.BusProvider;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.promoanalytics.utils.UtilHelper;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

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

public class ListDealsFragment extends RootFragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    public static final String TAG = ListDealsFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    ExpandableHeightGridView gridview;
    ExpandableHeightGridView gridviews;
    double currentLatitude, currentLongitude;
    Location location;
    TwoWayView lvTest;
    GPSTracker gps;
    private String currentLocation = "";
    private String categoryId = "";
    private ArrayList<Detail> myList;
    private FragmentHomeNewBinding fragmentHomeNewBinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleApiClient mGoogleApiClient;
    private FragmentManager fm;
    private PromoAnalyticsServices promoAnalyticsServices;
    private LatLng latLng = new LatLng(43.717899, -79.658251);

    private SearchLayoutModel searchLayoutModel = new SearchLayoutModel("Searching..", "Select Location", "Select Category", false);

    private CategoryDialogFragment editNameDialogFragment;

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
                    .addOnConnectionFailedListener(this).addApi(Places.GEO_DATA_API)
                    .build();
        }


        fragmentHomeNewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_new, container, false);
        fragmentHomeNewBinding.searchLayout.setData(searchLayoutModel);

        fragmentHomeNewBinding.rvFeaturedCoupons.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragmentHomeNewBinding.rvNormalCoupons.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);

        // showProgressBar();
        showFeaturedCoupons("", "30.7360306", "76.7328649");
        showUnFeaturedCoupons("", "30.7360306", "76.7328649");


        Call<CategoryModel> categoryModelCall = promoAnalyticsServices.getCategories();
        categoryModelCall.enqueue(new Callback<CategoryModel>() {


            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.body().getStatus()) {

                    ArrayList<String> categoryList = new ArrayList<String>();
                    for (Datum datum : response.body().getData()) {
                        categoryList.add(datum.getName());
                    }
                }

            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });


        // Register a listener that receives callbacks when a suggestion has been selected
        fragmentHomeNewBinding.searchLayout.autoCategorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editNameDialogFragment = CategoryDialogFragment.newInstance();
                editNameDialogFragment.setWhoWillBetheListner(2);
                editNameDialogFragment.show(fm, "fragment_edit_name");

            }
        });

        fragmentHomeNewBinding.searchLayout.autoCompleteLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });


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
                fragmentHomeNewBinding.searchLayout.autoCompleteLocationSearch.setText(place.getName());

                latLng = place.getLatLng();

                fragmentHomeNewBinding.searchLayout.slectn.setVisibility(View.GONE);


                showFeaturedCoupons(categoryId, "30.7360306", "76.7328649");
                showUnFeaturedCoupons(categoryId, "30.7360306", "76.7328649");


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


    private void showUnFeaturedCoupons(@NonNull String categoryId, @NonNull String lat, @NonNull String lng) {
        Call<AllDeals> getAllFeaturedDealsCall = promoAnalyticsServices.getAllDeals(categoryId, lat, lng, AppConstants.UNFEATURED_DEALS, AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), "1");
        getAllFeaturedDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {


                if (response.body().getStatus()) {

                    fragmentHomeNewBinding.rvNormalCoupons.setAdapter(new AllDealsRvAdapter(response.body().getData().getDetail()));
                } else {
                    //  showDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });
    }


    private void showFeaturedCoupons(@NonNull String categoryId, @NonNull String lat, @NonNull String lng) {

        Call<AllDeals> getAllDealsCall = promoAnalyticsServices.getAllDeals(categoryId, lat, lng, AppConstants.FEATURED_DEALS, AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), "1");
        getAllDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                if (response.body().getStatus()) {
                    fragmentHomeNewBinding.rvFeaturedCoupons.setAdapter(new AllDealsRvAdapter(response.body().getData().getDetail()));
                } else {
                    // showDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

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
            searchLayoutModel.setLocationSearchTitle(tabChangedOtto.getDetail().getName());

            this.categoryId = tabChangedOtto.getDetail().getCategoryId();
            searchLayoutModel.setOnAllCategory(false);
            showFeaturedCoupons(tabChangedOtto.getDetail().getCategoryId(), tabChangedOtto.getDetail().getLatitude(), tabChangedOtto.getDetail().getLongitude());
            showUnFeaturedCoupons(tabChangedOtto.getDetail().getCategoryId(), tabChangedOtto.getDetail().getLatitude(), tabChangedOtto.getDetail().getLongitude());


        }


    }

    @Subscribe
    public void onCategoryChange(CategoryChange event) {

        if (event != null && !TextUtils.isEmpty(event.datum.getName()) && event.whoWillBetheListner == 2) {
            editNameDialogFragment.dismiss();
            searchLayoutModel.setOnAllCategory(true);
            searchLayoutModel.setCategorySearchTitle(event.datum.getName());
            this.categoryId = event.datum.getId();
            // fetchDataFromRemote(event.datum.getId(), latLng);
            Log.i("CATEGORY_ID", event.datum.getName());
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
                    trasactFragment(R.id.container, CouponDetailFragment.newInstance(singleDeal, ""));
                }
            });

            holder.tvDiscount.setText(singleDeal.getDiscount());
            holder.tvDiscount.setText(singleDeal.getDiscount());
            holder.tvDealDetail.setText(singleDeal.getDescription());
            Picasso.with(getActivity()).load(String.valueOf(arrayListDeals.get(position).getLogo())).placeholder(R.drawable.logo_placeholder).error(R.drawable.logo_placeholder).into(holder.ivDeal);


            holder.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                    Call<SaveDealModel> saveDealModelCall = promoAnalyticsServices.saveToFavourite(AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, ""), singleDeal.getId(), 1);
                    saveDealModelCall.enqueue(new Callback<SaveDealModel>() {
                        @Override
                        public void onResponse(Call<SaveDealModel> call, Response<SaveDealModel> response) {
                            if (response.body().getStatus()) {
                                singleDeal.setIsFav(1);
                                UtilHelper.animateOverShoot(holder.ivHeart);
                                holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));
                                SavedDealsFragment.updateReceiptsList(singleDeal);
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
            });
        }

        @Override
        public int getItemCount() {
            return arrayListDeals.size();
        }
    }


}
