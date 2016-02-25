package com.youtube.sorcjc.proyectoprofesionales.io.responses;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.Talk;

public class ChatResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private Talk response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Talk getResponse() {
        return response;
    }

    public void setResponse(Talk response) {
        this.response = response;
    }

}
