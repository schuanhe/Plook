package com.schuanhe.Plook.service;

import com.schuanhe.Plook.entity.RoomFrom;

import java.util.Map;

public interface RoomService {

    /**
     * 创建房间
     * @param roomFrom 房间信息
     * @param userId 用户id
     * @return 房间
     */
    RoomFrom createRoom(RoomFrom roomFrom,String userId) throws Exception;

    /**
     * 退出房间
     * @param roomId 房间id
     * @param userId 用户id
     */
    void exitRoom(Integer roomId,String userId) throws Exception;

    /**
     * 加入房间
     * @param room 房间
     * @param userId 用户id
     */
    boolean joinRoom(RoomFrom room,String userId) throws Exception;


    Map<Integer, String> getRoomListMap();

    RoomFrom getRoom(Integer roomId,String userId) throws Exception;
}
