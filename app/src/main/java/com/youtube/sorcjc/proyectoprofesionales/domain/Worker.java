package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

public class Worker {

    /*
    {
      "uid": "74",
      "pid": "33",
      "display": "",
      "priority": "0",
      "created": "2015-05-18 13:16:16",
      "name": "Javier Ramade",
      "description": "cambiamos puertas interiores y exteriores de madera y aluminio, tambien reciclamos ventanas sin necesidad de llamar a un albañil, colocamos mosquiteros de enrollar donde no hay guias para mosquitero",
      "experience": "25 años  en el rubro avalan nuestra experiencia y trabajamos tanto para profesionales como para  particulares",
      "certifications": "{}",
      "score": "753",
      "score_qty": "3",
      "score_detail": "{}",
      "level": "10",
      "info": "{\"telefono\":\"4687-7598/8562\",\"celular\":\"(15)5956-9686\", \"email\":\"aberturasintegrales@gmail.com\"}",
      "catstr": "Carpintero, Vidriero",
      "profileheader": "0",
      "tags": ""
    }
     */

    @SerializedName("uid")
    private String uid;

    @SerializedName("pid")
    private String pid;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("experience")
    private String experience;

    @SerializedName("score")
    private String score;

    @SerializedName("catstr")
    private String catstr;

    @SerializedName("picture")
    private String picture;

    public String getUid() {
        return uid;
    }

    public String getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExperience() {
        return experience;
    }

    public String getScore() {
        return score;
    }

    public String getCatstr() {
        return catstr;
    }

    public String getUrlPhoto() {
        return picture;
    }
}
