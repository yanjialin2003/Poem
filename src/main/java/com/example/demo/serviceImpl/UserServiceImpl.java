package com.example.demo.serviceImpl;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User loginIn(String username, String password) {
       return userMapper.getUser(username,password);
    }

    public boolean register(String username, String password) {
        return userMapper.register(username,password);
    }
}
