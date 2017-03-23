package com.promoanalytics.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.promoanalytics.R;
import com.promoanalytics.adapter.LabAdapters;
import com.promoanalytics.model.Detail;
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.modules.GPSTracker;
import com.promoanalytics.modules.HorizontalListView;
import com.promoanalytics.post.GetAllDealsCpnfeature;
import com.promoanalytics.post.SavedList;

import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

/**
 * Created by think360user on 15/3/17.
 */

public class Home_Cpn extends Activity implements LocationListener {
    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> color1 = new ArrayList<>();
    ArrayList<String> id1 = new ArrayList<>();
    ArrayList<String> discount1 = new ArrayList<>();
    ArrayList<String> description1 = new ArrayList<>();
    ArrayList<String> is_fav1 = new ArrayList<>();
    ExpandableHeightGridView gridview;
    ExpandableHeightGridView gridviews;
    double currentLatitude, currentLongitude;
    Location location;
    HorizontalListView mHlvSimpleList;
    TwoWayView lvTest;
    LinearLayout list, saved, profile, saveddetails;
    RelativeLayout profiledetails;
    ScrollView listdetails;
    GPSTracker gps;
    TextView locationtext;

    private ArrayList<Detail> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homecpn);


        myList = (ArrayList<Detail>) getIntent().getSerializableExtra("LIST_DEALS");

        Log.e("SIZE", myList.get(myList.size() - 1).getCategoryPic());


        gps = new GPSTracker(Home_Cpn.this);
        if (gps.canGetLocation()) {
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();


        } else {

            gps.showSettingsAlert();
        }
        gridview = (ExpandableHeightGridView) findViewById(R.id.myId);
        gridviews = (ExpandableHeightGridView) findViewById(R.id.myIds);
        list = (LinearLayout) findViewById(R.id.list);
        lvTest = (TwoWayView) findViewById(R.id.lvItems);
        saved = (LinearLayout) findViewById(R.id.saved);
        saveddetails = (LinearLayout) findViewById(R.id.saveddetails);
        locationtext = (TextView) findViewById(R.id.locationtext);
        listdetails = (ScrollView) findViewById(R.id.listdetails);
        profile = (LinearLayout) findViewById(R.id.profile);
        name1 = getIntent().getStringArrayListExtra("name1");
        color1 = getIntent().getStringArrayListExtra("color1");
        is_fav1 = getIntent().getStringArrayListExtra("is_fav1");
        id1 = getIntent().getStringArrayListExtra("id1");
        discount1 = getIntent().getStringArrayListExtra("discount1");
        description1 = getIntent().getStringArrayListExtra("description1");
        mHlvSimpleList = (HorizontalListView) findViewById(R.id.hlvSimpleList);
        locationtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupSimpleList();
        setupSimpleList1();

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdetails.setVisibility(View.VISIBLE);
                saveddetails.setVisibility(View.GONE);
                profiledetails.setVisibility(View.GONE);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Cpn.this, EditProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Cpn.this, SavedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
           /*     setupSimpleList2();
                listdetails.setVisibility(View.GONE);
                saveddetails.setVisibility(View.VISIBLE);
                profiledetails.setVisibility(View.GONE);*/
            }
        });

    }

    private void setupSimpleList() {


        GetAllDealsCpnfeature getAllDealsCpn = new GetAllDealsCpnfeature(Home_Cpn.this, lvTest);
        getAllDealsCpn.addQueue();

    }

    private void setupSimpleList1() {
        LabAdapters adapters = new LabAdapters(Home_Cpn.this, myList);
        gridview.setAdapter(adapters);
        gridview.setExpanded(true);
        adapters.notifyDataSetChanged();
    }

    private void setupSimpleList2() {
        SavedList savedList = new SavedList(Home_Cpn.this, gridviews);
        savedList.addQueue();

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

}
