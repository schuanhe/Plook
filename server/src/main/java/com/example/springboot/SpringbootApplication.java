package com.example.springboot;

import com.alibaba.fastjson.JSONObject;
import com.example.springboot.service.SocketService;
import com.example.springboot.utils.CurPool;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

@SpringBootApplication
public class SpringbootApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(SpringbootApplication.class, args);

        List<String> names = new ManagedList<>();

        CurPool.roomList.put("系统默认房间(幻鹤)",names);

    }

}
