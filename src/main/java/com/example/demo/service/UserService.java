package com.example.demo.service;

import com.example.demo.bean.User;

public interface UserService {
    User loginIn(String username,String password);
    boolean register(String username, String password);
}
