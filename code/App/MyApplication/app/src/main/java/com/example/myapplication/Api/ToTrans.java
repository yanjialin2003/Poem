package com.example.myapplication.Api;

import com.example.myapplication.Modal.EmotionResBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ToTrans {
    @Multipart
    @POST("/poetry/emotion")
    Call<EmotionResBean> getCall(@Part List<MultipartBody.Part> partList);
        // @GET注解的作用:采用Get方法发送网络请求
        // getCall() = 接收网络请求数据的方法
        // 其中返回类型为Call<*>，*是接收数据的类（即上面定义的TranslationBean类），返回这个类的对象
}

