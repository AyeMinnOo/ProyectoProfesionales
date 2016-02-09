package com.youtube.sorcjc.proyectoprofesionales.io.responses;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.Worker;
import com.youtube.sorcjc.proyectoprofesionales.domain.WorkerProfile;

import java.util.ArrayList;

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
