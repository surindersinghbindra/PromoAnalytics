package com.promoanalytics.post;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.promoanalytics.adapter.LabAdapterssaved;
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.modules.MyApplication;
import com.promoanalytics.utils.BaseUrlCPn;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by think360user on 28/12/16.
 */
public class SavedList {

    StringRequest sr;

    MyApplication global;

    ArrayList<String> name1=new ArrayList<>();
    ArrayList<String> color1=new ArrayList<>();
    ArrayList<String> id1=new ArrayList<>();
    ArrayList<String> discount1=new ArrayList<>();
    ArrayList<String> description1=new ArrayList<>();
    ArrayList<String> is_fav1=new ArrayList<>();

    public SavedList(final Context context, final ExpandableHeightGridView gridView)
    {



        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        SharedPreferences prefs =context.getSharedPreferences(BaseUrlCPn.prfs, Context.MODE_PRIVATE);
      final String userid = prefs.getString("userid", "");


        sr = new StringRequest(Request.Method.POST, BaseUrlCPn.BASE_URL+BaseUrlCPn.fav_list, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                try{
                    final JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if(status.equals("true")){

                        JSONObject aa= jsonObject.getJSONObject("data");
                        JSONArray array1= aa.getJSONArray("detail");


                        for (int i = 0; i < array1.length(); i++) {

                            JSONObject object2 = array1.getJSONObject(i);
                            String name;
                            String color;
                            String discount;
                            String description;
                            String is_fav;
                            String id;
                            id = object2.getString("id");
                            name = object2.getString("name");
                            color = object2.getString("logo");
                            is_fav = object2.getString("is_fav");
                            discount = object2.getString("discount");
                            description = object2.getString("description");

                            name1.add(name);
                            color1.add(color);
                            discount1.add(discount);
                            description1.add(description);
                            id1.add(id);
                            is_fav1.add(is_fav);
                        }


                        LabAdapterssaved adapters = new LabAdapterssaved(context, name1,color1,id1,discount1,description1,is_fav1,gridView);
                        gridView.setAdapter(adapters);
                        adapters.notifyDataSetChanged();

                    }

                  else if(status.equals("false")){
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage(message);
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                if (error instanceof TimeoutError) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("TimeOut Error. Please try again.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else if(error instanceof NoConnectionError){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("No Internet Connection.Please try again");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }

                else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Server Error.Please try again");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
              params.put("user_id", userid);


                return params;
            }

        };
    }
    public void addQueue()
    {
        //Volley.getInstance(this).addToRequestQueue(strreq);
        MyApplication.getInstance().addToRequestQueue(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



}