package com.promoanalytics.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.promoanalytics.model.Category.Datum;

import java.util.List;

/**
 * Created by think360 on 29/03/17.
 */

public class CategoryAutoCompeleteAdapter extends ArrayAdapter<Datum> implements Filterable {

    private List<Datum> datumArrayList;


    public CategoryAutoCompeleteAdapter(@NonNull Context context, @NonNull List<Datum> objects) {
        super(context, android.R.layout.simple_expandable_list_item_1, android.R.id.text1, objects);
        this.datumArrayList = objects;


    }

    /**
     * Returns the number of results received in the last autocomplete query.
     */
    @Override
    public int getCount() {
        return datumArrayList.size();
    }

    /**
     * Returns an item from the last autocomplete query.
     */
    @Override
    public Datum getItem(int position) {
        return datumArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        Datum item = getItem(position);

        TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
        textView1.setText(item.getName());
        return row;
    }

}
