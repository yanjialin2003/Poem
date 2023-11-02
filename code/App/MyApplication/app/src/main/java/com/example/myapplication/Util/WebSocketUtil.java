package com.example.myapplication.Util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Modal.ClientMessageBean;
import com.example.myapplication.Modal.SystemMessageBean;
import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketUtil {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private final OkHttpClient client = new OkHttpClient.Builder()
                                        .pingInterval(10, TimeUnit.SECONDS)
                                        .build();
    private WebSocket socket;
    private WebSocketListener listener;
    private Map<String, WebSocket> webSockets = new ConcurrentHashMap<>();

    private Handler handler;

    public void connect(String url, Integer rid,String username, Handler handler) {
        this.handler = handler;
        url = url + rid+"/"+username;
        Request request = new Request.Builder()
                .url(url)
                .build();
        listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("open");
                socket = webSocket;
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // 接收到服务端发来的消息
                Log.d("WebSocket", "onMessage: " + text);
                Message message = new Message();
                Gson gson = new Gson();
                SystemMessageBean messageBean = gson.fromJson(text,SystemMessageBean.class);
                message.obj = messageBean;
                handler.sendMessage(message);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                // 接收到服务端发来的消息
                Log.d("WebSocket", "onMessage: " + bytes.toString());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(NORMAL_CLOSURE_STATUS, null);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e("WebSocket", "onFailure", t);
            }
        };
        socket = client.newWebSocket(request, listener);
        System.out.println("connect");
    }

    public void send(String message) {
        // 在房间内发送消息
        socket.send(message);
    }

    public void send(ByteString message) {
        // 在房间内发送消息
        socket.send(message);
    }


    public void disconnect() {
        if (socket != null) {
            socket.close(NORMAL_CLOSURE_STATUS, null);
        }
        client.dispatcher().executorService().shutdown();
    }

    public void addWebSocket(String clientId, WebSocket webSocket) {
        webSockets.put(clientId, webSocket);
    }

    public void removeWebSocket(String clientId) {
        webSockets.remove(clientId);
    }
}

