package com.example.demo.service;

import com.example.demo.bean.ChatRoom;

import java.util.List;

public interface ChatRoomService {
    int createRoom(String rname, String description);

    boolean deleteRoom(Integer rid);

    List<ChatRoom> getAllRoom(Integer page, Integer size);

    List<ChatRoom> getBySearch(String key,Integer page, Integer size);
}
