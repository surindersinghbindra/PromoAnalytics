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
import com.promoanalytics.model.Detail;
import com.promoanalytics.post.DealDetail;
import com.promoanalytics.post.Userfav;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LabAdapters extends BaseAdapter {
    private Context mContext;
    private ArrayList<Detail> detailArrayList;

    public LabAdapters(Context c, ArrayList<Detail> detailArrayList) {
        mContext = c;
        this.detailArrayList = detailArrayList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return detailArrayList.size();
    }

    @Override
    public Detail getItem(int position) {
        // TODO Auto-generated method stub
        return detailArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cpnadapter, null);
            holder = new ViewHolder();

            holder.textView = (TextView) convertView.findViewById(R.id.grid_text);
            holder.textView1 = (TextView) convertView.findViewById(R.id.lkjk);
            holder.imgs = (ImageView) convertView.findViewById(R.id.imgs);
            holder.cpndetails = (LinearLayout) convertView.findViewById(R.id.cpndetails);
            holder.hrtunfled = (ImageView) convertView.findViewById(R.id.hrtunfled);
            holder.hrtfilled = (ImageView) convertView.findViewById(R.id.hrtfilled);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Detail item = getItem(position);

        if (item.getIsFav().equals("1")) {
            holder.hrtfilled.setVisibility(View.VISIBLE);
            holder.hrtunfled.setVisibility(View.GONE);
        } else {
            holder.hrtfilled.setVisibility(View.GONE);
            holder.hrtunfled.setVisibility(View.VISIBLE);
        }
        final ViewHolder finalHolder = holder;
        holder.hrtfilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String statusvalue = "0";
                Userfav userfav = new Userfav(mContext, item.getId() + "", statusvalue, finalHolder.hrtfilled, finalHolder.hrtunfled);
                userfav.addQueue();
            }
        });
        holder.hrtunfled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String statusvalue = "1";

                Userfav userfav = new Userfav(mContext, item.getId() + "", statusvalue, finalHolder.hrtfilled, finalHolder.hrtunfled);
                userfav.addQueue();
            }
        });
        holder.textView.setText(item.getDiscount());
        holder.textView1.setText(item.getName());
        Picasso.with(mContext).load(item.getCategoryPic())
                .error(R.drawable.profilepic).
                placeholder(R.drawable.profilepic)
                .into(finalHolder.imgs);
        holder.cpndetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DealDetail dealDetail = new DealDetail(mContext, item.getId());
                dealDetail.addQueue();

            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView textView, textView1;
        ImageView imgs, hrtunfled, hrtfilled;
        LinearLayout cpndetails;
    }


}
