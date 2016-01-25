package com.youtube.sorcjc.proyectoprofesionales;

import android.app.Application;

import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;

public class Global extends Application {

    private UserAuthenticated userAuthenticated;

    public String getToken() {
        return userAuthenticated.getToken();
    }

    public String getUid() {
        return userAuthenticated.getUser().getUid();
    }

    public String getPictura() {
        return userAuthenticated.getUser().getPicture();
    }

    public void setUserAuthenticated(UserAuthenticated userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
    }

}
