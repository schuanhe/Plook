package com.schuanhe.Plook.utils;


import com.schuanhe.Plook.entity.RoomFrom;
import com.schuanhe.Plook.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一管理websocket
 */
public class CurPool {


    // 房间 信息
    public static Map<Integer, RoomFrom> roomFromList = new HashMap<>();

    // 公开房间
    public static Map<Integer, String> publicRoomFromList = new HashMap<>();

    // 用户信息
    public static Map<String, User> userMap = new HashMap<>();

}
