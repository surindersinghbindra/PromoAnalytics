package com.promoanalytics.ui.DealsOnMap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spanned;
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
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.promoanalytics.FetchAddressIntentService;
import com.promoanalytics.R;
import com.promoanalytics.databinding.FragmentDealsOnMapBinding;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.model.AllDeals.Detail;
import com.promoanalytics.model.Category.Datum;
import com.promoanalytics.model.SearchLayoutModel;
import com.promoanalytics.ui.CategoryNameCallBack;
import com.promoanalytics.ui.TabChangedOtto;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.BusProvider;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.squareup.otto.Produce;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DealsOnMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DealsOnMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DealsOnMapFragment extends RootFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, CategoryNameCallBack {
    public static final String TAG = DealsOnMapFragment.class.getSimpleName();

    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int tabSelected = 0;
    public static Detail mDetail;
    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     * The user requests an address by pressing the Fetch Address button. This may happen
     * before GoogleApiClient connects. This activity uses this boolean to keep track of the
     * user's intent. If the value is true, the activity tries to fetch the address as soon as
     * GoogleApiClient connects.
     */
    protected boolean mAddressRequested;
    /**
     * The formatted location address.
     */
    protected String mAddressOutput;
    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;
    private CategoryDialogFragmentFromMaps editNameDialogFragment;
    private LatLng latLng = new LatLng(43.717899, -79.658251);
    private String categoryId = "";
    private String currentLocation = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Location mLastLocation;
    private FragmentDealsOnMapBinding fragmentDealsOnMapBinding;
    private GoogleMap googleMap = null;
    private boolean isMapReady = false;
    private boolean isAlreadyFetchedDataForCurrentLocation = true;
    private GoogleApiClient mGoogleApiClient;
    private PromoAnalyticsServices promoAnalyticsServices;
    private FragmentManager fm;
    private SearchLayoutModel searchLayoutModel;


    public DealsOnMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DealsOnMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DealsOnMapFragment newInstance(String param1, String param2) {
        DealsOnMapFragment fragment = new DealsOnMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), 1, this)
                    .addConnectionCallbacks(DealsOnMapFragment.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).addApi(Places.GEO_DATA_API)
                    .build();
        }

        mResultReceiver = new AddressResultReceiver(new Handler());

        // Inflate the layout for this fragment
        fragmentDealsOnMapBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_deals_on_map, container, false);


        searchLayoutModel = new SearchLayoutModel("Searching..", "Select Location", "Select Category", false);

        fragmentDealsOnMapBinding.searchLayout.setData(searchLayoutModel);
        fragmentDealsOnMapBinding.searchLayout.ivResetCategory.setOnClickListener(this);


        fragmentDealsOnMapBinding.tbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentDealsOnMapBinding.searchLayout.slectn.getVisibility() == View.GONE)
                    fragmentDealsOnMapBinding.searchLayout.slectn.setVisibility(View.VISIBLE);
                else
                    fragmentDealsOnMapBinding.searchLayout.slectn.setVisibility(View.GONE);
            }
        });

        fragmentDealsOnMapBinding.mvDealsMap.onCreate(savedInstanceState);
        fragmentDealsOnMapBinding.mvDealsMap.getMapAsync(DealsOnMapFragment.this);


        // Register a listener that receives callbacks when a suggestion has been selected
        fragmentDealsOnMapBinding.searchLayout.autoCategorySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editNameDialogFragment = CategoryDialogFragmentFromMaps.newInstance();
                editNameDialogFragment.setCallBack(DealsOnMapFragment.this);
                editNameDialogFragment.show(fm, "fragment_edit_name");

            }
        });

        fragmentDealsOnMapBinding.searchLayout.autoCompleteLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });


        return fragmentDealsOnMapBinding.getRoot();
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        this.isMapReady = true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        fm = getChildFragmentManager();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentDealsOnMapBinding.mvDealsMap.onResume();
        BusProvider.getInstance().register(this);
    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        fragmentDealsOnMapBinding.mvDealsMap.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        fragmentDealsOnMapBinding.mvDealsMap.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        fragmentDealsOnMapBinding.mvDealsMap.onPause();
        // Always unregister when an object no longer should be on the bus.
        BusProvider.getInstance().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        fragmentDealsOnMapBinding.mvDealsMap.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        fragmentDealsOnMapBinding.mvDealsMap.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if (isMapReady && googleMap != null) {
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                if (isAlreadyFetchedDataForCurrentLocation) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, AppConstants.ZOOM_LEVEL_IN_APP);
                    googleMap.animateCamera(cameraUpdate);
                    fetchDataFromRemote("", latLng);
                    isAlreadyFetchedDataForCurrentLocation = false;
                }

                // We only start the service to fetch the address if GoogleApiClient is connected.
                if (mGoogleApiClient.isConnected() && mLastLocation != null) {
                    startIntentService();
                }
                cameraZoom(latLng, "Current Location");
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

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppConstants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    private void fetchDataFromRemote(@NonNull final String categoryIdToFetch, @NonNull final LatLng latLng) {

        promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
        Call<AllDeals> allDealsCall = promoAnalyticsServices.getAllDealsOnMap(TextUtils.isEmpty(categoryIdToFetch) ? null : categoryIdToFetch, latLng.latitude + "", latLng.longitude + "");
        allDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                if (response.isSuccessful()) {

                    if (response.body().getStatus()) {

                        if (response.body().getData().getDetail().size() > 0) {
                            LatLng latLng1 = new LatLng(Double.parseDouble(response.body().getData().getDetail().get(0).getLatitude()), Double.parseDouble(response.body().getData().getDetail().get(0).getLongitude()));
                            cameraZoom(latLng1, categoryIdToFetch);
                            addMarkersToMap(response.body().getData().getDetail());
                        } else {

                            showMessageInSnackBar(response.body().getMessage());
                        }
                    } else {
                        googleMap.clear();
                        showMessageInSnackBar(response.body().getMessage() + " " + (!TextUtils.isEmpty(currentLocation) && (currentLocation != null) ? currentLocation : ""));
                    }
                } else {
                    showMessageInSnackBar("Please try again");
                }


            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });

    }


    void cameraZoom(LatLng latLng, String placeName) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, AppConstants.ZOOM_LEVEL_IN_APP);
        googleMap.animateCamera(cameraUpdate);

    }

    //adding markers to map after clearing the map
    void addMarkersToMap(List<Detail> allDealsList) {
        googleMap.clear();
        for (final Detail item : allDealsList) {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    LatLng latLng = new LatLng(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));

                    Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap)).title(item.getName() + "," + item.getCategoryName()));
                    marker.setTag(item);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
            Picasso.with(getActivity()).load(item.getCategoryPic()).resize(96, 96).into(target);


        }
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Detail detail = (Detail) marker.getTag();
                assert detail != null;
                Log.i("Clicked", detail.getName());
                tabSelected = R.id.tab_list;
                mDetail = detail;
                BusProvider.getInstance().post(produceTabChange());


            }
        });


    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
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

