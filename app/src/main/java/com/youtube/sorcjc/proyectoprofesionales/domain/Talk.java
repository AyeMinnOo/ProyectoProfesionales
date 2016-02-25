package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Talk {

    @SerializedName("esprestador")
    private boolean esPrestador;

    // Read it only if the previous variable is TRUE
    private SimpleWorkerData simpleWorkerData;

    @SerializedName("username")
    private String username;

    @SerializedName("picture")
    private String picture;

    @SerializedName("chat")
    private ArrayList<Message> chat;

    public boolean isPrestador() {
        return esPrestador;
    }

    public SimpleWorkerData getPrestador() {
        return simpleWorkerData;
    }

    public void setPrestador(SimpleWorkerData simpleWorkerData) {
        this.simpleWorkerData = simpleWorkerData;
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
