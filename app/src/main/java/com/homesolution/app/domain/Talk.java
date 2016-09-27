package com.homesolution.app.domain;

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

    @SerializedName("rechazable")
    private boolean rechazable;

    @SerializedName("rechazablemid")
    private String rechazableMid;

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

    public boolean isRechazable() {
        return rechazable;
    }

    public void setRechazable(boolean rechazable) {
        this.rechazable = rechazable;
    }

    public String getRechazableMid() {
        return rechazableMid;
    }

    public void setRechazableMid(String rechazableMid) {
        this.rechazableMid = rechazableMid;
    }
}
