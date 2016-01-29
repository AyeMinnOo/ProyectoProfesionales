package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WorkerData {

    /*
    {
      "basico": {...},
      "skills": [...],
      "categos": [...],
      "ratings": [...],
      "tel": "",
      "estrellitas": 4
    }
    */
    @SerializedName("basico")
    private WorkerBasic basico;

    @SerializedName("skills")
    private ArrayList<Skill> skills;

    @SerializedName("ratings")
    private ArrayList<Rating> ratings;

    @SerializedName("tel")
    private String tel;

    @SerializedName("estrellitas")
    private float estrellitas;

    public WorkerBasic getBasico() {
        return basico;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public String getTel() {
        return tel;
    }

    public float getEstrellitas() {
        return estrellitas;
    }
}
