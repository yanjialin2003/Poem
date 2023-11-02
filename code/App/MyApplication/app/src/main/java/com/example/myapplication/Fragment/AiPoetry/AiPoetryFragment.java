package com.example.myapplication.Fragment.AiPoetry;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Api.Api;
import com.example.myapplication.Constant.constant;
import com.example.myapplication.Modal.PoetryUtil;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentAiPoetryBinding;
import com.example.myapplication.Util.Spilt;

import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AiPoetryFragment extends Fragment {

    private FragmentAiPoetryBinding binding;

    private static final String TAG = "test";

    private Retrofit CreateRetrofit;

    private Button startButton;
    private MaterialEditText inputText;
    private TextView createdText;

    private TagFlowLayout poem_tag_flow;
    private List<String> poem_tags;
    private TagFlowLayout poem_style_flow;

    private HashMap<String, String> poem_type;
    private HashMap<String, String> poem_style;
    private HashMap<String, String> selectedTag = new HashMap<>();

    private List<String> styles;

    private String createdPoem = "";

    private final int GET_POEM_FAIL = 0;
    private final int GET_POEM_SUCCESS = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == GET_POEM_SUCCESS) {
                String contentText = Spilt.spiltContentText(createdPoem);
                createdText.setText(contentText);
            }
            if (message.what == GET_POEM_FAIL) {
                createdText.setText(" ");
                Toast.makeText(getContext(), "服务错误，请重试", Toast.LENGTH_LONG).show();
            }
        }

    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAiPoetryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initView();//初始化视图

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createdText.setText("加载中...");

                String form =  selectedTag.get("yan") + selectedTag.get("type");
                String head = inputText.getEditValue();

                String create_url = constant.poetry_IP;

                //步骤五:构建Retrofit实例
                CreateRetrofit = new Retrofit.Builder()
                        //设置网络请求BaseUrl地址
                        .baseUrl(create_url)
                        //设置数据解析器
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                //步骤六:创建网络请求接口对象实例
                Api createApi = CreateRetrofit.create(Api.class);

                //步骤七：对发送请求进行封装，传入接口参数
                retrofit2.Call<ResponseBody> createDataCall = createApi.createPoetry(form,head);

                //步骤八:发送网络请求(同步)
                Log.e(TAG, "get == url：" + createDataCall.request().url());
                new Thread(new Runnable() { //Android主线程不能操作网络请求,所以new一个线程来操作
                    @Override
                    public void run() {
                        createDataCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                String responseData = null;
                                try {
                                    responseData = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                createdPoem = responseData;

                                Message message = new Message();
                                message.what = GET_POEM_SUCCESS;
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                                t.printStackTrace();
                                Message message = new Message();
                                message.what = GET_POEM_FAIL;
                                handler.sendMessage(message);
                            }
                        });
                    }
                }).start();
            }
        });

        return root;
    }

    private void initView() {
        poem_tags = PoetryUtil.getPoemTags();
        poem_type = PoetryUtil.getPoemType();

        poem_style = PoetryUtil.getPoemStyle();
        styles = PoetryUtil.getStyles();

        poem_tag_flow = binding.aiPoemTagFlow;
        poem_style_flow = binding.aiStyleTagFlow;

        inputText = binding.inputKeyword;
        startButton = binding.creatingButton;

        createdText = binding.createdContent;

        final LayoutInflater mInflater = getLayoutInflater();

        poem_tag_flow.setAdapter(new TagAdapter<String>(poem_tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {

                TextView tv = (TextView) mInflater.inflate(R.layout.adapter_item_tag,
                        poem_tag_flow, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedTag.put("type", poem_type.get(poem_tags.get(position)));

                TextView textView = (TextView) view;
                textView.setTextColor(Color.WHITE);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                TextView textView = (TextView) view;
                selectedTag.remove("type");

                showSelected(selectedTag);
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        poem_style_flow.setAdapter(new TagAdapter<String>(styles) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {

                TextView tv = (TextView) mInflater.inflate(R.layout.adapter_item_tag,
                        poem_style_flow, false);
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                selectedTag.put("yan", poem_style.get(styles.get(position)).toString());

                TextView textView = (TextView) view;
                textView.setTextColor(Color.WHITE);
            }

            @Override
            public void unSelected(int position, View view) {
                super.unSelected(position, view);
                TextView textView = (TextView) view;
                selectedTag.remove("yan");

                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    private void showSelected(HashMap<String, String> temp) {
        if (temp.size() > 0) {
            for (String tempString : temp.keySet())
                System.out.println(tempString + ": " + temp.get(tempString));
        } else
            System.out.println("没有选择");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}