package com.schuanhe.Plook.controller;

import com.alibaba.fastjson.JSON;
import com.schuanhe.Plook.entity.RoomFrom;
import com.schuanhe.Plook.utils.CurPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Base64;


@Slf4j
@Component
@ServerEndpoint("/websocket/{rooId}/{userId}")
//此注解相当于设置访问URL
public class WebSocket {

    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    public Session session;

    public Integer rid;

    public String uid;

    // 关闭理由
    public String reason = "链接关闭";


    @OnOpen
    public void onOpen(Session session, @PathParam(value="roomId")Integer roomId, @PathParam(value="userId")String userId) {
        this.session = session;
        this.rid = roomId;
        this.uid = userId;
        if (uid == null || "".equals(uid) || rid == null || rid <= 0) {
            this.reason = "链接信息不完全";
            onClose();
            return;
        }
        RoomFrom roomFrom = CurPool.roomFromList.get(rid);
        // 判断是否在房间里
        if(roomFrom == null || !roomFrom.getRoomUser().containsKey(uid)) {
            log.info("[链接失败]rid={} uid={} 你还没进入该房间",rid,uid);
            this.reason = "你还没进入该房间";
            onClose();
            return;
        }
        // 加入链接数据
        CurPool.roomFromList.get(rid).getUserSession().put(uid,session);
        log.info("[链接成功]rid={} uid={} 当前房间人数={}",rid,uid,CurPool.roomFromList.get(rid).getUserSession().size());
    }

    @OnClose
    public void onClose() {
        try {
            // 设置关闭理由reason
            CloseReason customCloseReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, reason);
            // 去除用户数据
            CurPool.roomFromList.get(rid).getUserSession().remove(uid);
            session.close(customCloseReason);
        } catch (Exception e) {
            log.error("[断开连接]rid={} uid={} 当前房间人数={}",rid,uid,CurPool.roomFromList.get(rid).getUserSession().size());
        }
        log.info("[链接关闭]rid={} uid={} 当前房间人数={}",rid,uid,CurPool.roomFromList.get(rid).getUserSession().size());
    }

    @OnMessage
    public void onMessage(String message) {
        sendMessage(rid,uid,message);
        log.info("[收到消息] rid={} uid={} message={}",rid,uid,message);
    }


    public static void sendMessage(Integer rid, String uid, String message) {
        CurPool.roomFromList.get(rid).getUserSession().forEach((k,v)->{
            System.out.println("k:"+k+"uid:"+uid);
            if (!k.equals(uid)) {
                v.getAsyncRemote().sendText(message);
            }
        });
        log.info("[发送消息]rid={} uid={} message={} size={}",rid,uid,message,CurPool.roomFromList.get(rid).getUserSession().size()-1);
    }

    public static <T> void sendObject(Integer rid, Integer uid, T message) {
        CurPool.roomFromList.get(rid).getUserSession().forEach((k,v)->{
            if (!k.equals(uid)) {
                String encode = Base64.getEncoder().encodeToString(JSON.toJSONString(message).getBytes());
                v.getAsyncRemote().sendText(encode);
            }
        });
        log.info("[发送消息]rid={} uid={} message={}",rid,uid,message);
    }

}