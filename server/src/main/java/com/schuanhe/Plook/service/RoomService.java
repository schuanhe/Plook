package com.schuanhe.Plook.service;

import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.entity.RoomChat;

public interface RoomService {

    /**
     * 添加房间，数据库没用则加入
     * @param userId userId
     * @return 返回
     */
    boolean addRoom(Integer userId,String roomName);

    /**
     * 通过id获取房间信息
     * @param id 房间id
     * @return 详细详细，包括数据
     */
    Room getRoomById(Integer id);

    /**
     * 聊天信息添加啊
     * @param roomId 房间id
     * @param userID 用户id
     * @param data 数据
     * @return 成功与否
     */
    boolean addRoomChat(Integer roomId, Integer userID, String data);

}
