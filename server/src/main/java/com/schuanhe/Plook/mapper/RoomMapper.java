package com.schuanhe.Plook.mapper;

import com.schuanhe.Plook.entity.Room;
import com.schuanhe.Plook.entity.RoomChat;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RoomMapper {

    /**
     * 通过id获取房间和聊天列表
     * @param id 房间id
     * @return 房间信息和聊天列表
     */
    Room getRoomAndChatsById(Integer id);

    /**
     * 添加chat数据
     */
    boolean addRoomChat(RoomChat roomChat);

}
