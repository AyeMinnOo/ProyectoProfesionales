package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

public class Worker {

    @SerializedName("uid")
    private String uid;

    @SerializedName("pid")
    private String pid;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("experience")
    private String experience;

    @SerializedName("score")
    private String score;

    @SerializedName("catstr")
    private String catstr;

    @SerializedName("picture")
    private String picture;

    @SerializedName("prestador")
    private SimpleWorkerData prestador;

    public String getUid() {
        return uid;
    }

    public String getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExperience() {
        return experience;
    }

    public String getScore() {
        return score;
    }

    public String getCatstr() {
        return catstr;
    }

    public String getUrlPhoto() {
        return picture;
    }

    public SimpleWorkerData getPrestador() {
        return prestador;
    }
}
