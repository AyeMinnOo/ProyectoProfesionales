package com.homesolution.app.domain;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.homesolution.app.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private String created;

    @SerializedName("read")
    private String read;

    @SerializedName("parent_mid")
    private String parent_mid;

    @SerializedName("readed")
    private boolean readed;

    // Custom attribute
    private boolean amISender;

    public Message(String content, String created, boolean amISender) {
        this.content = content;
        this.created = created;
        this.amISender = amISender;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        // Example for "created" => 2016-03-09 13:15:30
        String time;
        try {
            // Original format => yyyy-MM-dd hh:mm:ss
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = dt.parse(created);

            // New format => dd/MM/yyyy hh:mm
            SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            time = dt1.format(date);
        } catch (ParseException pe) {
            time = "Unknown";
        }

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

    public String getFromUid() {
        return from_uid;
    }

    public String getToUid() {
        return to_uid;
    }

    public String getRead() {
        return read;
    }

    public boolean isReaded() {
        return readed;
    }

    public String getParentMid() {
        return parent_mid;
    }
}
