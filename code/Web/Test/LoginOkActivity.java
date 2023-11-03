package com.example.weblogindemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginOkActivity extends AppCompatActivity {

    TextView userInfoTV, srcInfoTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ok);
        userInfoTV = (TextView)findViewById(R.id.userInfo);
        srcInfoTV = (TextView)findViewById(R.id.srcInfo);
        Intent intent=this.getIntent();
        Bundle bundle = intent.getExtras();
        String userInfo = bundle.getString("userInfo");
        StringBuffer sb = new StringBuffer();
        try {
            JSONObject jo = (JSONObject) new JSONObject(userInfo);
            JSONObject userJo = (JSONObject) jo.getJSONArray("data").get(0);
            sb.append("账号：").append(userJo.getString("account")).append("\n");
            sb.append("密码：").append(userJo.getString("password")).append("\n");
            sb.append("电话：").append(userJo.getString("telephone")).append("\n");
            sb.append("昵称：").append(userJo.getString("nickName")).append("\n");
            sb.append("创建时间：").append(userJo.getString("createTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userInfoTV.setText(sb.toString());
        srcInfoTV.setText(userInfo);
    }
}