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
    private String admin;   //房间的管理员(创建者)
    private boolean room_aut; //房间是否需要密码false：不需要，true：需要
    private String room_password; //房间的密码
    private List<String> users; //房间的用户
}