/*    void hideKeyBoard() {
        // hide leyboard on location selected
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }*/

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place Selected: " + place.getName());

                tabSelected = 0;
                this.currentLocation = place.getName() + "";
                // Format the place's details and display them in the TextView.
                fragmentDealsOnMapBinding.etSearchPlaceOrCategory.setText(place.getName());
                fragmentDealsOnMapBinding.searchLayout.autoCompleteLocationSearch.setText(place.getName());

                latLng = place.getLatLng();

                fragmentDealsOnMapBinding.searchLayout.slectn.setVisibility(View.GONE);
                fetchDataFromRemote(categoryId, latLng);


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivResetCategory:
                searchLayoutModel.setCategorySearchTitle("Select Category");
                searchLayoutModel.setOnAllCategory(false);
                fetchDataFromRemote("", latLng);
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Produce
    public TabChangedOtto produceTabChange() {
        // Provide an initial value for location based on the last known position.
        return new TabChangedOtto(tabSelected, mDetail, currentLocation);
    }

    @Override
    public void sendMeBackCategoryIdAndName(Datum datum) {

        if (datum != null) {
            editNameDialogFragment.dismiss();
            searchLayoutModel.setOnAllCategory(true);
            searchLayoutModel.setCategorySearchTitle(datum.getName());
            this.categoryId = datum.getId();
            fragmentDealsOnMapBinding.searchLayout.slectn.setVisibility(View.GONE);
            fetchDataFromRemote(datum.getId(), latLng);
            Log.i("CATEGORY_ID", datum.getName());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppConstants.RESULT_DATA_KEY);
            //  displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppConstants.SUCCESS_RESULT) {

                currentLocation = mAddressOutput;
                fragmentDealsOnMapBinding.etSearchPlaceOrCategory.setText(mAddressOutput);
                // showToast(getString(R.string.address_found));


            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;

        }
    }

}
