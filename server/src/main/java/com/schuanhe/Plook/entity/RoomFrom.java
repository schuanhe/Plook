package com.schuanhe.Plook.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;

/**
 * 房间配置实体类
 */
@Data
public class RoomFrom {
    private Integer id; // 房间id
    // 房间名称
    private String roomName;
    // 是否需要密码
    private Boolean isPassword;
    // 密码
    private String password;
    // 房间 管理员
    private String adminId; // 前端生成的8位随机字符串
    // 在房间的用户id
    private Map<String, String> roomUser; // 用户昵称 ， 前端生成的8位随机字符串
    // 房间播放视频列表
    private Map<String, String> roomVideo; // 标题和视频url
    // 视频进度
    private Double time;
    // 当前播放视频的标题和url
    private String Title;
    private String Url;
    private Integer pid; // 进度p

    // 视频是否在播放
    private Boolean isPlay;

    // 用户id与其对应的session(不序列化)
    @JsonIgnore
    private Map<String, Session> userSession; // 前端生成的8位随机字符串与对应的session

}
