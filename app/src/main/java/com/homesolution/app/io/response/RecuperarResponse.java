package com.homesolution.app.io.response;

import com.google.gson.annotations.SerializedName;

public class RecuperarResponse {

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
