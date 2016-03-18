package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

public class Certifications {
    /*
        "certifications": {
          "matriculas": "",
          "habilitaciones": "",
          "titulos": "",
          "cursos": "",
          "otras": "",
          "ART": "Seguro Profesional según pedido del cliente.",
          "garantias": "Seguimiento del trabajo post-deploy con respuesta 24-7 por email."
        }
    */

    @SerializedName("matriculas")
    private String matriculas;

    @SerializedName("habilitaciones")
    private String habilitaciones;

    @SerializedName("titulos")
    private String titulos;

    @SerializedName("cursos")
    private String cursos;

    @SerializedName("otras")
    private String otras;

    @SerializedName("ART")
    private String ART;

    @SerializedName("garantias")
    private String garantias;

    public String getMatriculas() {
        return matriculas;
    }

    public String getHabilitaciones() {
        return habilitaciones;
    }

    public String getTitulos() {
        return titulos;
    }

    public String getCursos() {
        return cursos;
    }

    public String getOtras() {
        return otras;
    }

    public String getART() {
        return ART;
    }

    public String getGarantias() {
        return garantias;
    }

    @Override
    public String toString() {
        // Returns the certifications in html format
        String certificaciones = "";

        if (matriculas != null && ! matriculas.isEmpty())
            certificaciones += "<b>Matrículas: </b>" + matriculas + "<br /><br />";

        if (habilitaciones != null && ! habilitaciones.isEmpty())
            certificaciones += "<b>Habilitaciones: </b>" + habilitaciones + "<br /><br />";

        if (titulos != null && ! titulos.isEmpty())
            certificaciones += "<b>Títulos: </b>" + titulos + "<br /><br />";

        if (cursos != null && ! cursos.isEmpty())
            certificaciones += "<b>Cursos: </b>" + cursos + "<br /><br />";

        if (otras != null && ! otras.isEmpty())
            certificaciones += "<b>Otras: </b>" + otras + "<br /><br />";

        if (ART != null && ! ART.isEmpty())
            certificaciones += "<b>ART: </b>" + ART + "<br /><br />";

        if (garantias != null && ! garantias.isEmpty())
            certificaciones += "<b>Garantías: </b>" + garantias + "<br />";

        return certificaciones;
    }
}
