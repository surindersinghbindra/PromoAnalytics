package com.promoanalytics.ui;

import com.promoanalytics.model.AllDeals.Detail;

/**
 * Created by think360 on 03/04/17.
 */

public class TabChangedOtto {

    private int tabSelected;


    private String location;
    private Detail detail;


    public TabChangedOtto(int tabSelected, Detail detail, String location) {
        this.tabSelected = tabSelected;
        this.detail = detail;
        this.location = location;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public int getTabSelected() {
        return tabSelected;
    }

    public void setTabSelected(int tabSelected) {
        this.tabSelected = tabSelected;
    }


}
