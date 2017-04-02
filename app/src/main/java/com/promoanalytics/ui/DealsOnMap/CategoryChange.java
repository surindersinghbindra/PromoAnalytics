package com.promoanalytics.ui.DealsOnMap;

/**
 * Created by surinder on 02-Apr-17.
 */

public class CategoryChange {

    public final String categoryId;


    public CategoryChange(String categoryId) {
        this.categoryId = categoryId;

    }

    @Override
    public String toString() {
        return new StringBuilder("(") //
                .append(categoryId) //
                .append(", ") //
                .append(")") //
                .toString();
    }
}
