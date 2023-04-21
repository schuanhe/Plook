package com.schuanhe.Plook.service.impl;

import com.alibaba.fastjson.JSON;
import com.schuanhe.Plook.controller.WebSocket;
import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.entity.RoomChat;
import com.schuanhe.Plook.mapper.RoomMapper;
import com.schuanhe.Plook.service.RoomService;
import com.schuanhe.Plook.service.SocketService;
import com.schuanhe.Plook.utils.CurPool;
import com.schuanhe.Plook.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service("RoomService")
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomMapper roomMapper;

    @Autowired
    WebSocket webSocket;


    @Override
    public Room createRoom(Room room) {

        //TODO:创建房间到数据库，设置id等...

        //先用随机数代替
        Random rand = new Random();
        int num = rand.nextInt(9000) + 1000;
        room.setId(num);

        //添加用户
        List<String> users = new ManagedList<>();
        users.add(room.getAdmin());
        //房间中加入该用户
        room.setUsers(users);
        //房间 信息写入
        CurPool.roomList.put(room.getName(), room);

        //添加用户在房间的信息
        //TODO:换成id
        CurPool.userByRoom.put(room.getAdmin(), room.getName());
        return room;
    }

    /**
     * 退出房间
     * @param id 用户id
     */
    @Override
    public void exitRoom(String id) {
        webSocket.sendMassageList(SocketService.exitRoom(id));
    }

    @Override
    public boolean joinRoom(Room room, String userId) {

        Map<String, Object> reMsg = new HashMap<>();//返回的消息
        reMsg.put("names", room.getUsers());//添加前的用户
        Map<String, Object> data = new HashMap<>();
        //{"type":2},"type":1,"ownerId":"22","roomId":"22的房间"}
        data.put("type", 1);
        data.put("data", Collections.singletonMap("type", 1));
        data.put("roomId", room.getName());
        data.put("ownerId", userId);
        String json = JSON.toJSONString(data);
        reMsg.put("data", json);
        System.out.println(reMsg);
        webSocket.sendMassageList(reMsg);

        //直接加入吧
        List<String> users = room.getUsers();
        users.add(userId);
        room.setUsers(users);
        //更新房间数据
        CurPool.roomList.put(room.getName(), room);
        //记录userByRoom
        CurPool.userByRoom.put(userId, room.getName());


        return true;
    }

    @Override
    public Room getRoomById(Integer id) {
        return roomMapper.getRoomAndChatsById(id);
    }

    @Override
    public boolean addRoomChat(Integer roomId, Integer userId, String data) {
        Timestamp time = new Timestamp(System.currentTimeMillis());//获取当前时间
        RoomChat roomChat = new RoomChat(null , roomId, time, data,userId);
        return roomMapper.addRoomChat(roomChat);
    }
}
