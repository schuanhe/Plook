package com.schuanhe.Plook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 房间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Room {
    private Integer id;     //房间id
    private String name;    //房间的名称
    private Integer admin;  //管理员id
    private List<RoomChat> roomChats; //聊天的记录
}
