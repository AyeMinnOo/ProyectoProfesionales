package com.youtube.sorcjc.proyectoprofesionales.domain;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.youtube.sorcjc.proyectoprofesionales.Global;

public class Message {
/*
    {
        "mid": "2521",
        "from_uid": "324",
        "to_uid": "3",
        "content": "Hola",
        "created": "2015-09-03 15:49:03",
        "read": "2016-01-20 22:43:03",
        "parent_mid": "2521",
        "trash": "0",
        "readed": "1"
    }
 */
    @SerializedName("mid")
    private String mid;

    @SerializedName("from_uid")
    private String from_uid;

    @SerializedName("to_uid")
    private String to_uid;

    @SerializedName("content")
    private String content;

    @SerializedName("created")
    private String time;

    @SerializedName("read")
    private String read;

    @SerializedName("readed")
    private boolean readed;

    // Custom attribute
    private boolean amISender;

    public Message(String content, String time, boolean amISender) {
        this.content = content;
        this.time = time;
        this.amISender = amISender;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public boolean amISender(Context context) {
        final Global global = (Global) context;
        final String uid  = global.getUid();

        return from_uid.equals(uid);
    }

    public String getMid() {
        return mid;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public String getRead() {
        return read;
    }

    public boolean isReaded() {
        return readed;
    }
}
