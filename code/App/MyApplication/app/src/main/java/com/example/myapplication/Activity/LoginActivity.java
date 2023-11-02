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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "test";
    public static final int MSG_LOGIN_ERR = 1; //登录错误
    public static final int MSG_CONNET_ERR = 2; //网络链接错误

    private Context context;
    private Retrofit LoginRetrofit;

    private EditText et_number;
    private EditText et_password;
    private Button bt_login;
    private Button bt_register;

    private LoginHandler login_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        InitView();
        login_handler = new LoginHandler();
        Init();

//        StrictMode是指严格模式
        if (Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);//这两句设置禁止所有检查
        }
    }

    private void InitView() {
        et_number = (EditText)findViewById(R.id.et_number);
        et_password = (EditText)findViewById(R.id.et_password);
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_register = (Button) findViewById(R.id.bt_register);
    }

    private void Init() {
        //设置提示的颜色
        et_number.setHintTextColor(getResources().getColor(R.color.white));
        et_password.setHintTextColor(getResources().getColor(R.color.white));


        //登录
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judge()) {
                    loginInfo();
                }
            }
        });

        //注册
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "注册", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    /**登录*/
    private void loginInfo() {
        String username = et_number.getText().toString();
        String password = et_password.getText().toString();

        String login_url = constant.poetry_IP;

        //步骤五:构建Retrofit实例
        LoginRetrofit = new Retrofit.Builder()
                //设置网络请求BaseUrl地址
                .baseUrl(login_url)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //步骤六:创建网络请求接口对象实例
        Api loginApi = LoginRetrofit.create(Api.class);

        //步骤七：对发送请求进行封装，传入接口参数
        Call<ResponseBody> loginDataCall = loginApi.getData(username, password);

        //步骤八:发送网络请求(同步)
        Log.e(TAG, "post == url：" + loginDataCall.request().url());
        new Thread(new Runnable() { //Android主线程不能操作网络请求,所以new一个线程来操作
            @Override
            public void run() {
                loginDataCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String date = new String(response.body().bytes());
                            if(date.equals("success")){
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "登录失败,请检查你的用户名和密码", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "get回调失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }

    /**判断登录信息是否合法*/
    private boolean judge() {
        if (TextUtils.isEmpty(et_number.getText().toString()) ) {
            Toast.makeText(context, "用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            Toast.makeText(context, "用户ID不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**事件捕获*/
    class LoginHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_LOGIN_ERR:
                    et_number.setText("");
                    et_password.setText("");
                    et_number.requestFocus();

                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("注意")
                            .setMessage("用户名或密码输入不正确，请重新输入")
                            .setPositiveButton("确定",null)
                            .create()
                            .show();
                    bt_login.setEnabled(true);
                    break;
                case MSG_CONNET_ERR:
                    new AlertDialog.Builder(LoginActivity.this)
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