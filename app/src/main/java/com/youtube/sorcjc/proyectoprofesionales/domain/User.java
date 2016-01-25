package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("uid")
    private String uid;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

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

    public String getUsername() {
        return username;
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
