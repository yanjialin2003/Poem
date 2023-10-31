package com.example.demo.websocket;

import com.example.demo.bean.ClientMessageBean;
import com.example.demo.bean.SystemMessageBean;
import com.example.demo.service.ChatRoomService;
import com.example.demo.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat/{rid}/{username}")
@Component
public class ChatEndPoint {

    public static Map<String ,ChatEndPoint> onlineUsers = new ConcurrentHashMap<>();
    public static Map<Integer ,ConcurrentHashMap<String, ChatEndPoint>> onlineRoom = new ConcurrentHashMap<>();
    public Map<String ,ChatEndPoint> roomUsers;
    String username;
    Integer rid;
    private Session session;
    @OnOpen
    public void onOpen(Session session, @PathParam("rid") int rid,@PathParam("username") String username){
        System.out.println("ws:onOpen");
        this.session = session;
        //加入在线用户中
        onlineUsers.put(username, this);
        this.username=username;
        this.rid=rid;

        if(onlineRoom.get(rid) == null){
            ConcurrentHashMap<String ,ChatEndPoint> map = new ConcurrentHashMap<>();
            map.put(username, this);
            onlineRoom.put(rid, map);
        }else {
            onlineRoom.get(rid).put(username,this);
        }
        roomUsers = onlineRoom.get(rid);

        String message = username+"进入房间";
        SystemMessageBean messageBean = new SystemMessageBean(true,null,message);
        Gson gson = new Gson();
        message = gson.toJson(messageBean);
        broadcastToAll(message,rid);
    }

    void broadcastToAll(String message,int rid){
        //通知所有人 该用户进入房间
        try{
            Set<String> users = onlineUsers.keySet();
            for (String name : users) {
                ChatEndPoint chatEndPoint = onlineRoom.get(rid).get(name);
                chatEndPoint.session.getBasicRemote().sendText(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Set<String> getNames(){
        return onlineUsers.keySet();
    }

    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("onMessage");
        Gson gson = new Gson();
        SystemMessageBean messageBean = new SystemMessageBean(false,username,message);
        message = gson.toJson(messageBean);
        //发消息给所有同一个房间的用户
        try {
            Set<String> users = roomUsers.keySet();
            for(String user:users){
                ChatEndPoint chatEndPoint = roomUsers.get(user);
                chatEndPoint.session.getBasicRemote().sendText(message);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session){
        System.out.println("ws:onClose");
        //告诉所有该房间的人，此人退出房间
        String message = username+"退出房间";
        SystemMessageBean messageBean = new SystemMessageBean(true,null,message);
        Gson gson = new Gson();
        message = gson.toJson(messageBean);
        //移除该会话
        onlineUsers.remove(username);
        onlineRoom.get(rid).remove(username);
        if(onlineRoom.get(rid).size()==0){
            onlineRoom.remove(rid);
        }
        //先清除再广播
        if(onlineRoom.containsKey(rid)){
            broadcastToAll(message,rid);
        }
    }
}
