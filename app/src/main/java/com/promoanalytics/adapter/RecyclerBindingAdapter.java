package com.promoanalytics.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.promoanalytics.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by troy379 on 16.03.16.
 */
public class RecyclerBindingAdapter<T> extends RecyclerView.Adapter<RecyclerBindingAdapter.BindingHolder> {

    private int holderLayout, variableId;
    private List<T> items = new ArrayList<>();
    private OnItemClickListener<T> onItemClickListener;

    private int itemPosition;

    public RecyclerBindingAdapter(int holderLayout, int variableId, List<T> items) {
        this.holderLayout = holderLayout;
        this.variableId = variableId;
        this.items = items;
    }

    @Override
    public RecyclerBindingAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(holderLayout, parent, false);
        return new BindingHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerBindingAdapter.BindingHolder holder, int position) {
        final T item = items.get(position);


        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(holder.getAdapterPosition(), item);


            }
        });
        RadioButton radioButton = (RadioButton) (holder.getBinding().getRoot()).findViewById(R.id.radioButton);

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (onItemClickListener != null)
                            onItemClickListener.onItemClick(holder.getAdapterPosition(), item);
                    }
                }, 350);

            }
        });


      /*  holder.getBinding().getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(position, item);
        });*/
        holder.getBinding().setVariable(variableId, item);
        holder.getBinding().executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public BindingHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }

}
