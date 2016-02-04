package com.youtube.sorcjc.proyectoprofesionales;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.youtube.sorcjc.proyectoprofesionales.domain.UserAuthenticated;

public class Global extends Application {

    private UserAuthenticated userAuthenticated;

    public String getToken() {
        return userAuthenticated.getToken();
    }

    public String getUid() {
        return userAuthenticated.getUser().getUid();
    }

    public String getPicture() {
        return userAuthenticated.getUser().getPicture();
    }

    public void setUserAuthenticated(UserAuthenticated userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
    }

    // Used for enabling multidex
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
