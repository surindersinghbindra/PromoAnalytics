package com.promoanalytics.ui.DealsOnMap;

import com.promoanalytics.model.Category.Datum;

/**
 * Created by surinder on 02-Apr-17.
 */

public class CategoryChange {

    public final Datum datum;


    public CategoryChange(Datum datum) {
        this.datum = datum;

    }

    @Override
    public String toString() {
        return new StringBuilder("(") //
                .append(datum.getName()) //
                .append(", ") //
                .append(")") //
                .toString();
    }
}
