package com.schuanhe.Plook.utils;


import com.schuanhe.Plook.controller.WebSocket;
import com.schuanhe.Plook.entity.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一管理websocket
 */
public class CurPool {

    //链接的用户名，websocket实体
    public static Map<String, WebSocket> webSockets = new ConcurrentHashMap<>();

    //房间 房间id，进入房间的用户id
    //public static Map<String, List<String>> roomList = new HashMap<>();

    //房间 房间id，房间的信息
    public static Map<String, Room> roomList = new HashMap<>();

    //记录这个b在那个房间 用户id，房间id
    public static Map<String,String> userByRoom = new HashMap<>();


    //通过用户id获取当前用户所在房间id
    public static Map<Integer,Integer> userIdByRoomId = new HashMap<>();

    //通过用户id获取用户的socket对象
    public static Map<Integer, WebSocket> userSockets = new ConcurrentHashMap<>();

    //通过房间id,获取房间所有人的list
    public static Map<Integer,List<Integer>> roomIdByIdList = new HashMap<>();



}
