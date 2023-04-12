package com.example.springboot.utils;


import com.example.springboot.controller.WebSocket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一管理websocket
 */
public class CurPool {

    public static Map<String, WebSocket> webSockets = new ConcurrentHashMap<>();

    //房间
    public static Map<String, List<String>> roomList = new HashMap<>();

    //记录这个b在那个房间
    public static Map<String,String> userByRoom = new HashMap<>();

}
