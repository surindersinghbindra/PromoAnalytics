package com.promoanalytics.adapter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.promoanalytics.R;
import com.squareup.picasso.Picasso;

/**
 * Created by think360 on 01/04/17.
 */

public class BindingHelper {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl))
            Picasso.with(view.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.profilepic)
                    .into(view);
    }
}
