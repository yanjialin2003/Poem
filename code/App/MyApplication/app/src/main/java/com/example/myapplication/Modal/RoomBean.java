package com.example.myapplication.Modal;

public class RoomBean {
    private int rid;
    private String rname;
    private String description;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
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

    public RoomBean(int rid, String rname, String description) {
        this.rid = rid;
        this.rname = rname;
        this.description = description;
    }

    public RoomBean() {
    }
}
