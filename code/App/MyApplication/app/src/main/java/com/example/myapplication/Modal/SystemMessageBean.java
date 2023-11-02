package com.example.myapplication.Modal;

public class SystemMessageBean {
    private boolean isSystem;
    private String from;
    private String message;

    public SystemMessageBean() {
    }

    public SystemMessageBean(boolean isSystem, String from, String message) {
        this.isSystem = isSystem;
        this.from = from;
        this.message = message;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
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
