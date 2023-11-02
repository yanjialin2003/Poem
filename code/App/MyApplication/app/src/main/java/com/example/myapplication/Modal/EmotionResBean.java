package com.example.myapplication.Modal;

import java.util.List;

public class EmotionResBean {
    private String emotion ;
    private List<Double> prob;

    public EmotionResBean() {
    }

    public EmotionResBean(String emotion, List<Double> prob) {
        this.emotion = emotion;
        this.prob = prob;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public List<Double> getList() {
        return prob;
    }

    public void setList(List<Double> list) {
        this.prob = list;
    }
}
