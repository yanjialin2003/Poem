package com.example.demo.serviceImpl;

import com.example.demo.bean.ChatRoom;
import com.example.demo.mapper.ChatRoomMapper;
import com.example.demo.mapper.PoemMapper;
import com.example.demo.service.ChatRoomService;
import com.example.demo.service.PoemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {
    @Autowired
    ChatRoomMapper chatRoomMapper;

    @Override
    public int createRoom(String rname, String description) {
        return chatRoomMapper.createRoom(rname, description);
    }

    @Override
    public boolean deleteRoom(Integer rid) {
        return chatRoomMapper.deleteRoom(rid);
    }
    @Override
    public List<ChatRoom>  getAllRoom(Integer page, Integer size){
        return chatRoomMapper.getAllRoom(page,size);
    }
    @Override
    public List<ChatRoom> getBySearch(String key,Integer page, Integer size) {
        return chatRoomMapper.getBySearch(key,page,size);
    }
}
