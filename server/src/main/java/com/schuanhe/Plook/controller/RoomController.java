package com.schuanhe.Plook.controller;

import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.service.RoomService;
import com.schuanhe.Plook.utils.CurPool;
import com.schuanhe.Plook.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
    public ResponseResult createRoom(@RequestBody Room room,@CookieValue("userId") String userId) {
        //判断用户是否在线
        log.info("[{}]创建房间:{}", userId, room);
        //if (room.getAdmin() == null || !CurPool.webSockets.containsKey(room.getAdmin())) {
        //    return ResponseResult.error("用户不在线");
        //}
        //判断当前cookie和创建者id是否相同
        if (!room.getAdmin().equals(userId)) {
            return ResponseResult.error("非法操作");
        }
        Room newRoom = roomService.createRoom(room);
        // 返回成功创建房间后的房间的信息
        return ResponseResult.success(newRoom);
    }

    /**
     * 加入房间
     */
    @RequestMapping("/joinRoom/{roomId}")
    public ResponseResult joinRoom(@PathVariable String roomId,@CookieValue("userId") String userId) {
        //判断参数是否完整
        if (roomId == null || roomId.isEmpty()|| userId == null || userId.isEmpty()) {
            return ResponseResult.error("参数不完整");
        }
        //判断用户是否存在
        //if (!CurPool.webSockets.containsKey(userId)) {
        //    return ResponseResult.error("用户不存在");
        //}
        //判断房间是否存在
        Room room = CurPool.roomList.get(roomId);
        if (room == null) {
            return ResponseResult.fail("房间不存在");
        }
        //用户是否在房间中
        if (room.getUsers().contains(userId)) {
            return ResponseResult.success("用户已经在房间里了");
        }
        roomService.joinRoom(room, userId);
        return ResponseResult.success("加入成功");
    }
    /**
     * 获取房间列表
     */
    @GetMapping("/getRoomList")
    public ResponseResult getRoomList() {
        return ResponseResult.success(CurPool.roomList);
    }

    /**
     * 退出房间
     */
    @RequestMapping("/exitRoom")
    public ResponseResult exitRoom(@CookieValue("userId") String userId) {
        roomService.exitRoom(userId);
        return ResponseResult.success("退出成功");
    }

}
