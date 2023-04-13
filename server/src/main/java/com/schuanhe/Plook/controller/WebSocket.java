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
        log.info("[链接成功]name={} session={} socket={} 数量:{}",name,session,this,CurPool.webSockets.size());
        //发送 房间 数据返回
        Map<String, Object> map = SocketService.onOpen(name);
        System.out.println(map);
        sendMassageList(map);

    }

    @OnClose
    public void onClose() {
        // 断开连接删除用户删除session
        Map<String, Object> map = SocketService.onClose(this.name);
        sendMassageList(map);
    }

    @OnMessage
    public void onMessage(String message) {
        Map<String, Object> map = SocketService.onMessage(message);
        sendMassageList(map);
        System.out.println("【websocket消息】收到客户端消息:"+message);
    }

    //发送消息
    public void sendMassageList(Map<String, Object> sendMsg){

        for (Object name : (List) sendMsg.get("names")) {
            if(sendMsg.containsKey("ownerId")){
                if(!sendMsg.get("ownerId").equals(name)){

                    sendMassage(name.toString(),sendMsg.get("data").toString());
                }
            }else {

                sendMassage(name.toString(),sendMsg.get("data").toString());
            }

        }

        System.out.println(sendMsg);
    }

    //发送消息
    public void sendMassage(String name,String message){
        System.out.println(name);
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