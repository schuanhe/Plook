package com.schuanhe.Plook.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.schuanhe.Plook.utils.CurPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 返回的string
 * {
 * "users":{"幻鹤","狂战","..."},
 * "data":{ Object }
 * }
 */
@Slf4j
public class SocketService {

    @Autowired
    RoomService roomService;

    /**
     * 接收的消息处理
     */
    public static Map<String, Object> onMessage(String message) {

        Map<String, Object> reMsg = new HashMap<>();

        JSONObject msg = JSONObject.parseObject(message);//将json转换为对象
        JSONObject data = JSONObject.parseObject(msg.getString("data"));

        List<String> userList = CurPool.roomList.get(msg.getString("roomId"));
        String ownerId = msg.getString("ownerId");

        if (msg.getInteger("type") == 1) {
            //房间 事件
            if (data.getInteger("type") == 1) {
                userList.add(msg.getString("ownerId"));
                CurPool.userByRoom.put(msg.getString("ownerId"), msg.getString("roomId"));
                log.info("[{}]加入房间:{}", ownerId, msg.getString("roomId"));
            }
        } else if (msg.getInteger("type") == 2) {
            log.info("[{}]视频控制:{}", ownerId, data);
        } else if (msg.getInteger("type") == 3) {
            //消息事件
            if (data.getInteger("type") == 0) {
                log.info("[{}]发送消息:{}", ownerId, data.getString("msg"));
            }
        }

        reMsg.put("names", userList);
        reMsg.put("data", message);
        reMsg.put("ownerId", ownerId);

        log.info("roomList[{}]\n{}\n{}",CurPool.roomList,CurPool.userByRoom,CurPool.webSockets);
        return reMsg;
    }

    /**
     * 连接
     *
     * @param name
     * @return
     */
    public static Map<String, Object> onOpen(String name) {
        Map<String, List<String>> roomList = CurPool.roomList;
        List<String> rooms = new LinkedList<>(roomList.keySet());//6

        Map<String, Object> msg = new HashMap<>();

        List<String> names = new LinkedList<>();//需要发送的用户组
        names.add(name);

        msg.put("names", names);
        //返回的数据处理
        String json = "{\"type\":1,\"data\":{\"type\":0,\"roomList\":" + JSON.toJSONString(rooms) + "}}";
        msg.put("data", json);
        return msg;
    }

    /**
     * 断开
     */
    public static Map<String, Object> onClose(String name) {

        CurPool.webSockets.remove(name);
        String roomId = CurPool.userByRoom.get(name);
        List<String> userList = CurPool.roomList.get(roomId);
        userList.remove(name);//删除里的人
        log.info("[{}]离开了【{}】:当前房间还有{}", name, roomId, userList);

        Map<String, Object> msg = new HashMap<>();

        msg.put("names", userList);
        //返回的数据处理
        String json = "{\"type\":1,\"data\":{\"type\":2},\"roomId\":\"" + roomId + "\",\"ownerId\":\"" + name + "\"}";
        msg.put("data", json);
        return msg;
    }

}
