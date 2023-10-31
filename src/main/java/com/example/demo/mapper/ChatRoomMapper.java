package com.example.demo.mapper;

import com.example.demo.bean.ChatRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRoomMapper {
    int createRoom(String rname, String description);

    boolean deleteRoom(Integer rid);

    List<ChatRoom> getAllRoom(Integer page, Integer size);

    List<ChatRoom> getBySearch(String key,Integer page, Integer size);
}
