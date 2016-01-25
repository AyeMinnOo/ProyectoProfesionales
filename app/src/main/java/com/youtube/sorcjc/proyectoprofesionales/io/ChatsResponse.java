package com.youtube.sorcjc.proyectoprofesionales.io;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.Chat;

import java.util.ArrayList;

public class ChatsResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private ArrayList<Chat> response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public ArrayList<Chat> getResponse() {
        return response;
    }

}
