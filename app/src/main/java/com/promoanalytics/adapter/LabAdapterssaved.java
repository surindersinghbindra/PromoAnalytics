package com.promoanalytics.adapter;

/**
 * Created by think360user on 20/2/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.promoanalytics.R;
import com.promoanalytics.modules.ExpandableHeightGridView;
import com.promoanalytics.post.DealDetail;
import com.promoanalytics.post.Userfav;
import com.promoanalytics.post.UserfavSaved;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LabAdapterssaved extends BaseAdapter{
    private Context mContext;
    ArrayList<String> name1=new ArrayList<>();
    ArrayList<String> color1=new ArrayList<>();
String  statusvalue;
    String dealid;
    ExpandableHeightGridView gridViews;
    ArrayList<String> id1=new ArrayList<>();
    ArrayList<String> discount1=new ArrayList<>();
    ArrayList<String> is_fav1=new ArrayList<>();
    ArrayList<String> description1=new ArrayList<>();

    public LabAdapterssaved(Context c, ArrayList<String> name, ArrayList<String> color, ArrayList<String> id,
                            ArrayList<String> discount, ArrayList<String> description, ArrayList<String> is_fav,
                            ExpandableHeightGridView gridView) {
        mContext = c;

        name1=name;
        color1=color;
        id1=id;
        gridViews=gridView;
        description1=description;
        discount1=discount;
        is_fav1=is_fav;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return id1.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpnadapter, null);
        TextView textView = (TextView) retval.findViewById( R.id.grid_text);
        TextView textView1 = (TextView) retval.findViewById(R.id.lkjk);
        ImageView imgs=(ImageView)retval.findViewById(R.id.imgs);
        final ImageView hrtunfled=(ImageView)retval.findViewById(R.id.hrtunfled);
        final ImageView hrtfilled=(ImageView)retval.findViewById(R.id.hrtfilled);
        LinearLayout cpndetails=(LinearLayout)retval.findViewById(R.id.cpndetails) ;
        if(is_fav1.get(position).equals("1")){
            hrtfilled.setVisibility(View.VISIBLE);
            hrtunfled.setVisibility(View.GONE);
        }else{
            hrtfilled.setVisibility(View.GONE);
            hrtunfled.setVisibility(View.VISIBLE);
        }
        hrtfilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               dealid= id1.get(position);
                statusvalue="0";
                UserfavSaved userfav=new UserfavSaved(mContext,dealid,statusvalue,gridViews);
                userfav.addQueue();
            }
        });
        hrtunfled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                statusvalue="1";
                dealid= id1.get(position);
                Userfav userfav=new Userfav(mContext,dealid,statusvalue,hrtfilled,hrtunfled);
                userfav.addQueue();
            }
        });
        textView.setText(discount1.get(position));
        textView1.setText(name1.get(position));
        Picasso.with(mContext).load(color1.get(position))
                .error(R.drawable.profilepic).
                placeholder(R.drawable.profilepic)
                .into(imgs);
        cpndetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealid= id1.get(position);
                DealDetail dealDetail=new DealDetail(mContext,dealid);
                dealDetail.addQueue();

            }
        });
        return retval;
    }


}
