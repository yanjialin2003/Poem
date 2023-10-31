package com.example.demo.utils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.ArrayList;


public class JavaExecPython {
    public static HashMap<String, Object> emotionAnalyze(String Path) throws IOException {
        HashMap<String, Object> res = new HashMap<>();
        String emotion = "";
        HashMap<String, Double> prob = new HashMap<>();
        String cmd = "/home/lighthouse/miniconda3/envs/Emotion/bin/python /home/lighthouse/Program/AImodel/transmission-tool/Speech-Emotion-Recognition/predict.py --config /home/lighthouse/Program/AImodel/transmission-tool/Speech-Emotion-Recognition/configs/lstm.yaml --path "+Path;
        Process pr = Runtime.getRuntime().exec(cmd);

        InputStreamReader ir = new InputStreamReader(pr.getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        String line ,result = "";
        int last = 0;
        while((line = input.readLine())!=null){
            last = input.getLineNumber();
            result += line;
        }
        if(result.length() == 0){
            return null;
        }
        emotion = result.substring(result.indexOf("Recogntion"),result.indexOf("Probability"));
        String listString = result.substring(result.indexOf("Probability"), result.length());
        //输出分析结果
        System.out.println(emotion);
        System.out.println(listString);

        emotion = emotion.substring(13, emotion.length());
        ArrayList<Double> list = new ArrayList<>();
        listString = listString.substring(listString.indexOf("[")+1,listString.indexOf("]"));
        String[] array = listString.split(" ");
        System.out.println(array.length);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
            list.add(Double.valueOf(array[i]));
            System.out.println("list["+i+"]:"+list.get(i));
        }
        res.put("emotion", emotion);
        res.put("prob", list);
        return res;
    }
    public static String createPoem(String  form, String head) {
        String ans = "";
        int length = 0;
        if(form.equals("七言律诗")){
            length = 65;
        } else if (form.equals("五言律诗 ")) {
            length = 50;
        } else if (form.equals("七言绝句")) {
            length = 34;
        } else if (form.equals("五言绝句")) {
            length = 26;
        }

        try {
            String  cmd  = "/home/lighthouse/miniconda3/envs/chinese/bin/python /home/lighthouse/Program/AImodel/transmission-tool/GPT2-Chinese-Poetry/generate.py --length="+length+" --nsamples=1 --prefix="+form+"[SEP]"+head;
            Process pr = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(pr.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = "";
            String result = "";
            while((line = input.readLine())!=null){
                result += line;
            }
            int indexBegin = result.lastIndexOf(head)+head.length();
            ans = result.substring(indexBegin,result.lastIndexOf("。"));
            System.out.println(ans);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
}