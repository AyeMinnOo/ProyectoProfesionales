package com.homesolution.app.domain;

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

    @SerializedName("esprestador")
    private boolean esPrestador;

    @SerializedName("invitacionpendiente")
    private boolean invitacionPendiente;

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
        return esPrestador;
    }

    public boolean hasInvitacionPendiente() {
        return invitacionPendiente;
    }

    public String getPicture() {
        return picture;
    }

}
