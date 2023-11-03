package com.example.weblogindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private String PATH_URL = "https://geoapi.qweather.com/v2/city/lookup" ;
    String key = "bc0418b57b2d4918819d3974ac1285d9";
    private TextView srcInfoTV;
    private EditText locationET;
    private Button searchBT;
    private ProgressDialog dialog;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            searchBT.setEnabled(true);
            dialog.dismiss();
            if(msg.what==1){
                srcInfoTV.setText(String.valueOf(msg.obj));//待改进
            }else if(msg.what==0){
                srcInfoTV.setText(String.valueOf(msg.obj));
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        locationET = (EditText)findViewById(R.id.locationInfo);
        srcInfoTV = (TextView)findViewById(R.id.srcInfo);
        searchBT = (Button) findViewById(R.id.search);

        searchBT.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(locationET.getText())) {
                    visitwithOkHttp(PATH_URL, locationET.getText().toString(), key);
                    searchBT.setEnabled(false);
                    srcInfoTV.setText("正在加载中......");
                    showPD();
                }
            }
        });
    }

    private void visitwithOkHttp(final String PATH_URL,final String location,final String key) {
        //get请求方式
        String login_check_url = PATH_URL +"?location="+location+"&key="+key;
        Log.e("LoginActivity",login_check_url);
        okhttp3.Callback callback = new okhttp3.Callback()
        {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("LoginActivity","onResponse");
                Log.e("LoginActivity",responseData);
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = responseData;
                handler.sendMessage(msg);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                String responseData = "网络连接异常!";
                Log.e("LoginActivity","onFailure");
                Log.e("LoginActivity",responseData);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = responseData;
                handler.sendMessage(msg);
            }
        };
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(login_check_url)
                .build();
        //发送请求
        client.newCall(request).enqueue(callback);
    }

    private void showPD() {

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//转盘
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("提示");
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Toast.makeText(LoginActivity.this, "消失了", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setMessage("正在加载，请稍后……");
        dialog.show();

    }
}