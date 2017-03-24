package com.promoanalytics.model.AllDeals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by think360 on 24/03/17.
 */

public class Data {

    @SerializedName("detail")
    @Expose
    private List<Detail> detail = null;
    @SerializedName("prevPage")
    @Expose
    private Integer prevPage;
    @SerializedName("nextPage")
    @Expose
    private Integer nextPage;

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public Integer getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Integer prevPage) {
        this.prevPage = prevPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

}