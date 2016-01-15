package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

public class Category {

    /*
    {
      "cid": "1",
      "name": "Pintor",
      "description": "¿Queres pintar un ambiente, restaurar una fachada o darle una lavada de cara a tu casa? Para estas y muchas otras cosas, contactá pintores con experiencia, compara por su reputación y contratá de manera directa.",
      "priority": "10",
      "slug": "pintor"
    }
    */

    @SerializedName("cid")
    private String cid;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("priority")
    private String priority;

    @SerializedName("slug")
    private String slug;

    public String getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getSlug() {
        return slug;
    }

    public String getUrlImage() {
        return "http://homesolution.com.ar/res/img/cats/" + cid + ".png";
    }
}
