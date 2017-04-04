package com.promoanalytics.ui.SavedCoupons;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.promoanalytics.R;
import com.promoanalytics.adapter.DealViewHolder;
import com.promoanalytics.databinding.HomecpnBinding;
import com.promoanalytics.model.AllDeals.AllDeals;
import com.promoanalytics.model.AllDeals.Detail;
import com.promoanalytics.model.SaveDealModel;
import com.promoanalytics.ui.CouponDetailFragment;
import com.promoanalytics.utils.AppConstants;
import com.promoanalytics.utils.AppController;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.promoanalytics.utils.RootFragment;
import com.promoanalytics.utils.UtilHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by think360user on 15/3/17.
 */

public class SavedDealsFragment extends RootFragment implements LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static List<Detail> detailArrayList = new ArrayList<Detail>();
    public static AllSavedDealsRvAdapter allSavedDealsRvAdapter;
    double currentLatitude, currentLongitude;
    Location location;
    private HomecpnBinding homecpnBinding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mUnFeaturedDealsRecyclerView;
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
    public static SavedDealsFragment newInstance(String param1, String param2) {
        SavedDealsFragment fragment = new SavedDealsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void updateReceiptsList(Detail obj) {
        detailArrayList.add(obj);
        allSavedDealsRvAdapter.notifyDataSetChanged();
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

        view = inflater.inflate(R.layout.fragment_saved_deals, container, false);
        mUnFeaturedDealsRecyclerView = (RecyclerView) view.findViewById(R.id.rvSavedCoupons);
        mUnFeaturedDealsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        allSavedDealsRvAdapter = new AllSavedDealsRvAdapter(detailArrayList);
        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);

        showProgressBar();
        Call<AllDeals> allDealsCall = promoAnalyticsServices.getSavedCoupons(AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, "0"));
        allDealsCall.enqueue(new Callback<AllDeals>() {
            @Override
            public void onResponse(Call<AllDeals> call, Response<AllDeals> response) {

                pDialog.hide();
                if (response.body().getStatus()) {

                    //  allSavedDealsRvAdapter = new AllSavedDealsRvAdapter(response.body().getData().getDetail());

                    detailArrayList.addAll(response.body().getData().getDetail());
                    mUnFeaturedDealsRecyclerView.setAdapter(allSavedDealsRvAdapter);
                    allSavedDealsRvAdapter.notifyDataSetChanged();
                } else {
                    showDialog(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AllDeals> call, Throwable t) {

            }
        });

        return view;

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    private class AllSavedDealsRvAdapter extends RecyclerView.Adapter<DealViewHolder> {

        private ArrayList<Detail> arrayListDeals;

        public AllSavedDealsRvAdapter(List<Detail> arrayList) {
            this.arrayListDeals = (ArrayList<Detail>) arrayList;

        }


        @Override
        public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_item_deal, parent, false);
            return new DealViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DealViewHolder holder, final int position) {
            final Detail singleDeal = arrayListDeals.get(position);

            if (singleDeal.getIsFav() != 0) {
                holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));
            } else {
                holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.hrtunfilled));
            }

            holder.tvDiscount.setText(String.format("%s%s OFF", singleDeal.getDiscount(), "%"));
            holder.tvDealDetail.setText(singleDeal.getDescription());
            Picasso.with(getActivity()).load(String.valueOf(arrayListDeals.get(position).getLogo())).placeholder(R.drawable.logo_placeholder).error(R.drawable.logo_placeholder).into(holder.ivDeal);


            holder.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
                    Call<SaveDealModel> saveDealModelCall = promoAnalyticsServices.saveToFavourite(AppController.sharedPreferencesCompat.getString(AppConstants.USER_ID, ""), singleDeal.getId(), 0);
                    saveDealModelCall.enqueue(new Callback<SaveDealModel>() {
                        @Override
                        public void onResponse(Call<SaveDealModel> call, Response<SaveDealModel> response) {
                            if (response.body().getStatus()) {
                                UtilHelper.animateOverShoot(holder.ivHeart);
                                arrayListDeals.remove(position);
                                notifyDataSetChanged();
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

            holder.cvLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trasactFragment(R.id.container, CouponDetailFragment.newInstance(singleDeal.getId(), ""));
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayListDeals.size();
        }
    }
}
