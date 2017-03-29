package com.promoanalytics.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.promoanalytics.R;

/**
 * Created by think360 on 28/03/17.
 */

public class DealViewHolder extends RecyclerView.ViewHolder {


    public CardView cvLayout;
    public ImageView ivDeal, ivHeart;
    public TextView tvDiscount, tvDealDetail;

    public DealViewHolder(View itemView) {
        super(itemView);
        ivDeal = (ImageView) itemView.findViewById(R.id.ivDeal);
        ivHeart = (ImageView) itemView.findViewById(R.id.ivHeart);
        tvDiscount = (TextView) itemView.findViewById(R.id.tvDiscount);
        tvDealDetail = (TextView) itemView.findViewById(R.id.tvDealDetail);
        cvLayout = (CardView) itemView.findViewById(R.id.cvLayout);

    }
}
