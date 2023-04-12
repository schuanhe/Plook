import { createStore } from "vuex";

export default createStore({
  //所有的数都放在这里
  state: {
    counter: 0,
    socketMsgStatus: true, //后端回调，true 为正常，false表示最近切换过一次视频进度
    MyWebSocket:null,   //websocket连接
    MyVideo:{
        aVideo:null,
        allVideo:null
    },
    //配置数据
    Allinfo: {
      //用户数据
      user: {
        userId: null, //用户id
        userName: null, //用户名
        //socket数据
        socketInfo: {
          status: false, //链接状态
          roomList: [], //房间列表
          // scoketThrottle: true, //节流函数
        },
      },
      //房间数据
      room: {
        //聊天数据
        roomId: null,   //房间id
        adminId: 100, //房间管理员id
        userList: [
          {
            userId: 1000, //房间用户数据
            userName: "幻鹤", //房间用户数据
          },
        ],
        chatInfo: {
          myMassg: "", //似乎没用过
          Massgs: [], //聊天数据
        },
        videoInfo: {
          src:{
            src: "https://p1-webcast-xgcdn.byteimg.com/origin/tos-alisg-v-0000/ocCq4mA92AheZonwIPbG3eCslHDg4GtueUXRn6?filename=1.mp4&tg_qun=yhzyw", //数据源
            type: "video/mp4", //视频类型
          },
          inTime: "", //视频当前进度
          playStatus: false, //视频状态
        },
      },
      //房间设置
      roomDis: {},
    },
  },
  //读取方法函数，也可以直接读
  getters: {
    getCounter(state) {
      return state.counter > 0 ? state.counter : "counter数据异常";
    },
    getMyWebSocket(state){
        return state.MyWebSocket
    },
    getMyVideo(state){
        return state.MyVideo
    },
    getAllinfo(state){
        return state.Allinfo
    },
    getUserUserName(state){
        return state.Allinfo.user.userName
    },
    getUserSocketInfoRoomList(state){
        return state.Allinfo.user.socketInfo.roomList
    },
    getRoom(state){
        return state.Allinfo.room
    },
    getVideoInfoSrc(state){
      return state.Allinfo.room.videoInfo.src
    },
    getRoomChatInfoMassgs(state){
      // 快速获取房间聊天数据
      return state.Allinfo.room.chatInfo.Massgs
    },
    getSocketMsgInit(state){
      //初始化socket的数据
      return {
        userName:state.Allinfo.user.userName,
        roomId:state.Allinfo.room.roomId
      }
    },

  },
  //改变数据函数
  mutations: {
    add(state) {
      state.counter++;
    },
    addcsvuex(state) {
        state.csvuex++;
    },
    initMyWebSocket(state,MyWebSocket){
        state.MyWebSocket = MyWebSocket        
    },
    initMyVideo(state,myVideo){
        state.MyVideo.aVideo = myVideo.aVideo
        state.MyVideo.allVideo = myVideo.allVideo
    },
    setSocketMsgStatus(state,status){
      state.socketMsgStatus = status
    },
    setUserUserName(state,userName){
        state.Allinfo.user.userName = userName
    },
    setUserSocketInfoRoomList(state,roomList){
        state.Allinfo.user.socketInfo.roomList = roomList
    },
    setRoomChatInfoMassg(state,massg){
      // 添加房间单条聊天数据
      state.Allinfo.room.chatInfo.Massgs.push(massg)
    },
    setRoomRoomId(state,roomId){
        state.Allinfo.room.roomId = roomId
    },
    setVideoInfoSrc(state,src){
      state.Allinfo.room.videoInfo.src = src
    }
  },
  //为异步操作准备
  actions: {
  },
});
