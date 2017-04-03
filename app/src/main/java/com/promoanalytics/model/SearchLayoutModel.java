package com.promoanalytics.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.promoanalytics.BR;

/**
 * Created by surinder on 03-Apr-17.
 */

public class SearchLayoutModel extends BaseObservable {

    String toolBatTitle;
    String locationSearchTitle;
    String categorySearchTitle;

    @Bindable
    public String getToolBatTitle() {
        return toolBatTitle;
    }

    public void setToolBatTitle(String toolBatTitle) {
        this.toolBatTitle = toolBatTitle;
        notifyPropertyChanged(BR.toolBatTitle);

    }

    @Bindable
    public String getLocationSearchTitle() {
        return locationSearchTitle;

    }

    public void setLocationSearchTitle(String locationSearchTitle) {
        this.locationSearchTitle = locationSearchTitle;
        notifyPropertyChanged(BR.locationSearchTitle);
    }

    @Bindable
    public String getCategorySearchTitle() {
        return categorySearchTitle;
    }

    public void setCategorySearchTitle(String categorySearchTitle) {
        this.categorySearchTitle = categorySearchTitle;
        notifyPropertyChanged(BR.categorySearchTitle);
    }


}
