package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

public class Extras {

    @SerializedName("area")
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
