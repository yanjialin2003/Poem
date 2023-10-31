package com.example.demo.bean;

public class ChatRoom {
    private Integer rid;
    private String rname;
    private String description;

    public ChatRoom() {
    }

    public ChatRoom(Integer rid, String rname, String description) {
        this.rid = rid;
        this.rname = rname;
        this.description = description;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
