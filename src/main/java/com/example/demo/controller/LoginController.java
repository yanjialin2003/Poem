package com.example.demo.controller;

import com.example.demo.bean.User;
import com.example.demo.service.UserService;
import com.example.demo.serviceImpl.UserServiceImpl;
import com.sun.net.httpserver.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/user")
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/loginIn",method = RequestMethod.GET)
    public String login(String username,String password) {
        User user = userService.loginIn(username, password);
        if (user != null) {
            return "success";
        } else {
            return "error";
        }
    }

    //注册，这里处理得玄学，报异常返回error
    @RequestMapping(value = "/register")
    public String register(String username,String password){
        String result = "success";
        try{
            boolean res = userService.register(username,password);
        }catch (Exception e){
            result = "error";
        }
        return result;
    }
}
