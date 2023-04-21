package com.schuanhe.Plook.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schuanhe.Plook.controller.WebSocket;
import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.utils.CurPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 返回的string
 * {
 * "users":{"幻鹤","狂战","..."},
 * "data":{ Object }
 * }
 */
@Slf4j
@Service
public class SocketService {


    /**
     * 接收的消息处理
     */
    public static Map<String, Object> onMessage(String message) {
        log.info("接收到消息:{}", message);
        Map<String, Object> reMsg = new HashMap<>();//返回的消息

        JSONObject msg = JSONObject.parseObject(message);//将json转换为对象
        JSONObject data = JSONObject.parseObject(msg.getString("data"));

        //判断房间是否存在
        if (!CurPool.roomList.containsKey(msg.getString("roomId"))) {
            return reMsg;
        }
        //获取当前消息房间的人
        Room room = CurPool.roomList.get(msg.getString("roomId"));

        List<String> userList = room.getUsers();
        //获取当前消息所有者id
        String ownerId = msg.getString("ownerId");


        // 如果是初始化
        if(msg.getInteger("type") == 1){
            //初始化
            if(data.getInteger("type") == 0){
                //拿到管理员的socket实例
                WebSocket webSocket = CurPool.webSockets.get(room.getAdmin());
                //给管理员发送回调
                webSocket.sendMassage(room.getAdmin(), message); //发送消息
                log.info("[{}]请求初始化:{}",room.getAdmin() , message);
                return new HashMap<>();
            }else if(data.getInteger("type") == 3){
                //回调
                WebSocket webSocket = CurPool.webSockets.get(msg.getString("ownerId"));
                //返回给消息的所有者
                webSocket.sendMassage(msg.getString("ownerId"), message); //发送消息
                log.info("[{}]初始化回调:{}",msg.getString("ownerId") , message);
                return new HashMap<>();
            }



        }



        //加入房间变更到/room/createRoom post请求中
        //if (msg.getInteger("type") == 1) {
        //    //房间 事件
        //    if (data.getInteger("type") == 1) {
        //        //加入房间
        //        userList.add(msg.getString("ownerId"));
        //        CurPool.userByRoom.put(msg.getString("ownerId"), msg.getString("roomId"));
        //        log.info("[{}]加入房间:{}", ownerId, msg.getString("roomId"));
        //    }
        //}
        //暂时不需要
        //else if (msg.getInteger("type") == 2) {
        //    log.info("[{}]视频控制:{}", ownerId, data);
        //} else if (msg.getInteger("type") == 3) {
        //    //消息事件
        //    if (data.getInteger("type") == 0) {
        //        log.info("[{}]发送消息:{}", ownerId, data.getString("msg"));
        //    }
        //}

        //判断三个属性是否有值
        if (userList != null || userList.size() != 0) {
            reMsg.put("names", userList);
        }
        if (message != null) {
            reMsg.put("data", message);
        }
        if (ownerId != null) {
            reMsg.put("ownerId", ownerId);
        }

        return reMsg;
    }

    /**
     * 连接初始化
     */
    public static Map<String, Object> onOpen(String name) {
        Map<String, Object> msg = new HashMap<>();//返回的消息
        //判断是否是重连,比如说在房间里
        if (CurPool.userByRoom.containsKey(name)) {
            //重连

        }else {
            //新连接

        }
        return msg;
    }

    /**
     * 断开
     */
    public static Map<String, Object> onClose(String id) {
        CurPool.webSockets.remove(id);
        return exitRoom(id);
    }

    //退出房间
    public static Map<String, Object> exitRoom(String id) {
        Map<String, Object> msg = new HashMap<>();
        try {
            //判断是否在房间中
            if (!CurPool.userByRoom.containsKey(id)) {
                return msg;
            }
            String roomId = CurPool.userByRoom.get(id); //获取房间id
            CurPool.userByRoom.remove(id); //移除用户与房间的关系

            Room room = CurPool.roomList.get(roomId);//获取房间里的人
            //移除他在房间
            room.getUsers().remove(id);

            //判断是否为房间管理员
            if (room.getAdmin().equals(id)) {
                //如果是，判断房间还有没有人
                if (room.getUsers().size() == 0) {
                    //删除房间
                    CurPool.roomList.remove(roomId);
                    return msg; //不发送消息
                }else {
                    //转移房间的管理员
                    room.setAdmin(room.getUsers().get(0));
                }
            }else {
                //如果不是房间的管理员,直接删除用户
                room.getUsers().remove(id);
            }

            //更新房间的信息
            CurPool.roomList.put(roomId, room);

            log.info("[{}]离开了【{}】:当前房间还有{}", id, roomId, room.getUsers());

            msg.put("names", room.getUsers());
            //返回的数据处理

            Map<String, Object> data = new HashMap<>();
            data.put("type", 1);
            data.put("data", Collections.singletonMap("type", 2));
            data.put("roomId", roomId);
            data.put("ownerId", id);
            String json = JSON.toJSONString(data);
            msg.put("data", json);
            //String json = "{\"type\":1,\"data\":{\"type\":2},\"roomId\":\"" + roomId + "\",\"ownerId\":\"" + name + "\"}";
            //msg.put("data", json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return msg;

    }




}


