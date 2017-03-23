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
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.modules.MyApplication;
import com.promoanalytics.utils.BaseUrlCPn;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by think360user on 28/12/16.
 */
public class UserfavSaved {

    StringRequest sr;

    MyApplication global;



    public UserfavSaved(final Context context, final String dealid, final String satsvalue, final ExpandableHeightGridView gridView)
    {



        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        SharedPreferences prefs =context.getSharedPreferences(BaseUrlCPn.prfs, Context.MODE_PRIVATE);
      final String userid = prefs.getString("userid", "");


        sr = new StringRequest(Request.Method.POST, BaseUrlCPn.BASE_URL+BaseUrlCPn.user_fav, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.hide();
                try{
                    final JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if(status.equals("true")){

                        SavedList savedList=new SavedList(context,gridView);
                        savedList.addQueue();




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
                params.put("deal_id",dealid );
                params.put("status",satsvalue);

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