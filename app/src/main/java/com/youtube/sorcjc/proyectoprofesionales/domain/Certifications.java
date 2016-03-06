package com.youtube.sorcjc.proyectoprofesionales.domain;

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
        String certificaciones = "";

        if (matriculas != null && ! matriculas.isEmpty())
            certificaciones += "Matrículas: " + matriculas + "\n";

        if (habilitaciones != null && ! habilitaciones.isEmpty())
            certificaciones += "Habilitaciones: " + habilitaciones + "\n";

        if (titulos != null && ! titulos.isEmpty())
            certificaciones += "Títulos: " + titulos + "\n";

        if (cursos != null && ! cursos.isEmpty())
            certificaciones += "Cursos: " + cursos + "\n";

        if (otras != null && ! otras.isEmpty())
            certificaciones += "Otras: " + otras + "\n";

        if (ART != null && ! ART.isEmpty())
            certificaciones += "ART: " + ART + "\n";

        if (garantias != null && ! garantias.isEmpty())
            certificaciones += "Garantías: " + garantias + "\n";

        /*
        if (certificaciones.isEmpty())
            certificaciones = "Este usuario no dispone de certificaciones.";
        */
        return certificaciones;
    }
}
