package com.promoanalytics.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.promoanalytics.BR;

/**
 * Created by surinder on 03-Apr-17.
 */

public class SearchLayoutModel extends BaseObservable {

    private String toolBatTitle;
    private String locationSearchTitle;
    private String categorySearchTitle;
    private boolean isOnAllCategory;

    public SearchLayoutModel(String toolBatTitle, String locationSearchTitle, String categorySearchTitle, boolean isOnAllCategory) {
        this.toolBatTitle = toolBatTitle;
        this.locationSearchTitle = locationSearchTitle;
        this.categorySearchTitle = categorySearchTitle;
        this.isOnAllCategory = isOnAllCategory;
    }


    @Bindable
    public boolean isOnAllCategory() {
        return isOnAllCategory;
    }

    public void setOnAllCategory(boolean onAllCategory) {
        isOnAllCategory = onAllCategory;
        notifyPropertyChanged(BR.onAllCategory);
    }


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
