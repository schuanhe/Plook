package com.schuanhe.Plook.controller;

import com.schuanhe.Plook.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    @Autowired
    UserMapper userMapper;

}
