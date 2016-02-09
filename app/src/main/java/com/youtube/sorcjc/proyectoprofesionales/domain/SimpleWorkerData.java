package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SimpleWorkerData {

    /*
    {
      "basico": {...},
      "categos": [...],
      "tel": "",
      "estrellitas": 4
    }
    */
    @SerializedName("basico")
    private WorkerBasic basico;

    @SerializedName("categos")
    private ArrayList<Category> categos;

    @SerializedName("tel")
    private String tel;

    @SerializedName("estrellitas")
    private float estrellitas;

    public WorkerBasic getBasico() {
        return basico;
    }

    public ArrayList<Category> getCategories() {
        return categos;
    }

    public String getTel() {
        return tel;
    }

    public float getEstrellitas() {
        return estrellitas;
    }
}
