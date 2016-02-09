package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.Message;

import java.util.ArrayList;

public class Talk {

    @SerializedName("prestador")
    private SimpleWorkerData prestador;

    @SerializedName("username")
    private String username;

    @SerializedName("picture")
    private String picture;

    @SerializedName("chat")
    private ArrayList<Message> chat;

    public SimpleWorkerData getPrestador() {
        return prestador;
    }

    public String getUsername() {
        return username;
    }

    public String getPicture() {
        return picture;
    }

    public ArrayList<Message> getChat() {
        return chat;
    }
}
