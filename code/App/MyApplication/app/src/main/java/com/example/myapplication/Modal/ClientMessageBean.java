package com.example.myapplication.Modal;

import java.io.File;

public class ClientMessageBean {
    /**
     * type对应
     * 0 系统消息
     * 1 自己发送的文本消息
     * 2 其他人发送的文本消息
     * 3 自己发送的语音消息
     * 4 其他人发送的语音消息
     */
    private int type;
    private String from;
    private String message;
    private byte[] record;

    public ClientMessageBean() {
    }

    public ClientMessageBean(int type, String from, String message, byte[] record) {
        this.type = type;
        this.from = from;
        this.message = message;
        this.record = record;
    }

    public byte[] getRecord() {
        return record;
    }

    public void setRecord(byte[] record) {
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
