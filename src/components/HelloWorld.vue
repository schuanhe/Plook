<template>
  <!-- :options="playerOptions"  -->
  <video-player controls :options="playerOptions" ref="videoPlayer" @ready="videoReadey" @play="onPlayerPlay($event)"
    @pause="onPlayerPause($event)" @ended="onPlayerEnded($event)" @seeked="onPlayerSeeked()" />


    <div :style="{width: divWidth, height: divHeight}"></div>
        <el-button type="primary" @click="dialog.dialogMenu = true">菜单</el-button>
  <el-scrollbar ref="scrollbarRef" max-height="400px" :style="{width:((divWidth-80)+'px')}">
    <chat v-for="item in chatInfo.Massgs" :key="item" :chats="item" :user-name="userInfo.userName"></chat>
  </el-scrollbar>
  <el-input v-model="chatInfo.myMassg" @change="setMyassg" placeholder="发送消息" />
<el-button @click="setMyassg" >发送</el-button>

<!-- 
  <el-drawer v-model="drawer" title="I am the title" :with-header="false"> -->
        <!-- <el-button type="primary" @click="playPause">{{ playStatus? '暂停': '播放' }}</el-button> -->
        <!-- <el-divider style="margin:0px" /> -->
        <!-- <el-switch v-model="socketStatus" inline-prompt active-text="连接成功" inactive-text="没有连接" disabled />
        <el-input v-model="userInfo.userName" placeholder="用户名" />
        <el-input v-model="userInfo.roomId" placeholder="请输入房间号" />
        <el-button @click="login">登录并链接服务器</el-button>
        <el-divider style="margin:0px" />
        <el-input v-model="videoSrc" placeholder="播放的视频链接" />
        <el-button @click="setVideoSrc">刷新播放器</el-button>
  </el-drawer> -->


  <!-- 登录弹窗 -->
  <el-dialog v-model="dialog.dialogLogin" title="你先登录一哈子" width="30%" :show-close=false draggable>
    <el-form-item label="用户名">
      <el-input placeholder="请输入用户名" v-model="userInfo.userName" />
    </el-form-item>
      <el-button type="primary" @click="login" >登 录</el-button>
  </el-dialog>
  <!-- 选房间弹窗 -->
  <el-dialog v-model="dialog.dialogRoom" title="选一哈房间嘛" width="30%" :show-close=false draggable>
    <el-form-item label="选择房间">
      <el-select v-model="userInfo.roomId" class="m-2" placeholder="选则一个存在的房间" >
        <el-option v-for="item in socketInfo.roomList" :key="item" :label="item" :value="item"/>
      </el-select>
    </el-form-item>
    <el-button @click="loginRoom" type="primary">进入这个房间</el-button>
  </el-dialog>
  <!-- 菜单弹窗 -->
  <el-dialog v-model="dialog.dialogMenu" title="菜单" draggable>
    <el-form-item label="切换视频源">
      <el-input placeholder="请输入src" v-model="videoInfo.src" />
    </el-form-item>
      <el-button type="primary" @click="setVideoSrc" >切 换</el-button>
  </el-dialog>

</template>

<script>
import chat from './chat.vue';
import { ElMessage } from 'element-plus'
import socketMsg from '../utils/socketMsg';
import videoConfig from '../utils/videoConfig'


