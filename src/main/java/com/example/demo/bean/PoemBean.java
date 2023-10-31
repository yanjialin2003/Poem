package com.example.demo.bean;

public class PoemBean {
    private String id;
    private String title;
    private String desty;
    private String author;
    private String content;
    private String trans_content;
    private String appear;
    private String background;
    private String tag;
    private String formal;
    private String data;
    private String ci_name;
    private String qu_name;
    private String zhu;

    public PoemBean() {
    }

    public PoemBean(String id, String title, String desty, String author, String content, String trans_content, String appear, String background, String tag, String formal, String data, String ci_name, String qu_name, String zhu) {
        this.id = id;
        this.title = title;
        this.desty = desty;
        this.author = author;
        this.content = content;
        this.trans_content = trans_content;
        this.appear = appear;
        this.background = background;
        this.tag = tag;
        this.formal = formal;
        this.data = data;
        this.ci_name = ci_name;
        this.qu_name = qu_name;
        this.zhu = zhu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesty() {
        return desty;
    }

    public void setDesty(String desty) {
        this.desty = desty;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTrans_content() {
        return trans_content;
    }

    public void setTrans_content(String trans_content) {
        this.trans_content = trans_content;
    }

    public String getAppear() {
        return appear;
    }

    public void setAppear(String appear) {
        this.appear = appear;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFormal() {
        return formal;
    }

    public void setFormal(String formal) {
        this.formal = formal;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCi_name() {
        return ci_name;
    }

    public void setCi_name(String ci_name) {
        this.ci_name = ci_name;
    }

    public String getQu_name() {
        return qu_name;
    }

    public void setQu_name(String qu_name) {
        this.qu_name = qu_name;
    }

    public String getZhu() {
        return zhu;
    }

    public void setZhu(String zhu) {
        this.zhu = zhu;
    }
}
