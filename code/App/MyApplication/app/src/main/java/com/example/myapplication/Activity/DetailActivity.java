package com.example.myapplication.Activity;

import static com.xuexiang.xui.XUI.getContext;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Api.Api;
import com.example.myapplication.Constant.constant;
import com.example.myapplication.Modal.PoetryDetail;
import com.example.myapplication.R;
import com.example.myapplication.Util.Spilt;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = "test";

    private Retrofit PoetryDetailRetrofit;
    private Context context;

    private Toolbar toolbar;
    private TextView titleText;
    private String toolbarTitle;

    private HtmlTextView htmlAnnotation;
    private TextView translation;
    private TextView appreciation;
    private LinearLayout linearLayout;
    private Button test_button;
    private TextView menu_line;
    private TextView poem_title;
    private TextView poem_author;
    private TextView poem_content;
    private ImageView poem_menu_advice;

//    标签
//    private TagFlowLayout tag_flow;

    private ScrollView scrollView;
    private ImageView author_img;
    private ImageView icon_collect;
    private TextView author_content;
    private JSONObject responseObject;
    private PoetryDetail poetryDetail;
    private final int COMPLETED = 1;
    private final int COLLECT = 2;
    private final int STORAGE = 3;
    private final int TO_WEBVIEW = 4;
    private List<String> tags;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == COMPLETED) {
                try {
                    poem_title.setText(poetryDetail.getTitle());    //诗词名
                    String author = poetryDetail.getDesty() + "." + poetryDetail.getAuthor();
                    poem_author.setText(author);                    //作者+朝代
                    String contentText = poetryDetail.getContent();
                    contentText = Spilt.spiltContentText(contentText);
                    poem_content.setText(contentText);    //原文

//                    图片
//                    Bitmap bitmap = getURLImage(poetryDetail.getImg());   //诗人图片
//                    author_img.setImageBitmap(bitmap);

//                    author_content.setText(poetryDetail.getAuthorDetail());   //诗人信息
                    String annotation = poetryDetail.getAnnotation();
                    htmlAnnotation.setHtml(annotation);                     //注释
                    appreciation.setText(poetryDetail.getAppreciation());   //赏析
                    translation.setText(poetryDetail.getTransContent());    //译文
//                    tags = spiltBySpace(poetryDetail.getTag());             //标签

//                    标签
//                    initTagFlowLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(DetailActivity.this, "显示失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = getContext();

        //在主线程访问网络连接
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        initView();

        setSupportActionBar(toolbar);//设置toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);//屏蔽toolbar默认标题显示

        toolbarTitle = getIntent().getStringExtra("toolbarTitle");
        String content = getIntent().getStringExtra("content");

        titleText.setText(toolbarTitle);

//        获取诗词详情
        getPoetryDetailByContent(content);

//        监听器
        //背诵
        test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), TestActivity.class);
                intent.putExtra("title", poetryDetail.getTitle());
                intent.putExtra("desty", poetryDetail.getDesty());
                intent.putExtra("author", poetryDetail.getAuthor());
                intent.putExtra("content", poetryDetail.getContent());
                startActivity(intent);
            }
        });

        //意见反馈
        poem_menu_advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdviceActivity.class);
                String poemId = String.valueOf(poetryDetail.getId());

                intent.putExtra("poemId", poemId);
                intent.putExtra("poemTitle", poetryDetail.getTitle());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.poem_titleBar);
        titleText = findViewById(R.id.poem_titleText);
        menu_line = findViewById(R.id.menu_line);
        htmlAnnotation = findViewById(R.id.annotation_text);
        translation = findViewById(R.id.translation_text);
        appreciation = findViewById(R.id.appreciation_text);
        test_button = findViewById(R.id.poem_test_button);
        poem_title = findViewById(R.id.detail_poem_title);
        poem_author = findViewById(R.id.detail_poem_author);
        poem_content = findViewById(R.id.detail_poem_content);
        poem_menu_advice = findViewById(R.id.poem_menu_advice);

        author_img = findViewById(R.id.author_img);
        author_content = findViewById(R.id.author_detail);
        scrollView = findViewById(R.id.detail_scroll);
        linearLayout = findViewById(R.id.poem_linnear);
        icon_collect = findViewById(R.id.poem_menu_icon);

    }

    //根据首句获取诗词详情
    public void getPoetryDetailByContent(String content) {
        String poetry_detail_url = constant.poetry_IP;

        //步骤五:构建Retrofit实例
        PoetryDetailRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(poetry_detail_url)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //步骤六:创建网络请求接口对象实例
        Api poetryDetailApi = PoetryDetailRetrofit.create(Api.class);

        //步骤七：对发送请求进行封装，传入接口参数
        retrofit2.Call<ResponseBody> poetryDetailDataCall = poetryDetailApi.getPoetryDetail(content);

        //步骤八:发送网络请求(同步)
        Log.e(TAG, "get == url：" + poetryDetailDataCall.request().url());
        new Thread(new Runnable() { //Android主线程不能操作网络请求,所以new一个线程来操作
            @Override
            public void run() {
                poetryDetailDataCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            String responseData = response.body().string();
                            parseJSONWithGSON(responseData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(DetailActivity.this, "获取诗词详情失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    //解析返回数据
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            responseObject = new JSONObject(jsonData);
            if (responseObject != null) {
                jsonObject = responseObject;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PoetryDetail detail = null;
        if (jsonObject != null) {//成功获取数据
            detail = gson.fromJson(jsonObject.toString(), PoetryDetail.class);
            poetryDetail = detail;
            Message message = new Message();
            message.what = COMPLETED;
            handler.sendMessage(message);
        } else//没有数据
        {
            Message message = new Message();
            message.what = TO_WEBVIEW;
            handler.sendMessage(message);
        }
    }
}