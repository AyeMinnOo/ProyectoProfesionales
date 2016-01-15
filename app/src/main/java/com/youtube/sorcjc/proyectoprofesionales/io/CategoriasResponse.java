package com.youtube.sorcjc.proyectoprofesionales.io;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.domain.Categoria;

import java.util.ArrayList;

public class CategoriasResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("error")
    private String error;

    @SerializedName("response")
    private ArrayList<Categoria> response;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public ArrayList<Categoria> getResponse() {
        return response;
    }

}
