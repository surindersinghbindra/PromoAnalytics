package com.promoanalytics.ui.dealslist;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.promoanalytics.R;
import com.promoanalytics.adapter.DealViewHolder;
import com.promoanalytics.adapter.PlaceAutocompleteAdapter;
import com.promoanalytics.databinding.FragmentHomeNewBinding;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.model.AllDeals.Detail;
import com.promoanalytics.model.Category.CategoryModel;
import com.promoanalytics.model.Category.Datum;
import com.promoanalytics.model.SaveDealModel;
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.modules.GPSTracker;
import com.promoanalytics.ui.CouponDetailFragment;
import com.promoanalytics.ui.HomeFragment;
import com.promoanalytics.ui.SavedCoupons.SavedDealsFragment;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.promoanalytics.utils.UtilHelper;
import com.squareup.picasso.Picasso;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private ArrayList<Detail> myList;
    private FragmentHomeNewBinding fragmentHomeNewBinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

           /* // Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));*/

            // Display the third party attributions if set.
          /*  final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }*/

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Toast.makeText(getActivity(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        fragmentHomeNewBinding.rvFeaturedCoupons.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragmentHomeNewBinding.rvNormalCoupons.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
        // Register a listener that receives callbacks when a suggestion has been selected
        fragmentHomeNewBinding.searchLayout.autoCompleteLocationSearch.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(getActivity(), mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);

        fragmentHomeNewBinding.searchLayout.autoCompleteLocationSearch.setAdapter(mAdapter);

        showProgressBar();
        Call<AllDeals> getAllDealsCall = promoAnalyticsServices.getAllDeals("", "30.7360306", "76.7328649", AppConstants.FEATURED_DEALS, AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), "1");
        getAllDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                pDialog.hide();
                if (response.body().getStatus()) {

                    Log.d("RETRO_GETALLDEALS", response.body().getStatus() + "");

                    Log.d("RETRO_GETALLDEALS", String.valueOf(response.body().getData().getDetail()));
                    Intent intent = new Intent(getActivity(), HomeFragment.class);
                    intent.putParcelableArrayListExtra("LIST_DEALS", (ArrayList<? extends Parcelable>) response.body().getData().getDetail());

                    fragmentHomeNewBinding.rvFeaturedCoupons.setAdapter(new AllDealsRvAdapter(response.body().getData().getDetail()));
                } else {
                    showDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });


        Call<AllDeals> getAllFeaturedDealsCall = promoAnalyticsServices.getAllDeals("", "30.7360306", "76.7328649", AppConstants.UNFEATURED_DEALS, AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), "1");
        getAllFeaturedDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                pDialog.hide();
                if (response.body().getStatus()) {

                    Log.d("RETRO_GETALLDEALS", response.body().getStatus() + "");

                    Log.d("RETRO_GETALLDEALS", String.valueOf(response.body().getData().getDetail()));
                    Intent intent = new Intent(getActivity(), HomeFragment.class);
                    intent.putParcelableArrayListExtra("LIST_DEALS", (ArrayList<? extends Parcelable>) response.body().getData().getDetail());

                    fragmentHomeNewBinding.rvNormalCoupons.setAdapter(new AllDealsRvAdapter(response.body().getData().getDetail()));
                } else {
                    showDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });

        Call<CategoryModel> categoryModelCall = promoAnalyticsServices.getCategories();
        categoryModelCall.enqueue(new Callback<CategoryModel>() {


            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.body().getStatus()) {

                    ArrayList<String> categoryList = new ArrayList<String>();
                    for (Datum datum : response.body().getData()) {
                        categoryList.add(datum.getName());
                    }
                    fragmentHomeNewBinding.searchLayout.autoCategorySearch.setAdapter(new ArrayAdapter<>
                            (getActivity(), R.layout.autocomplete_layout, categoryList));
                }

            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });


        return fragmentHomeNewBinding.getRoot();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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
                 /*   FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    // Store the Fragment in stack
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, CouponDetailFragment.newInstance("", "")).commitAllowingStateLoss();
*/
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
