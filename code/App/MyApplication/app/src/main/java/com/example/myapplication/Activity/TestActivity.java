package com.example.myapplication.Activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.Api.ToTrans;
import com.example.myapplication.Modal.EmotionResBean;
import com.example.myapplication.Modal.PrivateInfo;
import com.example.myapplication.R;
import com.example.myapplication.Util.Spilt;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tencent.taisdk.TAIError;
import com.tencent.taisdk.TAIOralEvaluation;
import com.tencent.taisdk.TAIOralEvaluationData;
import com.tencent.taisdk.TAIOralEvaluationEvalMode;
import com.tencent.taisdk.TAIOralEvaluationFileType;
import com.tencent.taisdk.TAIOralEvaluationListener;
import com.tencent.taisdk.TAIOralEvaluationParam;
import com.tencent.taisdk.TAIOralEvaluationRet;
import com.tencent.taisdk.TAIOralEvaluationServerType;
import com.tencent.taisdk.TAIOralEvaluationStorageMode;
import com.tencent.taisdk.TAIOralEvaluationTextMode;
import com.tencent.taisdk.TAIOralEvaluationWord;
import com.tencent.taisdk.TAIOralEvaluationWorkMode;
import com.tencent.taisdk.TAIRecorderParam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity {

    TAIOralEvaluation oralEvaluation ;
    TextView content;
    TextView author;
    TextView title;
    TextView oralResult;
    Button viewAns ;
    TAIOralEvaluationParam param;
    String[] criterion = {"评分标准","发音标准","流利度","发音完整度"};
    HorizontalBarChart ratings ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        requestPermission();
        initView();
        settingsOfChat();
        setPoem();
        ratings.setVisibility(View.INVISIBLE);
        //点击查看答案，显示诗句内容
        viewAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.setVisibility(View.VISIBLE);
            }
        });
    }


    public void onRecord(View view){
        Button button = (Button) view;
        oralResult.setText("");
        if(oralEvaluation == null){
            oralEvaluation = new TAIOralEvaluation();
        }
        if (oralEvaluation.isRecording()){
            button.setText("开始测试");
            oralEvaluation.stopRecordAndEvaluation();
        }else {
            button.setText("停止录制");
            oralEvaluation.setListener(new TAIOralEvaluationListener() {
                @Override
                public void onEvaluationData(TAIOralEvaluationData data, TAIOralEvaluationRet result) {
                    /**
                     * 中间结果
                     * 解析result中的评估结果
                     */
                    System.out.println("middle");
                }

                @Override
                public void onEvaluationError(TAIOralEvaluationData data, TAIError error) {
                    //调用接口失败，提升信号不好或账号已欠费
                    System.out.println("error");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            String retString = gson.toJson(error);
                            String errorStr = "onEvaluationE rror: " + retString + " seqId: " + data.seqId;
                        }
                    });
                }

                @Override
                public void onFinalEvaluationData(TAIOralEvaluationData data, TAIOralEvaluationRet result) {
                    //提交服务端，情感分析
                    submitMp3(param.audioPath);
                    /**
                     * 最终结果
                     * 处理每个字是否有念或读音正确
                     */
                    List<TAIOralEvaluationWord> list =  result.words;
                    String text = String.valueOf(content.getText());
                    //部分文字改变颜色
                    //这里注意一定要先给textview赋值
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    int curr = 0,size = text.length();
                    for (TAIOralEvaluationWord word : list) {
                        /**
                         * 处理没有读 -1
                         * 处理念错字 <0.5 || 0.7
                         */
                        ForegroundColorSpan miss = new ForegroundColorSpan(getResources().getColor(R.color.toast_error_color));
                        ForegroundColorSpan worry = new ForegroundColorSpan(getResources().getColor(R.color.toast_error_color));
                        ForegroundColorSpan right = new ForegroundColorSpan(getResources().getColor(R.color.black));
                        if(word.pronAccuracy <= -1){
                            builder.setSpan(miss,curr,curr+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }else if(word.pronAccuracy <= 0.5){
                            builder.setSpan(worry,curr,curr+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }else {
                            builder.setSpan(right,curr,curr+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        }
                        curr++;
                        while(curr < text.length() && isChinesePunctuation(text.charAt(curr))){
                            curr++;
                        }
                    }
                    /**
                     * 解析口语分数
                     */
                    Gson gson = new Gson();
                    String retString = gson.toJson(result);
                    String ans = "";
                    JsonParser parser = new JsonParser();
                    JsonElement JsonTree = parser.parse(retString);
                    double SuggestedScore,PronAccuracy = 0,PronFluency = 0,PronCompletion = 0;
                    if(JsonTree.isJsonObject()){
                        JsonObject jsonObject = JsonTree.getAsJsonObject();
                        SuggestedScore = jsonObject.get("SuggestedScore").getAsDouble();
                        ans += "评分: "+SuggestedScore+"\n";
                        PronAccuracy = jsonObject.get("PronAccuracy").getAsDouble();
//                        ans += "PronAccuracy: "+PronAccuracy+"\n";
                        PronFluency = jsonObject.get("PronFluency").getAsDouble();
//                        ans += "PronFluency: "+PronFluency+"\n";
                        PronCompletion = jsonObject.get("PronCompletion").getAsDouble();
//                        ans += "PronCompletion: "+PronCompletion+"\n";
                    }

                    String finalAns = ans;
                    int finalPronAccuracy = (int) (PronAccuracy);
                    int finalPronFluency = (int) (PronFluency*100);
                    int finalPronCompletion = (int) (PronCompletion*100);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //图表设置
                            List<BarEntry>list=new ArrayList<>();
                            list.add(new BarEntry(1, finalPronAccuracy));
                            list.add(new BarEntry(2, finalPronFluency));
                            list.add(new BarEntry(3, finalPronCompletion));

                            BarDataSet barDataSet=new BarDataSet(list,"口语分析");
                            BarData barData=new BarData(barDataSet);
                            barData.setDrawValues(true);
                            ratings.setVisibility(View.VISIBLE);
                            ratings.setData(barData);
                            ratings.animateY(3000);

                            content.setText(builder);
                            content.setVisibility(View.VISIBLE);
                            oralResult.append(finalAns);
//                            oralResult.append(retString);
                        }
                    });
                }

                @Override
                public void onEndOfSpeech(boolean isSpeak) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRecord(null);
                        }
                    });
                }

                @Override
                public void onVolumeChanged(int volume) {
                    ;
                }
            });
            param = new TAIOralEvaluationParam();
            param.context = this;
            param.sessionId = UUID.randomUUID().toString();
            param.appId = PrivateInfo.appId;
            param.soeAppId = PrivateInfo.soeAppId;
            param.secretId = PrivateInfo.secretId;
            param.secretKey = PrivateInfo.secretKey;
            param.token = PrivateInfo.token;

            int evalMode = TAIOralEvaluationEvalMode.PARAGRAPH;
            param.workMode =  TAIOralEvaluationWorkMode.ONCE ;
            param.evalMode = evalMode;
            param.storageMode = TAIOralEvaluationStorageMode.DISABLE;
            param.fileType = TAIOralEvaluationFileType.MP3;
            param.serverType = TAIOralEvaluationServerType.CHINESE ;
            param.textMode =  TAIOralEvaluationTextMode.NORMAL ;
            param.scoreCoeff = 2.0;
            param.refText = content.getText().toString();
            param.audioPath = this.getFilesDir() + "/" + param.sessionId + ".mp3";
            if (param.workMode == TAIOralEvaluationWorkMode.STREAM) {
                param.timeout = 5;
                param.retryTimes = 5;
            } else {
                param.timeout = 30;
                param.retryTimes = 0;
            }
            TAIRecorderParam recordParam = new TAIRecorderParam();
            recordParam.fragSize = (int) (1.0 * 1024);
            recordParam.fragEnable = false;
            recordParam.vadEnable = true;
            recordParam.vadInterval = Integer.parseInt("5000");
            oralEvaluation.setRecorderParam(recordParam);
            oralEvaluation.startRecordAndEvaluation(param);
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1234);
        }
    }

    public  boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }

    void submitMp3(String filePath){
        File file = new File(filePath);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://101.33.242.218:8081")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ToTrans request = retrofit.create(ToTrans.class);
        //请求体
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mpeg"),file);
        builder.addFormDataPart("file", file.getName(), requestBody);
        List<MultipartBody.Part> parts = builder.build().parts();
        //对 发送请求 进行封装
        Call<EmotionResBean> call = request.getCall(parts);
        call.enqueue(new Callback<EmotionResBean>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<EmotionResBean> call, Response<EmotionResBean> response) {
                //请求处理,输出结果
                System.out.println(response.body());
                Gson gson = new Gson();
                EmotionResBean resBean = response.body();
                String emotion = resBean.getEmotion();
                if(emotion.equals("neutral")||emotion.equals("calm")) {
                    oralResult.append("看看赏析，再体会诗中的情感");
                }else {
                    oralResult.append("声情并茂呢");
                }
            }
            //请求失败时候的回调
            @Override
            public void onFailure(Call<EmotionResBean> call, Throwable throwable) {
                System.out.println((call.request().body()));
                System.out.println("连接失败");
            }
        });
    }

    void initView(){
        content = findViewById(R.id.content);
        oralResult = findViewById(R.id.result);
        ratings = findViewById(R.id.ratings);
        viewAns = findViewById(R.id.viewAns);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        //设置内容不可见
        content.setVisibility(View.INVISIBLE);
    }

    void settingsOfChat(){
        //X轴
        XAxis xAxis=ratings.getXAxis();
        xAxis.setDrawGridLines(false);  //是否绘制X轴上的网格线（背景里面的竖线）
        xAxis.setAxisLineColor(Color.BLACK);   //X轴颜色
        xAxis.setAxisLineWidth(2);           //X轴粗细
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);        //X轴所在位置   默认为上面
        xAxis.setValueFormatter(new IndexAxisValueFormatter(criterion));
        xAxis.setAxisMaximum(4);   //X轴最大数值
        xAxis.setAxisMinimum(0);   //X轴最小数值
        //X轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        xAxis.setLabelCount(4,false);
        xAxis.setAvoidFirstLastClipping(true);

        //Y轴
        YAxis AxisLeft=ratings.getAxisRight();
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        AxisLeft.setAxisLineColor(Color.BLACK);  //Y轴颜色
        AxisLeft.setAxisLineWidth(2);           //Y轴粗细
