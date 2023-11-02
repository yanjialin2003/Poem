package com.example.myapplication.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @创建者 晏嘉琳
 * @创建时间 2023/5/31 17:44
 * @类描述 ${TODO}步骤四：创建网络接口类(封装Url地址和网络数据请求)
 */
public interface Api {
    //这里特别说明Url的组成，retrofit把网络请求的Url分成两部分设置：第一部分在创建Retrofit实例时通过.baseUrl()设置，
    //第二部分在网络接口注解中设置，如下面的"/user"，网络请求的完整地址Url = Retrofit实例.baseUrl()+网络请求接口注解()

    @GET("user/loginIn")
    Call<ResponseBody> getData(@Query("username") String username, @Query("password") String password);

    @GET("user/register")
    Call<ResponseBody> getRegisterData(@Query("username") String username, @Query("password") String password);

    @GET("poetry/getOnePoem")
    Call<ResponseBody> getPoetry();

    @GET("poetry/getByContentDetail")
    Call<ResponseBody> getPoetryDetail(@Query("content") String content);

    @GET("poetry/getBySearch")
    Call<ResponseBody> getPoetrys(@Query("key") String key, @Query("page") Integer page, @Query("size") Integer size);

    @GET("poetry/getAllDynasty")
    Call<ResponseBody> getDynastyList();

    @GET("poetry/getByTag")
    Call<ResponseBody> getPoetrysByTag(@Query("dynasty") String dynasty, @Query("tag") String tag, @Query("page") Integer page, @Query("size") Integer size);

    @GET("poetry/generate")
    Call<ResponseBody> createPoetry(@Query("form") String form, @Query("head") String head);

    //post请求
    @FormUrlEncoded
    @POST("api/comments.163")
    Call<Object> postDataCall(@Field("format") String format);

    @FormUrlEncoded
    @POST("user/register")
    Call<Object> registerCall(@Field("username") String username, @Field("password") String password);

}
