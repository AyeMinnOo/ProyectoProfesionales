package com.homesolution.app.io.response;

import com.google.gson.annotations.SerializedName;

public class RegistroResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    /*@SerializedName("response")
    private UserRegistered response;*/

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    /*public UserRegistered getResponse() {
        return response;
    }*/

}
