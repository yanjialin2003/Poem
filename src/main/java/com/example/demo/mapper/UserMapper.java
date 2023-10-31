package com.example.demo.mapper;

import com.example.demo.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User getUser(String username, String password);
    Boolean register(String username, String password);
}
