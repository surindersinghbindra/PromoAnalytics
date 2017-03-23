package com.promoanalytics.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.promoanalytics.R;
import com.promoanalytics.post.GetAllDealsCpn;


/**
 * Created by think360user on 17/3/17.
 */

public class EditProfile extends Activity

{

    LinearLayout saved,list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        saved=(LinearLayout)findViewById(R.id.saved);
        list=(LinearLayout)findViewById(R.id.list);
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditProfile.this,SavedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllDealsCpn getAllDealsCpn=new GetAllDealsCpn(EditProfile.this);
                getAllDealsCpn.addQueue();
            }
        });
    }
}
