package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("uid")
    private String uid;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("extras")
    private Extras extras;

    @SerializedName("prestador")
    private boolean prestador;

    @SerializedName("invitacionpendiente")
    private boolean invitacionpendiente;

    @SerializedName("picture")
    private String picture;

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Extras getExtras() {
        return extras;
    }

    public boolean isPrestador() {
        return prestador;
    }

    public boolean isInvitacionpendiente() {
        return invitacionpendiente;
    }

    public String getPicture() {
        return picture;
    }




}
