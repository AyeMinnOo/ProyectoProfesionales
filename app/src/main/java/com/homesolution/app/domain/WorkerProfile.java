package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

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

    public WorkerData getPrestador() {
        return prestador;
    }

    public String getPicture() {
        return picture;
    }
}
