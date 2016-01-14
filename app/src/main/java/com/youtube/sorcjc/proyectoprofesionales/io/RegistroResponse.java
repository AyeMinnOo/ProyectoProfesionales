package com.youtube.sorcjc.proyectoprofesionales.io;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;

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
