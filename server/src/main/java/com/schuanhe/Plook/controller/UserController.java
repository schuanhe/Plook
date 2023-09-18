package com.schuanhe.Plook.controller;

import com.schuanhe.Plook.entity.User;
import com.schuanhe.Plook.service.UserService;
import com.schuanhe.Plook.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody User user){
        User login;
        try {
            login = userService.Login(user);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success(login);
    }

    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody User user) {
        User register;
        try {
            register = userService.Register(user);
        } catch (Exception e) {
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success(register);
    }
}
