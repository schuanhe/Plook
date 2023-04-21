import { ElMessage } from "element-plus";
import store from "../store";

/**
 * 后端消息处理
 */

const inData = {
  asyData: true, //视频进度切换的异步函数
};

//===房间事件===
const roomFun = function (datas) {
  let Allinfo = store.getters.getAllinfo;
  let room = store.getters.getRoom;
  let MyVideo = store.getters.getMyVideo;

  //如果消息是自己的就不管

  

  if (datas.data.type == 0) {
    //房间初始化事件
    //如果是自己就不用同步
    if (datas.ownerId == Allinfo.user.userName) {
      //自己初始化信息
    }else{
      let data={
        "type":3,
        //视频url
        "src":Allinfo.room.videoInfo.src,
        //视频进度
        "reach":MyVideo.allVideo.currentTime,
        //视频状态
        "play":MyVideo.allVideo.paused //是否暂停
      }
      datas.data = data
      //提交信息发送给后端
      store.state.MyWebSocket.send(JSON.stringify(datas));
    }
    //store.commit("setUserSocketInfoRoomList", datas.data.roomList);
  } else if (datas.data.type == 1) {
    room.chatInfo.Massgs.push(datas);
    ElMessage.success(datas.ownerId + "进入了房间");
  } else if (datas.data.type == 2) {
    room.chatInfo.Massgs.push(datas);
    ElMessage.success(datas.ownerId + "离开了房间");
  }else if (datas.data.type == 3) {
    //初始化回调
    ElMessage.success("同步成功");
    //同步视频src
    store.commit("setVideoInfoSrc",{
      src:datas.data.src.src,
      type:datas.data.src.type
    })
    //同步进度
    store.commit("setSocketMsgStatus",false)
 
    setTimeout(() => {
      if (datas.data.play) {
      MyVideo.aVideo.pause();
    }else{
      MyVideo.allVideo.currentTime = datas.data.reach + 0.5
      MyVideo.aVideo.play();
    }
    },500)

    setTimeout(() => {
         store.commit("setSocketMsgStatus",true)
      }, 3000)

    
  }
};

//===视频事件===
const videoFun = function (datas) {
  if (datas.data.type == 0) {
    if (datas.data.play == 0) {
      //暂停
      store.state.MyVideo.aVideo.pause();
    } else {
      //播放
      store.state.MyVideo.aVideo.play();
    }
  } else if (datas.data.type == 1) {
    //切换进度

    store.commit("setSocketMsgStatus",false)

    store.state.MyVideo.allVideo.currentTime = datas.data.reach

    setTimeout(() => {
         store.commit("setSocketMsgStatus",true)
      }, 3000)

    // if (inData.asyData) {
    //   store.state.MyVideo.allVideo.currentTime = datas.data.reach
    //   inData.asyData = false
    //   setTimeout(() => {
    //     inData.asyData = true
    //   }, 1000)
    // } else {
    //   inData.asyData = true
    // }
  } else if (datas.data.type == 2) {
    //切换视频
    store.commit("setVideoInfoSrc",{
      src:datas.data.src,
      type:datas.data.srcType
    })
  }
};

//消息事件
const chatFun = function (datas) {
  if (datas.data.type == 0) {
    store.commit("setRoomChatInfoMassg", datas)
  }
};

const noFun = function (datas) {
  ElMessage.error("未知消息" + datas);
};

export default {
  roomFun,
  videoFun,
  chatFun,
  noFun,
};
