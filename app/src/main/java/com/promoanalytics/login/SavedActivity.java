package com.promoanalytics.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.promoanalytics.R;
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.post.GetAllDealsCpn;
import com.promoanalytics.post.SavedList;


/**
 * Created by think360user on 17/3/17.
 */

public class SavedActivity extends Activity{
    ExpandableHeightGridView gridviews;
    LinearLayout saveddetails;
    LinearLayout profile,list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_details);
        gridviews=(ExpandableHeightGridView)findViewById(R.id.myIds);

        list=(LinearLayout)findViewById(R.id.list);
        profile=(LinearLayout)findViewById(R.id.profile);
        saveddetails=(LinearLayout) findViewById(R.id.saveddetails);
        setupSimpleList2();
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllDealsCpn getAllDealsCpn=new GetAllDealsCpn(SavedActivity.this);
                getAllDealsCpn.addQueue();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SavedActivity.this,EditProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
    private void setupSimpleList2() {
        SavedList savedList=new SavedList(SavedActivity.this,gridviews);
        savedList.addQueue();

    }
}
