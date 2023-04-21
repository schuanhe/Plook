package com.schuanhe.Plook.service;

import com.schuanhe.Plook.entity.Room;

public interface RoomService {

    /**
     * 创建房间
     * @param room 房间的参数
     * @return 房间
     */
    Room createRoom(Room room);

    /**
     * 退出房间
     */
    void exitRoom(String id);

    /**
     * 加入房间
     */
    boolean joinRoom(Room room,String userId);

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
