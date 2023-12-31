package com.example.myapplication.Modal;

import android.util.Log;

import com.example.myapplication.Api.Api;
import com.example.myapplication.Constant.constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PoetryUtil {

    private static final String TAG = "test";

    static Retrofit GetDynastyRetrofit;

    static List<String> styles = Arrays.asList("五言", "七言");
    static List<String> poem_tags = Arrays.asList("绝句", "律诗");

    static HashMap<String, String> poem_type = new HashMap<String, String>();

    static {
        poem_type.put("绝句", "绝句");
        poem_type.put("律诗", "律诗");
    }

    static HashMap<String, String> poemStyle = new HashMap<String, String>();

    static {
        poemStyle.put("五言", "五言");
        poemStyle.put("七言", "七言");
    }

    static List<String> ci_pai = Arrays.asList("归字谣", "如梦令", "梧桐影", "渔歌子", "捣练子", "忆江南", "忆王孙", "河满子");

    static String[] type =
            {
                    "诗",
                    "词",
                    "曲",
                    "文言文"
            };

    static String[] dynasty = {
            "两汉",
            "五代",
            "元代",
            "先秦",
            "南北朝",
            "唐代",
            "宋代",
            "当代",
            "明代",
            "未知",
            "清代",
            "现代",
            "近代",
            "金朝",
            "隋代",
            "魏晋"
    };

    static String[] style = {
            "写景",
            "咏物",
            "春天",
            "夏天",
            "秋天",
            "冬天",
            "写雨",
            "写雪",
            "写风",
            "写花",
            "梅花",
            "荷花",
            "菊花",
            "柳树",
            "月亮",
            "山水",
            "写山",
            "写水",
            "长江",
            "黄河",
            "儿童",
            "写鸟",
            "写马",
            "田园",
            "边塞",
            "地名",
            "抒情",
            "爱国",
            "离别",
            "送别",
            "思乡",
            "思念",
            "爱情",
            "励志",
            "哲理",
            "闺怨",
            "悼亡",
            "写人",
            "老师",
            "母亲",
            "友情",
            "战争",
            "读书",
            "惜时",
            "婉约",
            "豪放",
            "诗经",
            "民谣",
            "节日",
            "春节",
            "元宵节",
            "寒食节",
            "清明节",
            "端午节",
            "七夕节",
            "中秋节",
            "重阳节",
            "忧国忧民",
            "咏史怀古",
            "宋词精选",
            "小学古诗",
            "初中古诗",
            "高中古诗",
            "小学文言文",
            "初中文言文",
            "高中文言文",
            "古诗十九首",
            "唐诗三百首",
            "古诗三百首",
            "宋词三百首"
    };


    public static List<String> getTypeList() {
        List<String> typeTagList = new ArrayList<String>();
        typeTagList.add("诗");
        typeTagList.add("词");
        typeTagList.add("曲");
        typeTagList.add("文言文");
        return typeTagList;
    }

    public static String[] getDynastyList() {

        List<String> dynastyList = new ArrayList<String>();

        String search_url = constant.poetry_IP;

        //步骤五:构建Retrofit实例
        GetDynastyRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(search_url)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //步骤六:创建网络请求接口对象实例
        Api getDynastyApi = GetDynastyRetrofit.create(Api.class);

        //步骤七：对发送请求进行封装，传入接口参数
        retrofit2.Call<ResponseBody> getDynastyListCall = getDynastyApi.getDynastyList();

        //步骤八:发送网络请求(同步)
        Log.e(TAG, "get == url：" + getDynastyListCall.request().url());
        Thread thread_dynasty = null;
        thread_dynasty = new Thread(new Runnable() { //Android主线程不能操作网络请求,所以new一个线程来操作
            @Override
            public void run() {
                getDynastyListCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        String responseData = null;
                        try {
                            responseData = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        List list = gson.fromJson(responseData, new TypeToken<List>(){}.getType());
                        dynastyList.addAll(list);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
        thread_dynasty.start();
        return dynastyList.toArray(new String[dynastyList.size()]);
    }

    public static List<String> getStyleList() {

        List<String> styleList = new ArrayList<String>();

//        HttpUtil.sendOkHttpRequest(SysConstant.YA_FENG_SERVER+"/yafeng-1.0/poetry/getAllPoemStyle", new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseData = response.body().string();
//                try {
//                    resultObject = new JSONObject(responseData);
//                    try {
//                        JSONArray jsonArray;
//                        jsonArray = new JSONArray(resultObject.getString("data"));
//                        String array = jsonArray.get(0).toString();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            styleList.add(jsonArray.get(i).toString());
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        return styleList;
    }


    public static String[] getDynasty() {
        return dynasty;
    }

    public static String[] getStyle() {
        return style;
    }

    public static String[] getType() {
        return type;
    }

    public static List<String> getStyles() {
        return styles;
    }

    public static List<String> getPoemTags() {
        return poem_tags;
    }

    public static List<String> getCiPai() {
        return ci_pai;
    }

    public static HashMap<String, String> getPoemType() {
        return poem_type;
    }

    public static HashMap<String, String> getPoemStyle() {
        return poemStyle;
    }

}
