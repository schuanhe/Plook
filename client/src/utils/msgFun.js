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
  if (datas.data.type == 0) {
    //房间列表
    store.commit("setUserSocketInfoRoomList", datas.data.roomList);
  } else if (datas.data.type == 1) {
    room.chatInfo.Massgs.push(datas);
    ElMessage.success(datas.ownerId + "进入了房间");
  } else if (datas.data.type == 2) {
    room.chatInfo.Massgs.push(datas);
    ElMessage.success(datas.ownerId + "离开了房间");
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
