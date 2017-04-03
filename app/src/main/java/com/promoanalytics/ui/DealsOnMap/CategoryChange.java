package com.promoanalytics.ui.DealsOnMap;

import com.promoanalytics.model.Category.Datum;

/**
 * Created by surinder on 02-Apr-17.
 */

public class CategoryChange {

    public final Datum datum;
    public final int whoWillBetheListner;

    public CategoryChange(Datum datum, int whoWillBetheListner) {
        this.datum = datum;
        this.whoWillBetheListner = whoWillBetheListner;


    }

}
