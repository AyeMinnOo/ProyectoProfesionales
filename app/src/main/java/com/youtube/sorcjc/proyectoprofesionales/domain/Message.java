package com.youtube.sorcjc.proyectoprofesionales.domain;

public class Message {

    private String message;
    private String time;
    private boolean amISender;

    public Message(String message, String time, boolean amISender) {
        this.message = message;
        this.time = time;
        this.amISender = amISender;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public boolean amISender() {
        return amISender;
    }
}
