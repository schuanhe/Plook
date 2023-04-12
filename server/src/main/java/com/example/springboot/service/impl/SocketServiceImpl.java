package com.example.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.example.springboot.controller.WebSocket;
import com.example.springboot.controller.WebSocket;
import com.example.springboot.service.SocketService;
import com.example.springboot.utils.CurPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;


@Slf4j
@Service
public class SocketServiceImpl extends SocketService {

//
//    public String onMessage(String message) {
//
//        JSONObject msg = JSONObject.parseObject(message);//将json转换为对象
//
//        System.out.println(msg);
//
//        return msg.toJSONString();
        //JSONObject msg = JSONObject.parseObject(msg.get);//将json转换为对象
//        //判断type
//        if(msg.getInteger("type")==1){
//            //房间 事件
//            if(msg.getDate("data")){
//
//            }
//        }else if(msg.getInteger("type")==2){
//            //播放器事件
//        }else if(msg.getInteger("type")==3){
//            //消息事件
//        }
//
//        if("状态".equals(msg.getString("action"))){
//            String name = msg.getString("serverName");
//            sendMsg(msg.getString("data"),name);
////            if ("暂停".equals(msg.getString("data"))){
////
////            }else if ("播放".equals(msg.getString("data"))){
////
////            }
//        }else if("跳转".equals(msg.getString("action"))){
//
//            String name = msg.getString("serverName");
//            String s = JSON.toJSONString(msg);
//            sendMsg(s,name);
//        }else if("切换".equals(msg.getString("action"))){
//            String name = msg.getString("serverName");
//            String s = JSON.toJSONString(msg);
//            sendMsg(s,name);
//        }
//    }


}
