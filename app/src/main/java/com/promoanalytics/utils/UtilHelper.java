package com.promoanalytics.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;

import com.promoanalytics.BR;
import com.promoanalytics.R;
import com.promoanalytics.adapter.RecyclerBindingAdapter;
import com.promoanalytics.model.Category.Datum;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by think360 on 22/03/17.
 */

public class UtilHelper {

    private static String TAG = UtilHelper.class.getSimpleName();

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static HashMap getFacebookData(JSONObject object) {

        try {
            HashMap<String, String> hasMap = new HashMap<>();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                hasMap.put("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            hasMap.put("idFacebook", id);
            if (object.has("first_name"))
                hasMap.put("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                hasMap.put("last_name", object.getString("last_name"));
            if (object.has("email"))
                hasMap.put("email", object.getString("email"));
            if (object.has("gender"))
                hasMap.put("gender", object.getString("gender"));
            if (object.has("birthday"))
                hasMap.put("birthday", object.getString("birthday"));
            if (object.has("location"))
                hasMap.put("location", object.getJSONObject("location").getString("name"));

            return hasMap;
        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
        }
        return null;

    }

    public static void animateOverShoot(View view) {
        //view.getChildAt(0).setScaleY(0);
        view.setScaleX(0);
        view.setScaleY(0);
        view.animate()
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(new OvershootInterpolator())
                .setDuration(150)
                .start();
    }


    public static void showMyDialog(Context context, List<Datum> datumList) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new RecyclerBindingAdapter(R.layout.single_item_category, BR.category, datumList));

        // BusProvider.getInstance().post(produceCategoryChangeEvent());


        /**
         * if you want the dialog to be specific size, do the following
         * this will cover 85% of the screen (85% width and 85% height)
         */
      /*  DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);*/

        dialog.show();
    }




}
