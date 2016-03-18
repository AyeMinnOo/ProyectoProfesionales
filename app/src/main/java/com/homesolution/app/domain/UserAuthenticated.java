package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

public class UserAuthenticated {

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
