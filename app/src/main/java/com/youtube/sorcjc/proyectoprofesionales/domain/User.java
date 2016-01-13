package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("uid")
    private int uid;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;
    // "created": "2015-02-26 15:55:14",
    /*
    "extras": {
        "source": "organic2",
        "area": "Ciudad de Buenos Aires",
        "bgimage": "1"
    }
    }*/
    // "activity": "2016-01-13 15:29:40",
    @SerializedName("prestador")
    private boolean prestador;
    /*
    "referente": {
        "rrid": "1",
        "uid": "324",
        "created": "2015-12-28 11:04:35",
        "name": "Matias",
        "description": "Test user",
        "info": "Test street",
        "payment_details": "{}",
        "agreement_details": "{}",
        "modified": "2015-12-28 11:52:54"
    }*/
    @SerializedName("invitacionpendiente")
    private boolean invitacionpendiente;

    public int getUid() {
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
}
