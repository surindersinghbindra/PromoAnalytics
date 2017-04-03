package com.promoanalytics.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.promoanalytics.BR;
import com.promoanalytics.R;
import com.promoanalytics.adapter.RecyclerBindingAdapter;
import com.promoanalytics.model.Category.CategoryModel;
import com.promoanalytics.model.Category.Datum;
import com.promoanalytics.ui.DealsOnMap.CategoryChange;
import com.promoanalytics.utils.BusProvider;
import com.promoanalytics.utils.PromoAnalyticsServices;
import com.squareup.otto.Produce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by surinder on 02-Apr-17.
 */

public class CategoryDialogFragment extends DialogFragment implements View.OnClickListener, RecyclerBindingAdapter.OnItemClickListener {


    private Datum datum = new Datum();
    private int whoWillBetheListner;

    public static CategoryDialogFragment newInstance() {

        return new CategoryDialogFragment();
    }

    public void setWhoWillBetheListner(int whoWillBetheListner) {
        this.whoWillBetheListner = whoWillBetheListner;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_dialog, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View dialog, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(dialog, savedInstanceState);


        final RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(dialog.getContext()));

        PromoAnalyticsServices promoAnalyticsServices = PromoAnalyticsServices.retrofit.create(PromoAnalyticsServices.class);
        Call<CategoryModel> categoryModelCall = promoAnalyticsServices.getCategories();
        categoryModelCall.enqueue(new Callback<CategoryModel>() {


            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.body().getStatus()) {
                    RecyclerBindingAdapter recyclerBindingAdapter = new RecyclerBindingAdapter<Datum>(R.layout.single_item_category, BR.category, response.body().getData());
                    recyclerView.setAdapter(recyclerBindingAdapter);

                    recyclerBindingAdapter.setOnItemClickListener(CategoryDialogFragment.this);

                    // fragmentDealsOnMapBinding.searchLayout.autoCategorySearch.setAdapter(mCategoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Produce
    public CategoryChange produceCategoryChangeEvent() {
        // Provide an initial value for location based on the last known position.
        return new CategoryChange(datum, whoWillBetheListner);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onItemClick(int position, Object item) {

        this.datum = (Datum) item;

        BusProvider.getInstance().post(produceCategoryChangeEvent());

    }
}
