package com.schuanhe.Plook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * 聊天记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoomChat {
    private Integer id;     //消息id
    private Integer roomId; //房间id
    private Timestamp time;    //记录时间
    private String data;    //聊天数据
    private Integer userId; //用户id
}
