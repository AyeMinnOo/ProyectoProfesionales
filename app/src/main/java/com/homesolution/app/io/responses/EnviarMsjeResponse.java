package com.homesolution.app.io.responses;

import com.google.gson.annotations.SerializedName;
import com.homesolution.app.domain.Message;

public class EnviarMsjeResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private Message message;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Message getResponse() {
        return message;
    }

}
