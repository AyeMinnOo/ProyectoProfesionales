package com.youtube.sorcjc.proyectoprofesionales.io.responses;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;

public class LoginResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    // This field will be parsed using a custom deserializer
    private UserAuthenticated userAuthenticated;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public UserAuthenticated getResponse() {
        return userAuthenticated;
    }

    public void setResponse(UserAuthenticated response) {
        this.userAuthenticated = response;
    }
}
