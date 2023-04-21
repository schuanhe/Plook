package com.schuanhe.Plook.controller;

import com.schuanhe.Plook.service.SocketService;
import com.schuanhe.Plook.utils.CurPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Component
@ServerEndpoint("/websocket/{name}")
//此注解相当于设置访问URL
public class WebSocket {


    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    public Session session;
    /**
     * 标识当前连接客户端的用户名
     */
    public String name;


    @OnOpen
    public void onOpen(Session session, @PathParam(value="name")String name) {
        this.session = session;
        this.name = name;
        CurPool.webSockets.put(name,this);
        log.info("[链接成功]name={} socket={} 数量:{}",name,this,CurPool.webSockets.size());

        Map<String, Object> map = SocketService.onOpen(name);
        sendMassageList(map);

    }

    @OnClose
    public void onClose() {
        // 断开连接删除用户删除session
        Map<String, Object> map = SocketService.onClose(this.name);
        sendMassageList(map);
        log.info("[断开连接]name={} session={} socket={} 数量:{}",name,session,this,CurPool.webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        Map<String, Object> map = SocketService.onMessage(message);
        sendMassageList(map);
        log.info("[收到客户端消息]name={} message={} map={} 数量:{}",name,message,map,CurPool.webSockets.size());
    }

    //发送消息
    public void sendMassageList(Map<String, Object> sendMsg){

        //for (Object name : (List) sendMsg.get("names")) {
        //    if(sendMsg.containsKey("ownerId")){
        //        //list包括消息发送者
        //        if(!sendMsg.get("ownerId").equals(name)){
        //            //不是消息发送者
        //            sendMassage(name.toString(),sendMsg.get("data").toString());
        //        }else {
        //            //本次是消息发送者
        //            System.out.println("跳过本次发送");
        //        }
        //    }else {
        //        //list不包括消息发送者
        //        sendMassage(name.toString(),sendMsg.get("data").toString());
        //    }
        //}

        //判断是否为空
        if (Objects.isNull(sendMsg)||sendMsg.isEmpty()){
            return;
        }
        log.info("发送消息:{}",sendMsg);
        ((List<?>) sendMsg.get("names")).stream()
                .filter(name -> !sendMsg.containsKey("ownerId") || !sendMsg.get("ownerId").equals(name))
                .forEach(name -> sendMassage(name.toString(), sendMsg.get("data").toString()));
    }

    //发送消息
    public void sendMassage(String name,String message){
        if (!CurPool.webSockets.containsKey(name)) {
            return;
        }
        Session session = CurPool.webSockets.get(name).session;
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 此为广播消息
//    public void sendAllMessage(String message) {
//        for(WebSocket webSocket : webSockets) {
//            System.out.println("【websocket消息】广播消息:"+message);
//            try {
//                webSocket.session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // 此为单点消息 (发送文本)
//    public void sendTextMessage(Integer userId, String message) {
//        Session session = (Session)CurPool.sessionPool.get(userId).get(1);
//        if (session != null) {
//            try {
//                session.getAsyncRemote().sendText(message);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // 此为单点消息 (发送对象)
//    public void sendObjMessage(String sessionId, Object message) {
//        Session session = CurPool.sessionPool.get(sessionId);
//        if (session != null) {
//            try {
////                session.getAsyncRemote().sendObject(message);
//                session.getBasicRemote().sendText(JsonUtils.objectToJson(message));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}