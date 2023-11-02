package com.example.myapplication.Activity;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Adapter.MsgAdapter;
import com.example.myapplication.Modal.ClientMessageBean;
import com.example.myapplication.Modal.SystemMessageBean;
import com.example.myapplication.R;
import com.example.myapplication.Util.FileUtils;
import com.example.myapplication.Util.WebSocketUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private List<ClientMessageBean> msgList = new ArrayList<>();
    private String username;
    private int rid;
    static int numOfCaches= 1;
    //此处待修改
    private String url = "ws://101.33.242.218:8081/chat/";
    private EditText inputText;
    private Button send;
    private Button sendVoiceButton;
    private ImageButton changeToVoice;
    private ImageButton changeToText;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    LinearLayout sendVoice;
    LinearLayout sendText;
    private LinearLayoutManager layoutManager;
    private WebSocketUtil webSocketUtil;
    //语音操作对象
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    //语音文件保存路径
    private String FileName = null;
    //接受消息，更新界面
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            SystemMessageBean systemMessageBean = (SystemMessageBean) message.obj;
            Gson gson = new Gson();
            ClientMessageBean messageBean ;
            if(systemMessageBean.getFrom() == null){
                messageBean = new ClientMessageBean(0,null,systemMessageBean.getMessage(),null);
            }else if (!systemMessageBean.getFrom().equals(username)){
                messageBean = gson.fromJson(systemMessageBean.getMessage(),ClientMessageBean.class);
                messageBean.setType(messageBean.getType()+1);
            }else{
                messageBean = gson.fromJson(systemMessageBean.getMessage(),ClientMessageBean.class);
            }
            if(messageBean.getRecord()!=null){
                try {
                    byte[] bytes = messageBean.getRecord();
                    FileUtils.getFileByBytes(bytes, getCacheDir().getPath()+"/",numOfCaches+".amr");
                    messageBean.setMessage(getCacheDir().getPath()+"/"+numOfCaches+".amr");
                    numOfCaches++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            msgList.add(messageBean);
            //当有新消息，刷新RecyclerVeiw的显示
            adapter.notifyItemInserted(msgList.size() - 1);
            //将RecyclerView定位到最后一行
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
            //清空输入框内容
            inputText.setText("");
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view);
        mPlayer = new MediaPlayer();
        FileName = getCacheDir() + "/amrsend.amr";
        //初始化控件
        initView();
        createConnection();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击发送后新建信息，发送给服务器
                String content = inputText.getText().toString();
                ClientMessageBean messageBean = new ClientMessageBean(
                        1,
                        username,
                        content,
                        null
                );
                Gson gson = new Gson();
                webSocketUtil.send(gson.toJson(messageBean));
            }
        });

        //切换发送语音和文本
        changeToVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendText.setVisibility(View.GONE);
                sendVoice.setVisibility(View.VISIBLE);
            }
        });
        changeToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVoice.setVisibility(View.GONE);
                sendText.setVisibility(View.VISIBLE);
            }
        });
        //设置发送语音监听
        sendVoiceButton.setOnTouchListener(sendVoiceListener);

    }

    public void initView(){
        sendText = findViewById(R.id.text_bottom);
        sendVoice = findViewById(R.id.voice_bottom);

        inputText = (EditText)findViewById(R.id.input_txet);
        send = (Button) findViewById(R.id.send);
        sendVoiceButton = findViewById(R.id.record_voice);
        changeToVoice = findViewById(R.id.change_to_voice);
        changeToText = findViewById(R.id.back_to_text);

        //首先隐藏发送语音模块
        sendVoice.setVisibility(View.GONE);

        msgRecyclerView = (RecyclerView)findViewById(R.id.msg_recycle_view);
        layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList,mPlayer);
        msgRecyclerView.setAdapter(adapter);
    }

    void createConnection(){
        //获取进入房间号和用户名
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        rid = intent.getIntExtra("rid",0);

        webSocketUtil = new WebSocketUtil();
        webSocketUtil.connect(url,rid,username,handler);
    }


    //这里记得断开连接
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocketUtil.disconnect();
    }
    private OnTouchListener sendVoiceListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float beginX,beginY=0,finalX,finalY;
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendVoiceButton.setText("正在录音");
                    sendVoiceButton.setBackgroundColor(Color.RED);
                    beginX = motionEvent.getX();
                    beginY = motionEvent.getY();
                    Log.e("MainActivity", "beginX:" + beginX);
                    Log.e("MainActivity", "beginY:" + beginY);
                    Log.e("MainActivity", "ACTION_DOWN");
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setOutputFile(FileName);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e("MainActivity", "prepare() failed" + e);
                    }
                    mRecorder.start();
                    break;
                case MotionEvent.ACTION_UP:
                    sendVoiceButton.setText("按住说话");
                    sendVoiceButton.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                    finalX = motionEvent.getX();
                    finalY = motionEvent.getY();
                    Log.e("MainActivity", "finalX:" + finalX);
                    Log.e("MainActivity", "finalY:" + finalY);
                    Log.e("MainActivity", "ACTION_UP");
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    if ((beginY - finalY) > 50) {
                    Log.e("MainActivity", "执行删除");
                    new File(FileName).delete();
                    Toast.makeText( ChatActivity.this,"取消发送", LENGTH_SHORT).show();
                    Log.e("MainActivity", "执行删除后文件是否还存在：" + new File(FileName).exists());
                    }
                    if(new File(FileName).exists()){
                        ClientMessageBean messageBean = new ClientMessageBean(
                                3,
                                username,
                                null,
                                FileUtils.getBytesByFile(new File(FileName))
                        );
                        Gson gson = new Gson();
                        webSocketUtil.send(gson.toJson(messageBean));
                    }
                break;
                default:
                    break;
            }
            return true;
        }
    };
}