package com.example.myapplication.Util;

public class Spilt {

    public Spilt() {
    }

    public static String spiltContentText(String contentText) {
        String result = "";
        String split[];

        String regex = "\\(.*\\)";
        contentText = contentText.replaceAll(regex,"");

        split = contentText.split("。");
        for(int i = 0; i < split.length; i++){
            result = result + split[i] + "。" + "\n";
        }
        return result;
    }

    //按换行分割字符串
    public static String[] spiltPoemTextByEnter(String poemText) {
        String result[];
        result = poemText.split("\n");
        return result;
    }

}
