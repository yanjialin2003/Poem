package com.example.myapplication.Modal;

/**
 * @创建者 晏嘉琳
 * @创建时间 2023/5/31 17:58
 * @类描述 ${TODO}实体类
 */

public class Poetry {
    private String title;
    private String desty;
    private String author;
    private String content;
    private String tag;

    public Poetry() {
    }

    public Poetry(String title, String desty, String author, String content, String tag) {
        this.title = title;
        this.desty = desty;
        this.author = author;
        this.content = content;
        this.tag = tag;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public String getDynasty() {
        return desty;
    }

    public void setDynasty(String desty) {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
