package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WorkerProfile {

    /*
    {
        "prestador": {},
        "picture": "http://dev.homesolution.com.ar/res/avatar/min/3.jpg",
        "galeria": []
    }
     */

    @SerializedName("prestador")
    private WorkerData prestador;

    @SerializedName("picture")
    private String picture;

    @SerializedName("galeria")
    private ArrayList<String> gallery;

    public WorkerData getPrestador() {
        return prestador;
    }

    public String getPicture() {
        return picture;
    }

    public ArrayList<String> getGallery() {
        return gallery;
    }
}
