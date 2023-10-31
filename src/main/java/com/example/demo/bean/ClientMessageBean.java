package com.example.demo.bean;

import java.io.File;

public class ClientMessageBean {
    private int type;
    private String from;
    private String message;
    private File record;

    public ClientMessageBean() {
    }

    public ClientMessageBean(int type, String from, String message, File record) {
        this.type = type;
        this.from = from;
        this.message = message;
        this.record = record;
    }

    public File getRecord() {
        return record;
    }

    public void setRecord(File record) {
        this.record = record;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
