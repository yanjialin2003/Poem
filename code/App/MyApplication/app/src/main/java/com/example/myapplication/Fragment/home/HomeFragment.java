package com.example.myapplication.Fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.DetailActivity;
import com.example.myapplication.Activity.PoetryListActivity;
import com.example.myapplication.Activity.TestActivity;
import com.example.myapplication.Api.Api;
import com.example.myapplication.Constant.constant;
import com.example.myapplication.Util.Spilt;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.textview.label.LabelButtonView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private static final String TAG = "test";

    //    获取诗词
    private final int COMPLETED = 1;

    private Retrofit SearchRetrofit;

    private JSONObject poetryObject;

    private SmartRefreshLayout refreshLayout;

    private TextView title;
    private TextView author;
    private TextView content;

    private TextView sentence;
    private TextView tag1;
    private TextView tag2;

    private LabelButtonView detail;
    private ImageView ic_menu;
    private TextView menu_line;

    private MaterialEditText searchText;
    private ImageView search;

    private Button testButton;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
//            获取每日诗词
            if (message.what == COMPLETED) { //获取每日诗词成功
                try {

                    title.setText(poetryObject.get("title").toString());

                    String dynasty_author = poetryObject.get("desty").toString() + " . "
                            + poetryObject.get("author").toString();
                    author.setText(dynasty_author);

                    String contentText = poetryObject.get("content").toString();
                    contentText = Spilt.spiltContentText(contentText);
                    content.setText(contentText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Log.e(TAG,"设置诗词失败");
            }
        }

    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initView();//初始化视图

        getOnePoetry();//获取每日诗词

//        设置监听
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getOnePoetry();
                refreshLayout.finishRefresh();//结束刷新
                refreshLayout.finishLoadMore(2000);//结束加载
            }
        });

        //将数据传递给背诵页面
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getContext(), TestActivity.class);
                try {
                    intent.putExtra("title", poetryObject.get("title").toString());
                    intent.putExtra("desty", poetryObject.get("desty").toString());
                    intent.putExtra("author", poetryObject.get("author").toString());
                    intent.putExtra("content", poetryObject.get("content").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        //搜索
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PoetryListActivity.class);
                intent.putExtra("from", "search");
                intent.putExtra("toolBarTitle", searchText.getEditValue());
                intent.putExtra("key", searchText.getEditValue());
                startActivity(intent);
            }
        });

        //查看详情
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailActivity.class);

                String poemText = content.getText().toString();
                String sentence = Spilt.spiltPoemTextByEnter(poemText)[0];
                intent.putExtra("content", sentence);
                intent.putExtra("toolbarTitle", title.getText().toString());
                startActivity(intent);
            }
        });
        return root;
    }

    private void initView() {
        refreshLayout = binding.refreshLayout;
        refreshLayout.setEnableScrollContentWhenLoaded(true);

        title = binding.title;
        author = binding.author;
        content = binding.content;

        sentence = binding.sentence;
        tag1 = binding.tag1;
        tag2 = binding.tag2;

        testButton = binding.testButton;

        ic_menu = binding.menuIcon;
        menu_line = binding.menuLine;

        search = binding.searchButton;
        searchText = binding.search;
        detail = binding.detail;
    }

    //随机获取一首古诗
    private void getOnePoetry() {
        String search_url = constant.poetry_IP;

        //步骤五:构建Retrofit实例
        SearchRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(search_url)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //步骤六:创建网络请求接口对象实例
        Api searchApi = SearchRetrofit.create(Api.class);

        //步骤七：对发送请求进行封装，传入接口参数
        retrofit2.Call<ResponseBody> searchDataCall = searchApi.getPoetry();

        //步骤八:发送网络请求(同步)
        Log.e(TAG, "get == url：" + searchDataCall.request().url());
        new Thread(new Runnable() { //Android主线程不能操作网络请求,所以new一个线程来操作
            @Override
            public void run() {
                searchDataCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            String responseData = response.body().string();
                            poetryObject = new JSONObject(responseData);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = COMPLETED;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "获取每日诗词失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}