package com.promoanalytics.activity;

/**
 * Created by think360user on 15/3/17.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.promoanalytics.R;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    String id, name, logo, discount, description, is_fav, image, detail, address, valid, code;
    private CoordinatorLayout mCLayout;
    private FloatingActionButton mFAB;

    ImageView image_view;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    TextView addresses;
    TextView descrptn, validity, cpncode, dscnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpndetails);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        logo = getIntent().getStringExtra("logo");
        discount = getIntent().getStringExtra("discount");
        description = getIntent().getStringExtra("description");
        is_fav = getIntent().getStringExtra("is_fav");
        image = getIntent().getStringExtra("image");
        detail = getIntent().getStringExtra("detail");
        address = getIntent().getStringExtra("address");
        valid = getIntent().getStringExtra("valid");
        code = getIntent().getStringExtra("code");
        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Get the widget reference from XML layout
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        image_view = (ImageView) findViewById(R.id.image_view);
        addresses = (TextView) findViewById(R.id.addresses);
        descrptn = (TextView) findViewById(R.id.descrptn);
        validity = (TextView) findViewById(R.id.validity);
        cpncode = (TextView) findViewById(R.id.cpncode);
        dscnt = (TextView) findViewById(R.id.dscnt);
        dscnt.setText(discount);
        validity.setText("Valid From: " + valid);
        cpncode.setText(code);
        addresses.setText("Address: " + address);
        descrptn.setText(detail);
        Picasso.with(mContext).load(image)
                .error(R.drawable.profilepic).
                placeholder(R.drawable.profilepic)
                .into(image_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        setSupportActionBar(mToolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set a title for collapsing toolbar layout
        mCollapsingToolbarLayout.setTitle(name);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        // Set a click listener for Floating Action Button
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new Snackbar instance
                Snackbar snackbar = Snackbar.make(
                        mCLayout,
                        "This is a Snackbar",
                        Snackbar.LENGTH_LONG);
                // Display the Snackbar
                snackbar.show();
            }
        });
    }


}
