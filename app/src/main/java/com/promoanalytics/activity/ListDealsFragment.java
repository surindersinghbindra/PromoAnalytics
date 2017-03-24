package com.promoanalytics.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.promoanalytics.AppConstants;
import com.promoanalytics.R;
import com.promoanalytics.adapter.LabAdapters;
import com.promoanalytics.databinding.HomecpnBinding;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.model.AllDeals.Detail;
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.modules.GPSTracker;
import com.promoanalytics.modules.MyApplication;
import com.promoanalytics.post.GetAllDealsCpnfeature;
import com.promoanalytics.post.SavedList;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
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

public class ListDealsFragment extends RootFragment implements LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ExpandableHeightGridView gridview;
    ExpandableHeightGridView gridviews;
    double currentLatitude, currentLongitude;
    Location location;
    TwoWayView lvTest;
    GPSTracker gps;
    private ArrayList<Detail> myList;
    private HomecpnBinding homecpnBinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mFeaturedDealsRecyclerView, mUnFeaturedDealsRecyclerView;
    private View view;

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

        view = inflater.inflate(R.layout.fragment_home_new, container, false);
        mFeaturedDealsRecyclerView = (RecyclerView) view.findViewById(R.id.rvFeaturedCoupons);
        mUnFeaturedDealsRecyclerView = (RecyclerView) view.findViewById(R.id.rvNormalCoupons);


        mFeaturedDealsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mUnFeaturedDealsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);

        showProgressBar();
        Call<AllDeals> getAllDealsCall = promoAnalyticsServices.getAllDeals("", "30.7360306", "76.7328649", AppConstants.FEATURED_DEALS, MyApplication.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), "1");
        getAllDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                pDialog.hide();
                if (response.body().getStatus()) {

                    Log.d("RETRO_GETALLDEALS", response.body().getStatus() + "");

                    Log.d("RETRO_GETALLDEALS", String.valueOf(response.body().getData().getDetail()));
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putParcelableArrayListExtra("LIST_DEALS", (ArrayList<? extends Parcelable>) response.body().getData().getDetail());

                    mFeaturedDealsRecyclerView.setAdapter(new AllDealsRvAdapter(response.body().getData().getDetail()));
                } else {
                    showDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });


        Call<AllDeals> getAllFeaturedDealsCall = promoAnalyticsServices.getAllDeals("", "30.7360306", "76.7328649", AppConstants.UNFEATURED_DEALS, MyApplication.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"), "1");
        getAllFeaturedDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                pDialog.hide();
                if (response.body().getStatus()) {

                    Log.d("RETRO_GETALLDEALS", response.body().getStatus() + "");

                    Log.d("RETRO_GETALLDEALS", String.valueOf(response.body().getData().getDetail()));
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putParcelableArrayListExtra("LIST_DEALS", (ArrayList<? extends Parcelable>) response.body().getData().getDetail());

                    mUnFeaturedDealsRecyclerView.setAdapter(new AllDealsRvAdapter(response.body().getData().getDetail()));
                } else {
                    showDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });


        return view;

        //  homecpnBinding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_home_new);
        /*if (getActivity().getIntent() != null) {
            myList = (ArrayList<Detail>) getActivity().getIntent().getSerializableExtra("LIST_DEALS");
        }*/


        //Log.e("SIZE", myList.get(myList.size() - 1).getCategoryPic());


     /*   gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();


        } else {

            gps.showSettingsAlert();
        }*/
/*        gridview = (ExpandableHeightGridView) findViewById(R.id.myId);
        gridviews = (ExpandableHeightGridView) findViewById(R.id.myIds);
        list = (LinearLayout) findViewById(R.id.list);
        lvTest = (TwoWayView) findViewById(R.id.lvItems);
        saved = (LinearLayout) findViewById(R.id.saved);
        saveddetails = (LinearLayout) findViewById(R.id.saveddetails);
        locationtext = (TextView) findViewById(R.id.locationtext);
        listdetails = (ScrollView) findViewById(R.id.listdetails);
        profile = (LinearLayout) findViewById(R.id.profile);

        mHlvSimpleList = (HorizontalListView) findViewById(R.id.hlvSimpleList);*/
     /*   homecpnBinding.locationtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        //  setupSimpleList();
        //  setupSimpleList1();


        //return homecpnBinding.getRoot();


    }


    private void setupSimpleList() {


        GetAllDealsCpnfeature getAllDealsCpn = new GetAllDealsCpnfeature(getActivity(), lvTest);
        getAllDealsCpn.addQueue();

    }

    private void setupSimpleList1() {
        LabAdapters adapters = new LabAdapters(getActivity(), myList);
        // homecpnBinding.myId.setAdapter(adapters);
        //  homecpnBinding.myId.setExpanded(true);
        adapters.notifyDataSetChanged();
    }

    private void setupSimpleList2() {
        SavedList savedList = new SavedList(getActivity(), gridviews);
        savedList.addQueue();

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    private class AllDealsRvAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private ArrayList<Detail> arrayListDeals;

        public AllDealsRvAdapter(List<Detail> arrayList) {
            this.arrayListDeals = (ArrayList<Detail>) arrayList;

        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_item_deal, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Detail singleDeal = arrayListDeals.get(position);
            holder.singleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

       /*     holder.tv_ProjectName.setText((CharSequence) allProjectsBean.get("projectName"));
            holder.tvSector.setText((CharSequence) allProjectsBean.get("sector"));
            holder.tv_Selected.setText((CharSequence) allProjectsBean.get("numOfSelected"));

            holder.tvPending.setText((String) allProjectsBean.get("NUM"));
*/

            Picasso.with(getActivity()).load(String.valueOf(arrayListDeals.get(position).getLogo())).placeholder(R.drawable.check).into(holder.iv_projectImage);

        }

        @Override
        public int getItemCount() {
            return arrayListDeals.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_projectImage, hrtunfled;
        private TextView grid_text, lkjk;
        private LinearLayout singleItem;

        private TextView tv_ProjectName, tvSector, tv_Selected, tvPending;

        public MyViewHolder(View itemView) {
            super(itemView);
            singleItem = (LinearLayout) itemView.findViewById(R.id.cpndetails);

            iv_projectImage = (ImageView) itemView.findViewById(R.id.ivDeal);
            hrtunfled = (ImageView) itemView.findViewById(R.id.hrtunfled);

            grid_text = (TextView) itemView.findViewById(R.id.grid_text);
            lkjk = (TextView) itemView.findViewById(R.id.lkjk);

        }
    }

}
