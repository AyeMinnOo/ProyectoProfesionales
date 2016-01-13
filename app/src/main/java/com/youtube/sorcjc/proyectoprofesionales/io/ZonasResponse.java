package com.youtube.sorcjc.proyectoprofesionales.io;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ZonasResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private ArrayList<String> response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public ArrayList<String> getResponse() {
        return response;
    }

}
