package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Api.Api;
import com.example.myapplication.Constant.constant;
import com.example.myapplication.R;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "test";
    public static final int MSG_LOGIN_ERR = 1; //登录错误
    public static final int MSG_CONNET_ERR = 2; //网络链接错误

    private Context context;
    private Retrofit RegisterRetrofit;

    private EditText r_number;
    private EditText r_password;
    private Button bt_register;

    private RegisterHandler register_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;
        InitView();
        register_handler = new RegisterHandler();
        Init();

//        StrictMode是指严格模式
        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);//这两句设置禁止所有检查
        }
    }

    private void InitView() {
        r_number = (EditText)findViewById(R.id.r_number);
        r_password = (EditText)findViewById(R.id.r_password);
        bt_register = (Button)findViewById(R.id.bt_register);
    }

    private void Init() {
        //设置提示的颜色
        r_number.setHintTextColor(getResources().getColor(R.color.white));
        r_password.setHintTextColor(getResources().getColor(R.color.white));


        //注册
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judge()) {
//                    postData();
                    registerInfo();
                }
            }
        });
    }

    /**注册*/
    private void registerInfo(){
        String username = r_number.getText().toString();
        String password = r_password.getText().toString();

        String register_url = constant.poetry_IP;

        //步骤五:创建Retrofit对象
        RegisterRetrofit = new Retrofit.Builder()
                .baseUrl(register_url) // 设置网络请求baseUrl
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析
                .build();

        // 步骤六:创建网络请求接口的实例
        Api registerApi = RegisterRetrofit.create(Api.class);

        //步骤七：对发送请求进行封装:传入参数
        Call<ResponseBody> registerCall = registerApi.getRegisterData(username, password);

        //步骤八:发送网络请求(异步)
        Log.e(TAG, "post == url：" + registerCall.request().url());
        //请求参数
        StringBuilder sb = new StringBuilder();
        if (registerCall.request().body() instanceof FormBody) {
            FormBody body = (FormBody) registerCall.request().body();
            for (int i = 0; i < body.size(); i++) {
                sb.append(body.encodedName(i))
                        .append(" = ")
                        .append(body.encodedValue(i))
                        .append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            Log.e(TAG, "RequestParams:{" + sb.toString() + "}");
        }

        registerCall.enqueue(new Callback<ResponseBody>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //步骤九：请求处理,输出结果
                String date = null;
                try {
                    date = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(date.equals("success")){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else {
                    Toast.makeText(RegisterActivity.this, "注册失败,该用户名已被使用", Toast.LENGTH_SHORT).show();
                }

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(TAG, "post回调失败：" + throwable.getMessage() + "," + throwable.toString());
                Toast.makeText(RegisterActivity.this, "post回调失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //判断登录信息是否合法
    private boolean judge() {
        if (TextUtils.isEmpty(r_number.getText().toString()) ) {
            Toast.makeText(context, "用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(r_password.getText().toString())) {
            Toast.makeText(context, "用户ID不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**事件捕获*/
    class RegisterHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_LOGIN_ERR:
                    r_number.setText("");
                    r_password.setText("");
                    r_number.requestFocus();

                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("注意")
                            .setMessage("用户名或密码输入不正确，请重新输入")
                            .setPositiveButton("确定",null)
                            .create()
                            .show();
                    bt_register.setEnabled(true);
                    break;
                case MSG_CONNET_ERR:
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("注意")
                            .setMessage("网络连接错误，请检查网络")
                            .setPositiveButton("确定",null)
                            .create()
                            .show();
                    break;
            }
        }
    }
}