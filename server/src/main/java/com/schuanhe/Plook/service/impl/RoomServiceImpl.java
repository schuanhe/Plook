package com.schuanhe.Plook.service.impl;

import com.schuanhe.Plook.controller.WebSocket;
import com.schuanhe.Plook.entity.RoomFrom;
import com.schuanhe.Plook.entity.User;
import com.schuanhe.Plook.service.RoomService;
import com.schuanhe.Plook.utils.CurPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("RoomService")
public class RoomServiceImpl implements RoomService {

    @Autowired
    WebSocket webSocket;


    @Override
    public RoomFrom createRoom(RoomFrom roomFrom, String userId) throws Exception{

        // 判断用户信息
        if (userId == null || userId.isEmpty()) {
            throw  new Exception("用户id不能为空");
        }
        User user = CurPool.userMap.get(userId);
        if (user == null) {
            throw  new Exception("用户没登录不存在");
        }
        // 直接创建房间
        int newRoomId = CurPool.roomFromList.size() + 1;

        RoomFrom newRoomFrom = new RoomFrom();
        newRoomFrom.setPid(1); // 设置当前播放视频的分集
        newRoomFrom.setId(newRoomId);
        if (roomFrom.getIsPassword())
            newRoomFrom.setPassword(roomFrom.getPassword());
        else
            CurPool.publicRoomFromList.put(newRoomId, roomFrom.getRoomName());
        newRoomFrom.setIsPassword(roomFrom.getIsPassword());
        newRoomFrom.setRoomName(roomFrom.getRoomName());
        newRoomFrom.setAdminId(userId);

        // 设置房间 用户
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put(userId, user.getUsername());
        newRoomFrom.setRoomUser(userMap);

        // 设置播放列表
        Map<String, String> videoMap = new HashMap<>();
        videoMap.put(roomFrom.getTitle(), roomFrom.getUrl());
        newRoomFrom.setRoomVideo(videoMap);

        newRoomFrom.setTime(0.0);
        newRoomFrom.setTitle(roomFrom.getTitle());
        newRoomFrom.setUrl(roomFrom.getUrl());
        newRoomFrom.setIsPlay(false);

        // 设置 房间 信息
        CurPool.roomFromList.put(newRoomId, newRoomFrom);

        return newRoomFrom;
    }


    @Override
    public void exitRoom(Integer roomId,String userId) throws Exception{
        isRoomAndUserExist(roomId, userId);
        CurPool.roomFromList.get(roomId).getRoomUser().remove(userId);
    }

    @Override
    public boolean joinRoom(RoomFrom roomFrom, String userId) throws Exception{
        // 判断该房间是否存在
        isRoomAndUserExist(roomFrom.getId(), userId);
        // 判断密码是否正确
        User user = CurPool.userMap.get(userId);
        RoomFrom roomFrom1 = CurPool.roomFromList.get(roomFrom.getId());
        if(roomFrom1.getIsPassword()){
            if (!roomFrom1.getPassword().equals(roomFrom.getPassword())) {
                throw new Exception("密码错误");
            }
        }

        CurPool.roomFromList.get(roomFrom.getId())
                .getRoomUser().put(userId, user.getUsername());
        return true;
    }

    @Override
    public Map<Integer, String> getRoomListMap() {
        return CurPool.publicRoomFromList;
    }

    @Override
    public RoomFrom getRoom(Integer roomId, String userId) throws Exception{
        // 判断该房间是否存在
        isRoomAndUserExist(roomId, userId);
        // 判断是否进入了房间
        if (!CurPool.roomFromList.get(roomId).getRoomUser().containsKey(userId)) {
            throw new Exception("用户未进入房间");
        }
        return CurPool.roomFromList.get(roomId);
    }

    // 判断房间和用户是否存在
    private void isRoomAndUserExist(Integer roomId, String userId) throws Exception{
        // 判断该房间是否存在
        RoomFrom roomFrom = CurPool.roomFromList.get(roomId);
        if (roomFrom == null) {
            throw new Exception("房间不存在");
        }
        // 判断用户是否存在
        User user = CurPool.userMap.get(userId);
        if (user == null) {
            throw new Exception("用户没登录或不存在");
        }
    }

}
