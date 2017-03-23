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
public class GetAllDealsCpn {

    StringRequest sr;


    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> color1 = new ArrayList<>();
    ArrayList<String> id1 = new ArrayList<>();
    ArrayList<String> discount1 = new ArrayList<>();
    ArrayList<String> description1 = new ArrayList<>();
    ArrayList<String> is_fav1 = new ArrayList<>();
    MyApplication global;
    Activity act;

    ExpandableHeightGridView gridviews;
    String userid12;

    public GetAllDealsCpn(final Activity context) {
        act = context;


        global = (MyApplication) context.getApplication();
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        SharedPreferences prefs = context.getSharedPreferences(BaseUrlCPn.prfs, Context.MODE_PRIVATE);
        final String userid = prefs.getString("userid", "");

        sr = new StringRequest(Request.Method.POST, BaseUrlCPn.BASE_URL + BaseUrlCPn.get_values, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                try {
                    final JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("true")) {


                        JSONObject aa = jsonObject.getJSONObject("data");
                        JSONArray array1 = aa.getJSONArray("detail");


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


                        Intent intent = new Intent(context, Home_Cpn.class);
                        intent.putStringArrayListExtra("id1", id1);
                        intent.putStringArrayListExtra("name1", name1);
                        intent.putStringArrayListExtra("color1", color1);
                        intent.putStringArrayListExtra("discount1", discount1);
                        intent.putStringArrayListExtra("description1", description1);
                        intent.putStringArrayListExtra("is_fav1", is_fav1);
                        context.startActivity(intent);


                    } else if (status.equals("false")) {
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
                } else if (error instanceof NoConnectionError) {
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
                } else {
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("latitude", String.valueOf(30.7360306));
                params.put("longitude", String.valueOf(76.7328649));
                params.put("feature", "0");
                params.put("user_id", userid);

                return params;
            }

        };
    }

    public void addQueue() {
        //Volley.getInstance(this).addToRequestQueue(strreq);
        MyApplication.getInstance().addToRequestQueue(sr);

        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



  /*  private void populatRecyclerView()
    {

        tripsAdapter=new TripsAdapter(act,name1,color1,discount1,description1,id1);
        gridviews.setAdapter(tripsAdapter);
        gridviews.setExpanded(true);
        tripsAdapter.notifyDataSetChanged();

    }
    // Implement scroll listener
    private void implementScrollListener()
    {
        gridviews.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            private int mLastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState)
            {
                // If scroll state is touch scroll then set userScrolled == true
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    userScrolled = true;
                    updateListView();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount)
            {
                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    Log.i("SCROLLING DOWN","TRUE");
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {

                    Log.i("SCROLLING UP","TRUE");
                }
                mLastFirstVisibleItem=firstVisibleItem;

            }
        });
    }*/
  /*  // Method for repopulating recycler view
    private void updateListView()
    {
        // Show Progress Layout
        //bottomLayout.setVisibility(View.VISIBLE);

        // Handler to show refresh for a period of time you can use async task
        // while commnunicating serve

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {String checkOrigin = .getNextpage();

                if(checkOrigin.equals("0"))
                {
                    txt.setText("No More items");
                    more_progress.setVisibility(View.GONE);
                    //new ModuleDetails(ListViewLayout_Activity.this,id,catname,checkOrigin,listView,seeall_adapter,Name,descriptions,price,ids,pics).addQueue();
                    bottomLayout.setVisibility(View.VISIBLE);
                    //btnAddtoShoppingList.setVisibility(View.GONE);
                    //btnDeleteShoppingList.setVisibility(View.VISIBLE);
                }
                else
                {
                    new ModuleDetails(ListViewLayout_Activitys.this,id,catname,checkOrigin,listView,Name,descriptions,price,ids,pics).addQueue();
                    Log.i("","");
                    //listView.setStackFromBottom(true);
                    //	populatRecyclerView();

                    bottomLayout.setVisibility(View.GONE);
                }



            }
        }, 5000);
    }*/
}