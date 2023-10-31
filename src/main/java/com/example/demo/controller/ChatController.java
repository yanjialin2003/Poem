package com.example.demo.controller;

import com.example.demo.bean.ChatRoom;
import com.example.demo.service.ChatRoomService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/chatController")
@RestController
public class ChatController {
    @Autowired
    private ChatRoomService chatRoomService;

    @RequestMapping(value = "/createRoom",method = RequestMethod.GET)
    String createRoom(String rname, String description){
        //创建该名称房间，并且返回房间id
        return String.valueOf(chatRoomService.createRoom(rname,description));
    }
    @RequestMapping(value = "/deleteRoom",method = RequestMethod.GET)
    String deleteRoom(Integer rid){
        //根据房间号，删除房间
        return String.valueOf(chatRoomService.deleteRoom(rid));
    }
    @RequestMapping(value = "/getAllRoom", method = RequestMethod.GET)
    String getAllRoom(Integer page, Integer size){
        Gson gson = new Gson();
        page = page*size;
        List<ChatRoom> list = chatRoomService.getAllRoom(page,size);
        return gson.toJson(list);
    }
    @RequestMapping(value = "/getBySearch", method = RequestMethod.GET)
    String getBySearch(String key,Integer page, Integer size){
        Gson gson = new Gson();
        page = page*size;
        key = "%"+key+"%";
        List<ChatRoom> list = chatRoomService.getBySearch(key,page,size);
        return gson.toJson(list);
    }
}
