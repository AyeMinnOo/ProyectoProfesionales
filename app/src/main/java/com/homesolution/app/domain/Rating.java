package com.homesolution.app.domain;

import com.google.gson.annotations.SerializedName;

public class Rating {
/*
{
  "pid": "26",
  "uid": "647",
  "comment": "Me armo muy bien el blog e hicimos una app que anduvo bien también.\r\n\r\nMuy recomendable porque sabe mucho. Es de super Confianza.",
  "values": {
    "puntualidad": "4",
    "profesionalismo": "4",
    "cumplimiento": "4",
    "recomendaria": "1",
    "precio": "4"
  },
  "skills": "[\"15\"]",
  "created": "2015-05-16 01:39:35",
  "overall": "998",
  "estrellitas": 5
}
*/
    @SerializedName("comment")
    private String comment;

    @SerializedName("estrellitas")
    private String stars;

    public String getComment() {
        return comment;
    }

    public String getStars() {
        return stars;
    }
}
