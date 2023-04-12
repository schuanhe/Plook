<template>
  <!-- :options="playerOptions"  -->
  <video-player controls :options="playerOptions" ref="videoPlayer" @ready="videoReadey" @play="onPlayerPlay($event)"
    @pause="onPlayerPause($event)" @ended="onPlayerEnded($event)" @seeked="onPlayerSeeked()" />


  <div :style="{ width: divWidth, height: divHeight }"></div>
  <el-button type="primary" @click="dialog.dialogMenu = true">菜单</el-button>
  <el-scrollbar ref="scrollbarRef" max-height="400px" :style="{ width: ((divWidth - 80) + 'px') }">
    <chat v-for="item in chatInfo.Massgs" :key="item" :chats="item" :user-name="userInfo.userName"></chat>
  </el-scrollbar>
  <el-input v-model="chatInfo.myMassg" @change="setMyassg" placeholder="发送消息" />
  <el-button @click="setMyassg">发送</el-button>

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
    <el-button type="primary" @click="login">登 录</el-button>
  </el-dialog>
  <!-- 选房间弹窗 -->
  <el-dialog v-model="dialog.dialogRoom" title="选一哈房间嘛" width="30%" :show-close=false draggable>
    <el-form-item label="选择房间">
      <el-select v-model="userInfo.roomId" class="m-2" placeholder="选则一个存在的房间">
        <el-option v-for="item in socketInfo.roomList" :key="item" :label="item" :value="item" />
      </el-select>
    </el-form-item>
    <el-button @click="loginRoom" type="primary">进入这个房间</el-button>
  </el-dialog>
  <!-- 菜单弹窗 -->
  <el-dialog v-model="dialog.dialogMenu" title="菜单" draggable>
    <el-form-item label="切换视频源">
      <el-input placeholder="请输入src" v-model="videoInfo.src" />
    </el-form-item>
    <el-button type="primary" @click="setVideoSrc">切 换</el-button>
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
  created() {
    this.divWidth = document.body.clientWidth
    this.divHeight = document.body.clientHeight
  },
  data() {
    return {
      dialog: {
      
        dialogLogin: true,//登录弹窗
        dialogRoom: false, //房间弹窗
        dialogMenu: false //菜单弹窗
      },
      socketInfo: {
        status: false, //连接状态
        roomList: [],//房间列表
        scoketThrottle: true,//节流函数
        socketMsgStatus: false,//后端会回调的 状态
      },
      chatInfo: {
        myMassg: '',
        Massgs: [],
      },
      // videoSrc:'',
      // myMassg:'',//我要发送的消息
      // Massgs:[],
      divHeight: 0,//动态高宽
      divWidth: 0,//动态高宽
      // drawer:false,
      // scoketThrottle: true,//节流函数
      // socketUserName: '',
      // socketStatus: false,//连接状态
      // loginName: '',//登录的用户名
      // playerTime: 0,//视频进度时间
      // playStatus: false,//视频播放与否
      // playerTimeMax: 0,//视频时常
      playerOptions: videoConfig.playerOptions,
      userInfo: {
        userId: null,
        userName: null,
        roomId: null,
      },
      videoInfo: {
        src: ""
      }
    }
  },
  mounted() {
  },
  methods: {}

})
</script>
<style scoped></style>