export default ({
  name: "videDemo",
  components: {
    chat
  },
  created(){
    this.divWidth = document.body.clientWidth
    this.divHeight = document.body.clientHeight
  },
  data() {
    return {
      dialog:{
        dialogLogin:true,//登录弹窗
        dialogRoom:false, //房间弹窗
        dialogMenu:false //菜单弹窗
      },
      socketInfo:{
        status:false, //连接状态
        roomList:[],//房间列表
        scoketThrottle:true,//节流函数
        socketMsgStatus:false,//后端会回调的 状态
      },
      chatInfo:{
        myMassg:'',
        Massgs:[],
      },
      // videoSrc:'',
      // myMassg:'',//我要发送的消息
      // Massgs:[],
      divHeight:0,//动态高宽
      divWidth:0,//动态高宽
      // drawer:false,
      // scoketThrottle: true,//节流函数
      // socketUserName: '',
      // socketStatus: false,//连接状态
      // loginName: '',//登录的用户名
      // playerTime: 0,//视频进度时间
      // playStatus: false,//视频播放与否
      // playerTimeMax: 0,//视频时常
      playerOptions: videoConfig.playerOptions,
      userInfo:{
        userId:null,
        userName:null,
        roomId:null,
      },
      videoInfo:{
        src:""
      }
    }
  },
  mounted() {
  },
  methods: {

    //===============
    //发送消息
    setMyassg(){

      console.log(this.player.currentSource());
      let msg = socketMsg.chatFun.send(this.chatInfo.myMassg)
      this.chatInfo.myMassg=""
      this.chatInfo.Massgs.push(JSON.parse(msg))
      this.websock.send(msg)
      this.$refs.scrollbarRef.setScrollTop(99999)
      ElMessage.success('发送成功.')
    },
    //视频
    //================
    //切换资源
    setVideoSrc(){
      let msg = socketMsg.videoFun.src(this.videoInfo.src)
      this.websock.send(msg)

      this.player.src({src: this.videoInfo.src, type: 'video/mp4'})

      this.player.load()
      // this.player.currentType("video/mp4")
      console.log(this.player.currentSource());

      this.dialog.dialogMenu = false
      ElMessage.success('切换成功.')
    },
    //房间相关
    //==============
    //进入房间
    loginRoom(){
      if(this.userInfo.roomId == null || this.userInfo.roomId == ""){
        ElMessage.error('喊你龟儿子选房间')
      }else{
        socketMsg.init(this.userInfo)//初始化
        let msg = socketMsg.roomFun.joinRoom()//加入房间
        this.websock.send(msg)
        this.dialog.dialogRoom = false
      }
      
    },
    //登录
    login(){
      if(this.userInfo.userName == null || this.userInfo.userName == ""){
        ElMessage.error('昵称呢？')
      }else{
        this.initWebSocket(this.userInfo.userName)
      }
    },
    //===============
    //socket连接初始化
    initWebSocket: function (loginName) {
      // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
      //this.websock = new WebSocket("ws://" + url + "/websocket/"+loginName+"/"+sessionId);
      // this.websock = new WebSocket("ws://127.0.0.1:1999/websocket/" + loginName);
      this.websock = new WebSocket("ws://"+ window.location.host +"/websocket/" + loginName);
      this.websock.onopen = this.websocketonopen;
      this.websock.onerror = this.websocketonerror;
      this.websock.onmessage = this.websocketonmessage;
      this.websock.onclose = this.websocketclose;
    },
    websocketonopen: function () {
      //如果是登录连接那么就开启选择房间
      if (this.dialog.dialogLogin) {
        this.dialog.dialogLogin = false
        this.dialog.dialogRoom = true
        ElMessage.success("连接成功")
      }
    },
    websocketonerror: function (e) {
      console.log("WebSocket连接发生错误", e);
    },
    websocketonmessage: function (e) {
      console.log("后端来消息咯", e.data);
      let datas = JSON.parse(e.data)
      if(datas.type == 1){
        //===房间事件===
        if(datas.data.type==0){
          //房间列表
          this.socketInfo.roomList = datas.data.roomList
        }else if(datas.data.type == 1){
          this.chatInfo.Massgs.push(datas)
          ElMessage.success(datas.ownerId+"进入了房间")
          this.$refs.scrollbarRef.setScrollTop(99999)
        }else if (datas.data.type == 2) {
          this.chatInfo.Massgs.push(datas)
          ElMessage.success(datas.ownerId+"离开了房间")
          this.$refs.scrollbarRef.setScrollTop(99999)
        }
      }else if(datas.type == 2){
        //===视频事件===
        this.socketInfo.socketMsgStatus = true //会产生回调
        if(datas.data.type == 0){
          if (datas.data.play==0) {//暂停
            this.player.pause()
          }else{//播放
            this.player.play()
          }
        }else if(datas.data.type == 1){//切换进度
          this.player.currentTime(datas.data.reach)
        }else if(datas.data.type == 2){
          this.videoInfo.src = datas.data.src
          this.player.src({src: this.videoInfo.src, type: 'video/mp4'})
          this.player.load()
        }
      }else if(datas.type == 3){
        //消息事件
        if (datas.data.type == 0) {
          // max-height
          this.chatInfo.Massgs.push(datas)
          this.$refs.scrollbarRef.setScrollTop(99999)
        }

      }else{
        ElMessage.error("未知消息:"+datas)
      }
    },
    websocketclose: function (e) {
      this.socketStatus = false
      console.log("WebSocket连接关闭", e);
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

    //视频控件初始化完成
    videoReadey(player) {
      this.player = player.target.player
      this.videoInfo.src = this.player.src()
    },
    // 播放回调
    onPlayerPlay($event) {
      if(!this.socketInfo.socketMsgStatus){
        //不是后端带来的回调则执行
        let msg = socketMsg.videoFun.play(1)
        this.websock.send(msg)
      }else{
        this.socketInfo.socketMsgStatus = false
      }
    },
    // 暂停回调
    onPlayerPause($event) {
      if(!this.socketInfo.socketMsgStatus){
        //不是后端带来的回调则执行
        let msg = socketMsg.videoFun.play(0)
        this.websock.send(msg)
      }else{
        this.socketInfo.socketMsgStatus = false
      }
    },
    // 视频播完回调
    onPlayerEnded($event) {
      console.log("完成");
      this.noOpen = true;
    },
    //播放进度条回调
    onPlayerSeeked($event) {
      if(!this.socketInfo.socketMsgStatus){
      //太jr快了，要节流
        if (this.socketInfo.scoketThrottle) {
          this.socketInfo.scoketThrottle = false
          setTimeout(() => {
            let msg = socketMsg.videoFun.reach(this.player.currentTime())
            this.websock.send(msg)
            this.socketInfo.scoketThrottle = true
            console.log(this.player.currentTime());
          }, 100);
        }
      }else{
        this.socketInfo.socketMsgStatus = false
      }
    }
  }

})
</script>
<style scoped>

</style>