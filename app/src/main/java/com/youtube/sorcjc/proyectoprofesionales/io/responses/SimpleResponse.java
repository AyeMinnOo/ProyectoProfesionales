package com.youtube.sorcjc.proyectoprofesionales.io.responses;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private Boolean response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Boolean getResponse() {
        return response;
    }

}
