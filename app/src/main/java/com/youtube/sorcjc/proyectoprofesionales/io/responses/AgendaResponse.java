package com.youtube.sorcjc.proyectoprofesionales.io.responses;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.Worker;

import java.util.ArrayList;

public class AgendaResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private ArrayList<Worker> response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public ArrayList<Worker> getResponse() {
        return response;
    }

}
