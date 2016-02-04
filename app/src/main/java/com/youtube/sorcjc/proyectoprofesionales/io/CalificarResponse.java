package com.youtube.sorcjc.proyectoprofesionales.io;

import com.google.gson.annotations.SerializedName;

public class CalificarResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private int response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public int getResponse() {
        return response;
    }

}
