package com.homesolution.app.io.responses;

import com.google.gson.annotations.SerializedName;
import com.homesolution.app.domain.WorkerProfile;

public class PrestadorResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private WorkerProfile response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public WorkerProfile getResponse() {
        return response;
    }

}
