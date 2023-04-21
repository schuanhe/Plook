import msgFun from "./msgFun";
import Cookies from "js-cookie";
import store from "../store";

//对象
const socket = {
  getWebsock() {
    return this.websock;
  },

  initWebSocket: function (loginName) {
    if (import.meta.env.VITE_BASE_WS_S == "true") {
      this.websock = new WebSocket("ws://" + window.location.host + "/websocket/" + loginName);
    }else{
      this.websock = new WebSocket(import.meta.env.VITE_BASE_WS +"/websocket/" + loginName); 
    }
    
    this.websock.onopen = this.websocketonopen;
    this.websock.onerror = this.websocketonerror;  
    this.websock.onmessage = this.websocketonmessage;
    this.websock.onclose = this.websocketclose;
  },
  websocketonopen: function () {
    store.commit("setUserSocketStatus",true)
    console.log("连接成功");
  },
  websocketonerror: function (e) {
    console.log("WebSocket连接发生错误", e);
  },
  websocketonmessage: function (e) {
    // console.log("后端来消息咯", e.data);
    let datas = JSON.parse(e.data);

    if (datas.type == 1) {
      msgFun.roomFun(datas);
    } else if (datas.type == 2) {
      msgFun.videoFun(datas);
    } else if (datas.type == 3) {
      msgFun.chatFun(datas);
    } else {
      ElMessage.error("未知消息:" + datas);
    }
  },
  websocketclose: function (e) {
    console.log("WebSocket连接关闭", e);
      //没有就重定向
      window.location.href = "/";
      //弹出提示
      ElMessage.error("webSocket连接关闭");
      return ;

    //可以重连
    // if(this.curUserId != null){
    // 	if(this.curSessionId != null){
    // 		this.initWebSocket(this.curUserId, this.curSessionId,this.hunaheurl)
    // 	}else{
    // 		this.initWebSocket(this.curUserId, 99999999,this.hunaheurl)
    // 	}
    // }
    // console.log("connection closed",e);
    // console.log(e);
  },
};

export default socket;

