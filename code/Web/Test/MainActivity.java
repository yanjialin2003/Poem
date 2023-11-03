package com.example.weblogindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button okBtn;
    EditText userNameET,pwdET;
    TextView tipTV;
    //食乐网登录地址
    String path = "http://www.sl777.cc/API/DataM/SQLData/userLogin";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(msg.what==1){
                String userInfo = String.valueOf(msg.obj);
                Log.e("handleMessage",userInfo);
                //解析返回的结果json字符串，获取data域
                JSONObject jo = null;
                String joData = "";
                try {
                    jo = new JSONObject(userInfo);
                    joData = jo.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if("".equals(joData) ||"[]".equals(joData)){
                    tipTV.setText(tipTV.getText()+"\n"+"用户名或密码错误，登陆失败！");
                }else {
                    Intent intent = new Intent(MainActivity.this, LoginOkActivity.class);
                    intent.putExtra("userInfo", userInfo);
                    startActivity(intent);
                }
            }else if(msg.what==0){
                tipTV.setText(String.valueOf(msg.obj));
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameET  = (EditText)findViewById(R.id.userName);
        pwdET = (EditText)findViewById(R.id.pwd);
        okBtn= (Button)findViewById(R.id.okBtn);
        tipTV =(TextView)findViewById(R.id.tipTV);

        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                final String userName = userNameET.getText().toString();
                final String pwd = pwdET.getText().toString();
                userNameET.setText("");
                pwdET.setText("");
                if(userName.equals("")||pwd.equals("")){
                    Toast.makeText(MainActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }else{
                    login(userName,pwd);  //调用登录方法
                    //loginwithOkHttpAsyn(userName,pwd);  //调用登录方法
                    //loginwithOkHttp(userName,pwd);  //调用登录方法
                }
            }
        });
    }

    //用户名：18772737825，密码：123456
    private void login(final String userName, final String pwd){
        Runnable runnable =new Runnable(){
            public void run(){
                try{

                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");//设置请求方式为POST
                    connection.setDoOutput(true);//允许写出
                    connection.setDoInput(true);//允许读入
                    connection.setUseCaches(false);//不使用缓存
                    connection.connect();
                    //拼接参数字符串，并使用输出流发向服务器
                    String param = "Param={'where':'account="+userName+" and password="+pwd+"'}";
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(param);
                    writer.close();
                    int responseCode = connection.getResponseCode();
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = connection.getInputStream();
                        String entityStr = is2String(inputStream);//将流转换为字符串
                        //Message msg = Message.obtain(handler, 1, entityStr);
                        //msg.sendToTarget();
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = entityStr;
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain(handler, 0, "网络连接异常");
                        msg.sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    //从输入流中读入所有的内容，并转成字符串返回
    public String is2String(InputStream is) throws IOException {
        //连接后，创建一个输入流来读取response
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        //每次读取一行，若非空则添加至 stringBuilder
        while((line = br.readLine()) != null){
            stringBuilder.append(line);
        }
        //读取所有的数据后，赋值给 response;
        String result = stringBuilder.toString().trim();
        return result;
    }

    /**
     * 登录//用户名：18772737825，密码：123456
     * 同步，post方式，注意写入的参数参考 login()方法
     */
    private void loginwithOkHttp(final String userName, final String pwd) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    OkHttpClient okHttpClient = new OkHttpClient();
                    MediaType contentType = MediaType.parse("text/x-markdown; charset=utf-8");
                    String param = "Param={'where':'account="+userName+" and password="+pwd+"'}";
                    RequestBody body = RequestBody.create(contentType, param);
                    Request request = new Request.Builder().url(url).post(body).build();
                    Call call = okHttpClient.newCall(request);
                    Response response = call.execute();

                    int responseCode = response.code();
                    if(responseCode == 200){
                        InputStream inputStream = response.body().byteStream();
                        String entityStr = is2String(inputStream);//将流转换为字符串
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = entityStr;
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain(handler, 0, "网络连接异常");
                        msg.sendToTarget();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(runnable).start();

    }

    /**
     * 登录//用户名：18772737825，密码：123456
     * 异步，post方式，注意写入的参数参考 login()方法
     */
    private void loginwithOkHttpAsyn(final String userName, final String pwd) {

        URL url = null;
        try {
            url = new URL(path);
            OkHttpClient okHttpClient = new OkHttpClient();
            MediaType contentType = MediaType.parse("text/x-markdown; charset=utf-8");
            String param = "Param={'where':'account="+userName+" and password="+pwd+"'}";
            RequestBody body = RequestBody.create(contentType, param);
            Request request = new Request.Builder().url(url).post(body).build();
            Call call = okHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error","e");
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        int responseCode = response.code();
                        if(responseCode == 200){
                            InputStream inputStream = response.body().byteStream();
                            String entityStr = is2String(inputStream);//将流转换为字符串
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = entityStr;
                            handler.sendMessage(msg);
                        }else{
                            Message msg = Message.obtain(handler, 0, "网络连接异常");
                            msg.sendToTarget();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}