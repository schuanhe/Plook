package com.schuanhe.Plook.controller;

import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.entity.RoomFrom;
import com.schuanhe.Plook.service.RoomService;
import com.schuanhe.Plook.utils.CurPool;
import com.schuanhe.Plook.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController()
@RequestMapping("/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    /**
     * 创建房间，添加房间列表信息，并返回数据。
     * @return {ResponseResult} - 一个 ResponseResult 对象，包含成功创建房间信息。
     */
    @PostMapping("/createRoom")
    public ResponseResult<?> createRoom(@RequestBody RoomFrom roomFrom, @CookieValue("userId") String userId) {
        RoomFrom newRoom;
        try {
            newRoom = roomService.createRoom(roomFrom,userId);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        // 返回成功创建房间后的房间的信息
        return ResponseResult.success(newRoom);
    }

    /**
     * 加入房间
     */
    @RequestMapping("/joinRoom")
    public ResponseResult<?> joinRoom(@RequestBody RoomFrom room,@CookieValue("userId") String userId) {
        //判断参数是否完整
        try {
            roomService.joinRoom(room, userId);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success("加入成功");
    }

    /**
     * 获取房间 信息
     */
    @GetMapping("/getRoom/{roomId}")
    public ResponseResult<?> getRoom(@PathVariable Integer roomId, @CookieValue("userId") String userId) {
        RoomFrom roomFrom;
        try {
            roomFrom = roomService.getRoom(roomId,userId);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success(roomFrom);
    }

    /**
     * 获取公开房间列表
     */
    @GetMapping("/getRoomList")
    public ResponseResult<?> getRoomList() {
        Map<Integer, String> roomListMap = roomService.getRoomListMap();
        return ResponseResult.success(roomListMap);
    }

    /**
     * 退出房间
     */
    @RequestMapping("/exitRoom/{roomId}")
    public ResponseResult<?> exitRoom(@PathVariable Integer roomId , @CookieValue("userId") String userId) {
        try {
            roomService.exitRoom(roomId,userId);
        }catch (Exception e){
            return ResponseResult.error(e.getMessage());
        }
        return ResponseResult.success("退出成功");
    }

}
