package com.example.myapplication.Activity;

import static com.xuexiang.xui.XUI.getContext;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.PoemListAdapter;
import com.example.myapplication.Api.Api;
import com.example.myapplication.Constant.constant;
import com.example.myapplication.Modal.Poetry;
import com.example.myapplication.R;
import com.example.myapplication.Util.Spilt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PoetryListActivity extends AppCompatActivity {

    private Retrofit SearchRetrofit;
    private Retrofit SearchByTagRetrofit;
    private static final String TAG = "test";

    private PoemListAdapter poemListAdapter;
    private ListView poemListView;
    private Toolbar toolbar;
    private TextView titleText;
    private String toolbarTitle;
    private final int COMPLETED = 1;
    private final int TO_WEBVIEW = 2;
    private RefreshLayout refreshLayout;
    //    每次20首
    private Integer size = 20;
    private Integer page = 0;
    private List<Poetry> poetryList = new ArrayList<>();
    private boolean isLoad = false;
    private JSONObject responseObject;
    private String from;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == COMPLETED) { //绑定数据
                try {
                    poemListAdapter = new PoemListAdapter(poetryList, getContext());
                    poemListView.setAdapter(poemListAdapter);
                    refreshLayout.finishRefresh();//结束刷新
                    refreshLayout.finishLoadMore(2000);//结束加载
                    isLoad = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (message.what == TO_WEBVIEW)//跳转至网站
            {
                Toast.makeText(getContext(), "暂时没有数据哦,请重新选择标签", Toast.LENGTH_LONG).show();
            }
        }

    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                isLoad = false;
                poetryList.clear();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poetry_list);

        //        哪种搜索类型（搜索框/标签）
        from = getIntent().getStringExtra("from");

        toolbar = findViewById(R.id.titleBar);
        poemListView = findViewById(R.id.poemList);
        titleText = findViewById(R.id.titleText);
        setSupportActionBar(findViewById(R.id.titleBar));//设置toolbar

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableScrollContentWhenLoaded(true);


        poemListAdapter = new PoemListAdapter(poetryList, getContext());
        poemListView.setAdapter(poemListAdapter);
//        poemListView.setEmptyView(findViewById(R.id.empty_imageview_iv));

        getSupportActionBar().setDisplayShowTitleEnabled(false);//屏蔽toolbar默认标题显示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }//设置返回图标显示

        toolbarTitle = getIntent().getStringExtra("toolBarTitle");
        titleText.setText(toolbarTitle);
        refreshLayout.autoRefresh();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (from.equals("collect")) {
                    String temp = getIntent().getStringExtra("collect");
                    Log.d("TEMPPPPPPPPP", temp);
                    Gson gson = new Gson();
                    List<Poetry> poetries = gson.fromJson(temp, new TypeToken<List<Poetry>>() {
                    }.getType());
                    poetryList = poetries;
                    poemListAdapter = new PoemListAdapter(poetryList, getContext());
                    poemListView.setAdapter(poemListAdapter);
                    refreshlayout.finishRefresh();
                } else if (from.equals("filter")) {
                    String tag = getIntent().getStringExtra("tag");
                    String dynasty = getIntent().getStringExtra("dynasty");
                    getPoemListByTag(tag, dynasty, page, size);
                } else if (from.equals("search")) {
                    String key = getIntent().getStringExtra("key");
                    getPoemListBySearch(key, page, size);
                } else if (from.equals("detail")) {
                    String tag = getIntent().getStringExtra("tag");
                    getPoemListByTag(tag, "null", page, size);
                }
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (from.equals("collect")) {
                    String temp = getIntent().getStringExtra("collect");
                    Log.d("TEMPPPPPPPPP", temp);
                    Gson gson = new Gson();
                    List<Poetry> poetries = gson.fromJson(temp, new TypeToken<List<Poetry>>() {
                    }.getType());
                    poetryList = poetries;
                    poemListAdapter = new PoemListAdapter(poetryList, getContext());
                    poemListView.setAdapter(poemListAdapter);
                    refreshlayout.finishRefresh();
                    refreshlayout.finishLoadMore(2000);
                } else if (from.equals("filter")) {
                    page++;
                    String tag = getIntent().getStringExtra("tag");
                    String dynasty = getIntent().getStringExtra("dynasty");
                    getPoemListByTag(tag, dynasty, page, size);
                } else if (from.equals("search")) {
                    page++;
                    String key = getIntent().getStringExtra("key");
                    getPoemListBySearch(key, page, size);
                } else if (from.equals("detail")) {
                    page++;
                    String tag = getIntent().getStringExtra("tag");
                    getPoemListByTag(tag, "null", page, size);
                }

            }
        });

        poemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PoetryListActivity.this, DetailActivity.class);

                String poemText = poetryList.get(position).getContent();
                String sentence = Spilt.spiltPoemTextByEnter(poemText)[0];
                intent.putExtra("content", sentence);
                intent.putExtra("toolbarTitle", poetryList.get(position).getName());
                startActivity(intent);
            }
        });
    }

    //根据标签获取诗词
    private void getPoemListByTag(String tag, String dynasty, Integer page, Integer size) {
        String searchByTag_url = constant.poetry_IP;

        //步骤五:构建Retrofit实例
        SearchByTagRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(searchByTag_url)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //步骤六:创建网络请求接口对象实例
        Api poetryApi = SearchByTagRetrofit.create(Api.class);

        //步骤七：对发送请求进行封装，传入接口参数
        retrofit2.Call<ResponseBody> poetryDataCall = poetryApi.getPoetrysByTag(dynasty, tag, page, size);

        //步骤八:发送网络请求(同步)
        Log.e(TAG, "post == url：" + poetryDataCall.request().url());
        new Thread(new Runnable() { //Android主线程不能操作网络请求,所以new一个线程来操作
            @Override
            public void run() {
                poetryDataCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                        String responseData = null;
                        try {
                            responseData = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        parseJSONWithGSON(responseData);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(PoetryListActivity.this, "get回调失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    //根据搜索关键字获取诗词
    private void getPoemListBySearch(String key, Integer page, Integer size) {
        String search_url = constant.poetry_IP;

        //步骤五:构建Retrofit实例
        SearchRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(search_url)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //步骤六:创建网络请求接口对象实例
        Api poetryApi = SearchRetrofit.create(Api.class);

        //步骤七：对发送请求进行封装，传入接口参数
        retrofit2.Call<ResponseBody> poetryDataCall = poetryApi.getPoetrys(key, page, size);

        //步骤八:发送网络请求(同步)
        Log.e(TAG, "post == url：" + poetryDataCall.request().url());
        new Thread(new Runnable() { //Android主线程不能操作网络请求,所以new一个线程来操作
            @Override
            public void run() {
                poetryDataCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                        String responseData = null;
                        try {
                            responseData = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        parseJSONWithGSON(responseData);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(PoetryListActivity.this, "get回调失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        List<Poetry> list = gson.fromJson(jsonData, new TypeToken<List<Poetry>>(){}.getType());
        if (list.size() != 0)//数据获取成功
        {
            poetryList.addAll(list);
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