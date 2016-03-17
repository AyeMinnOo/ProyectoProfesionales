package com.youtube.sorcjc.proyectoprofesionales.domain;

import com.google.gson.annotations.SerializedName;

public class WorkerBasic {

    /*
    "basico": {
        "pid": "26",
        "uid": "3",
        "name": "Matias Celani",
        "description": "Desarrollo de presencia en Internet y consultoria digital.",
        "experience": "10 años en el rubro. Webmaster de sitios de alto trafico (+2M).\r\nDesarrollo de proyectos para Italia, España, USA, Perú y Argentina, entre otros.",
        "certifications": {...},
        "level": "10",
        "catstr": "Técnico de PC",
        "tags": "Emite comprobantes para empresas,Presupuestos sin cargo",
        "area": "Parana, Zona Sur Gran Buenos Aires, Ciudad de Buenos Aires,"
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

    @SerializedName("certifications")
    private Certifications certifications;

    @SerializedName("level")
    private String level;

    @SerializedName("catstr")
    private String catstr;

    @SerializedName("tags")
    private String tags;

    @SerializedName("area")
    private String area;

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

    public Certifications getCertifications() {
        return certifications;
    }

    public String getLevel() {
        return level;
    }

    public String getCatstr() {
        return catstr;
    }

    public String getTags() {
        return tags;
    }

    public String getArea() {
        return area;
    }

    @Override
    public String toString() {
        String info = "";

        if (! catstr.isEmpty())
            info += "<i>" + catstr + "</i><br />";

        if (! description.isEmpty())
            info += description + "<br /><br />";

        if (! experience.isEmpty())
            info += "<b>Experiencia: </b>" + experience + "<br /><br />";

        /* Now, this info will be loaded in a different card
        if (! area.isEmpty())
            info += "<b>Áreas: </b>" + area + "<br />";
        */

        if (info.isEmpty())
            info = "Este usuario no ha cargado su información básica.";

        return info;
    }
}
