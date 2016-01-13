package com.youtube.sorcjc.proyectoprofesionales.io;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;

import java.util.ArrayList;

public class LoginResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private UserAuthenticated response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public UserAuthenticated getResponse() {
        return response;
    }

}
