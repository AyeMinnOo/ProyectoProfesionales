package com.homesolution.app.io.response;

import com.google.gson.annotations.SerializedName;
import com.homesolution.app.domain.Chat;

import java.util.ArrayList;

public class ChatsResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private ArrayList<Chat> response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public ArrayList<Chat> getResponse() {
        return response;
    }

}