//        AxisLeft.setValueFormatter(new DefaultAxisValueFormatter(100));
        AxisLeft.setAxisMaximum(105f);   //Y轴最大数值
        AxisLeft.setAxisMinimum(0f);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        AxisLeft.setLabelCount(5,false);

        AxisLeft=ratings.getAxisLeft();
        AxisLeft.setDrawGridLines(false);  //是否绘制Y轴上的网格线（背景里面的横线）
        AxisLeft.setAxisLineColor(Color.BLACK);  //Y轴颜色
        AxisLeft.setAxisLineWidth(2);           //Y轴粗细
//        AxisLeft.setValueFormatter(new DefaultAxisValueFormatter(100));
        AxisLeft.setAxisMaximum(105f);   //Y轴最大数值
        AxisLeft.setAxisMinimum(0f);   //Y轴最小数值
        //Y轴坐标的个数    第二个参数一般填false     true表示强制设置标签数 可能会导致X轴坐标显示不全等问题
        AxisLeft.setLabelCount(5,false);



        //是否隐藏右边的Y轴（不设置的话有两条Y轴 同理可以隐藏左边的Y轴）
        ratings.getDescription().setEnabled(false);//隐藏右下角英文
        ratings.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为右边
        ratings.getAxisLeft().setEnabled(false);//隐藏上侧Y轴   默认是上下两侧都有Y轴
        ratings.setTouchEnabled(false);//不可调整大小
    }

    void setPoem(){
        //获取活动跳转传过来的诗，并显示在界面（诗内容不显示）
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));

        String dynasty_author = intent.getStringExtra("desty") + " . "
                + intent.getStringExtra("author");
        author.setText(dynasty_author);

        String contentText = intent.getStringExtra("content");

        contentText = Spilt.spiltContentText(contentText);
        content.setText(contentText);
    }

}