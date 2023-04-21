<template>
  <!-- 中间组件 -->

  <div id="div-main" class="div-main">
    <div class="container">
      <div id="div-video">
        <huanhe-video ref="huanheVideo" />
      </div>
      <div class="chat-wrapper" id="chatWrapper">
        <el-scrollbar ref="scrollbarRef" :height="scrollbarHeight">
          <chat v-for="item in getRoomChatInfoMassgs" :key="item" :chats="item" :user-name="getUserUserName"
            @ScrollbarRef="onScrollbarRef" />
        </el-scrollbar>
        <div class="send-msg" id="sendMsg">
          <el-input v-model="myMassg" @change="setMyassg" placeholder="发送消息" />
          <el-button @click="setMyassg">发送</el-button>
        </div>
      </div>
    </div>

    <!-- 房间设置抽屉 -->
    <el-drawer v-model="this.$store.state.setRoom" direction="btt">
      <template #header>
        <h4>房间设置</h4>
      </template>
      <template #default>
        <div>
          <el-form :inline="true" :model="videoSrc" :rules="rules" ref="videoSrcForm">
            <el-form-item label="视频源:" prop="src">
              <el-input v-model="videoSrc.src" style="width: 300px;" />
            </el-form-item>
            <el-form-item label="视频类型:" prop="type">
              <el-select style="width: 100px;" v-model="videoSrc.type" placeholder="请选择视频源类型">
                <el-option label="mp4" value="video/mp4" />
                <el-option label="m3u8" value="m3u8" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="setVideoSrc">切换视频源</el-button>
            </el-form-item>
          </el-form>
        </div>
      </template>
    </el-drawer>


  </div>
</template>

<script >
import HuanheVideo from '../components/Huanhe-video.vue';
import chat from "../components/chat.vue"
import otherFun from '../utils/socketMsgother';
import { ElMessage } from "element-plus";
import { mapGetters, mapState } from "vuex"
import { joinRoom } from "../api/room"


export default {
  name: "viewsVideo",
  components: {
    HuanheVideo,
    chat
  },
  //专门来读取vux的数据
  computed: {
    ...mapState(["MyWebSocket", "Allinfo", "adaptiveMin"]),
    ...mapGetters(["getRoomChatInfoMassgs", "getUserUserName", "getVideoInfoSrc","getUserSocketStatus"]),
  },
  data() {
    return {
      myMassg: "",   //我要发送的消息
      scrollbarHeight: "100px", //消息的长度
      videoSrc: {
        src: null,
        type: null
      },
      //表单验证
      rules: {
        src: [{ required: true, message: '请输入视频源', trigger: 'blur', }, { pattern: /^([hH][tT]{2}[pP]:\/\/|[hH][tT]{2}[pP][sS]:\/\/)(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+(..)+$/, message: '请输入正确的视频源', trigger: 'blur' }],
        type: [{ required: true, message: '请输入视频类型', trigger: 'blur', }]
      }
    }
  },
  mounted() {
    // console.log(this.getVideoInfoSrc.url);
    this.videoSrc.src = this.getVideoInfoSrc.src
    this.videoSrc.type = this.getVideoInfoSrc.type
    console.log(this.videoSrc.type);
    // 初始化房间信息
    //获取路由中的房间id
    this.roomId = this.$route.params.roomId
    
    //将roomid放到路由中
    this.$store.commit("setRoomRoomId", this.roomId)

    //自适应
    let screenWidth = document.body.offsetWidth;
    this.$store.commit("setadaptiveMin", (screenWidth < 700))
    //小布局则初始化
    this.setWandH()
    window.addEventListener('resize', this.setWandH)
    //延迟执行控件读取
    setTimeout(() => { this.setWandH() }, 1);

    //如果是连接状态则不是重连
    if (this.getUserSocketStatus) {
      this.initRoom()
      console.log("新连接同步");
    }
  },
  watch: {
    //对连接状态监听
    getUserSocketStatus: {
      handler(nv) {
        console.log(nv,7777777);
        //如果变化到连接
        if (nv) {
          joinRoom(this.roomId).then(res => {
            if (res.code!=200) {
              //跳回到选房间
              ElMessage.error("房间已解散")
              this.$router.push("/pc/selectRoom")
            }else{
              this.initRoom()
              console.log("重连同步");
              ElMessage.success("回到房间成功成功")
            }
          })
        }
      },
      deep: false,
    },

  },
  methods: {
    //初始化数据
    initRoom(){
      //如果没连接那么就是刷新进入
      if (this.MyWebSocket!=null) {
        this.MyWebSocket.send(otherFun.initRoom(this.Allinfo))
      }
      
    },
    // 设置长宽
    setWandH() {
      const mainHeight = document.getElementsByTagName("main")[0].offsetHeight
      const divVideoHeight = document.getElementById('div-video').offsetHeight
      const sendMsgHeight = document.getElementById('sendMsg').offsetHeight
      if (this.adaptiveMin) {
        this.scrollbarHeight = (mainHeight - divVideoHeight - sendMsgHeight - 40) + "px"
      } else {
        this.scrollbarHeight = (divVideoHeight - sendMsgHeight - 20) + "px"
      }
    },

    onScrollbarRef() {
      //组件渲染回调,自动底部
      this.$refs.scrollbarRef.setScrollTop(this.$refs.scrollbarRef.wrapRef.firstChild.clientHeight)
    },

    setVideoSrc() {
      //设置视频资源
      let than = this
      this.$refs.videoSrcForm.validate(valid => {
        if (valid) {
          //切换视频资源
          than.$store.commit("setVideoInfoSrc", {
            src: than.videoSrc.src,
            type: than.videoSrc.type
          })//防止绑定

          than.MyWebSocket.send(otherFun.setVideoSrcFun(than.videoSrc))
          ElMessage.success("切换视频源成功")
        }
      })
    },
    //===============
    //发送消息
    setMyassg() {
      if (this.myMassg!="") {
      let socketMsg = otherFun.setMyassg(this.myMassg)
      this.MyWebSocket.send(socketMsg) //发送消息
      this.$store.commit("setRoomChatInfoMassg", JSON.parse(socketMsg))
      this.myMassg = "" //清空编辑框
      ElMessage.success('发送成功.')        
      }

    },
  }

}
</script>
<style>
/* Use flexbox to center the div-video element horizontally and vertically */
/* 将#div-video元素设置为flex布局，使其内部元素在水平和垂直方向上都居中 */
#div-video {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0px;
}

.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  margin: 10px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.el-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.el-main {
  flex: 1;
}

.el-footer {
  flex-shrink: 0;
}

.send-msg {
  display: flex;
  align-items: center;
}

.room-info {
  margin: 0px 20px;
  max-width: 30%;
  display: block;
}

.up {
  max-width: 30%;
  margin: 0px 0%;
}


/* cs */
@media (min-width: 700px) {

  .container {
    display: grid;
    grid-template-columns: 18fr 6fr;
    gap: 10px;
  }

  #div-video {
    grid-column: 1;
  }

  .chat-wrapper {
    display: flex;
    flex-direction: column;
    max-height: 640px;
  }

  .send-msg {
    display: flex;
    align-items: center;
  }

  .send-msg el-input {
    margin-right: 10px;
  }

  .send-msg el-button {
    min-width: 60px;
  }
}
</style>