package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

public class Chat {

    /*
    "uid":"1481",
    "email":"leandro.rondinella@lenaled.com.ar",
    "activity":"2016-01-13 09:50:46",
    "pid":"315",
    "name":"Leandro Rondinella",
    "catstr":"Plomero, Electricista, Gasista, T\u00e9cnico de PC, Herrero, Jardinero, T\u00e9cnico de Electrodomesticos, Obra Completa",
    "readed":"1",
    "msg":"Muchas gracias por la respuesta Leandro. En breve seguramente estaran llegando nuevas consultas. Puede adquirir el pack Nitro desde tu panel de control para mayor cantidad de consultas en menor tiempo. La version gratuita es limitada. Saludos!",
    "cdate":"2015-12-28 14:34:53",
    "rdate":"2015-12-29 22:01:56"
     */

    @SerializedName("uid")
    private String uid;

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("activity")
    private String activity;

    @SerializedName("pid")
    private String pid;

    @SerializedName("name")
    private String name;

    @SerializedName("msg")
    private String msg;

    @SerializedName("catstr")
    private String catstr;

    @SerializedName("picture")
    private String picture;

    @SerializedName("prestador")
    private SimpleWorkerData prestador;

    @SerializedName("esprestador")
    private boolean esPrestador;

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getActivity() {
        return activity;
    }

    public String getPid() {
        return pid;
    }

    public String getName() {
        if (name==null || name.isEmpty())
            return username;

        return name;
    }

    public String getMsg() {
        return msg;
    }

    public String getCatstr() {
        return catstr;
    }

    public String getUrlPhoto() {
        return picture;
    }

    public SimpleWorkerData getPrestador() {
        return prestador;
    }

    public boolean isEsPrestador() {
        return esPrestador;
    }
}
