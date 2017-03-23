package com.promoanalytics.post;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.promoanalytics.activity.Home_Cpn;
import com.promoanalytics.activity.MainActivity;
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
public class DealDetail {

    StringRequest sr;



    public DealDetail(final Context context,final String dealid)
    {




        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        SharedPreferences prefs =context.getSharedPreferences(BaseUrlCPn.prfs, Context.MODE_PRIVATE);
      final String userid = prefs.getString("userid", "");

        sr = new StringRequest(Request.Method.POST, BaseUrlCPn.BASE_URL+BaseUrlCPn.deal_detail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                try{
                    final JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if(status.equals("true")){



                        JSONObject object2= jsonObject.getJSONObject("data");






                                String name;
                                String logo;
                                String discount;
                                String description;
                                String is_fav;
                                String id;
                                String image;
                                String detail;
                                String address;
                                String valid;
                                String code;
                                id = object2.getString("id");
                        image = object2.getString("image");
                        detail = object2.getString("detail");
                        address = object2.getString("address");
                                name = object2.getString("name");
                                logo = object2.getString("logo");
                                is_fav = object2.getString("is_fav");
                                discount = object2.getString("discount");
                                description = object2.getString("description");
                                valid = object2.getString("valid");
                                code = object2.getString("code");




                        Intent intent=new Intent(context,MainActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("name",name);
                        intent.putExtra("logo",logo);
                        intent.putExtra("discount",discount);
                        intent.putExtra("description",description);
                        intent.putExtra("is_fav",is_fav);
                        intent.putExtra("image",image);
                        intent.putExtra("detail",detail);
                        intent.putExtra("address",address);
                        intent.putExtra("valid",valid);
                        intent.putExtra("code",code);
                        context.startActivity(intent);




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

                params.put("deal_id",dealid);
                params.put("user_id",userid);

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