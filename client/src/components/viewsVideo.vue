<template>
  <!-- 中间组件 -->

  <div id="div-main" class="div-main">

    <!-- 小布局 -->
    <div v-show="adaptiveMin">
      <div id="div-video">
        <huanhe-video />
      </div>
      <el-scrollbar ref="scrollbarRef" :height="scrollbarHeight">
        <chat v-for="item in getRoomChatInfoMassgs" :key="item" :chats="item" :user-name="getUserUserName"
          @ScrollbarRef="onScrollbarRef" />
      </el-scrollbar>
      <div class="send-msg" id="sendMsg">
        <el-input v-model="myMassg" @change="setMyassg" placeholder="发送消息" />
        <el-button @click="setMyassg">发送</el-button>
      </div>
    </div>

    <!-- 大布局 -->
    <div v-show="!adaptiveMin">
      <el-row>
        <el-col :span="18">
          <div id="div-video">
            <huanhe-video />
          </div>
        </el-col>
        <el-col :span="6">
          <el-scrollbar ref="scrollbarRef" max-height="640px">
            <chat v-for="item in getRoomChatInfoMassgs" :key="item" :chats="item" :user-name="getUserUserName"
              @ScrollbarRef="onScrollbarRef" />
          </el-scrollbar>
          <div class="send-msg">
            <el-input v-model="myMassg" @change="setMyassg" placeholder="发送消息" />
            <el-button @click="setMyassg">发送</el-button>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 当前房间详细 -->






    <!-- 按钮 -->
    <!-- <div class="main">
      <el-button type="success" round>Success</el-button>
      <el-button type="danger" round>Danger</el-button> -->
    <!-- <el-row style="margin: 0;">
        <el-col >
          
        </el-col>
        <el-col >
          <div class="up"> -->
    <!-- <button class="card1" @click="drawer.setRoom = true">设置</button>
            <button class="card2" @click="outRoom">退出</button> -->


    <!-- </div>
        </el-col>
      </el-row> -->

    <!-- </div> -->
    <!-- end按钮 -->



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

              <button @click="setWandH">测试测试</button>
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
import { ref } from 'vue' //监听

export default {
  name: "viewsVideo",
  components: {
    HuanheVideo,
    chat
  },
  //专门来读取vux的数据
  computed: {
    ...mapState(["MyWebSocket", "Allinfo", "adaptiveMin"]),
    ...mapGetters(["getRoomChatInfoMassgs", "getUserUserName", "getVideoInfoSrc"]),
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

    //自适应

    let screenWidth = document.body.offsetWidth;
    this.$store.commit("setadaptiveMin", (screenWidth < 700))
    //小布局则初始化

    this.setWandH()
    window.addEventListener('resize', this.setWandH)


  },
  methods: {
    // 设置长宽
    setWandH() {
      if (this.adaptiveMin) {

        //安卓端
        let than = this
        setTimeout(function () {
          let mainHeight = document.getElementsByTagName("main")[0].offsetHeight
          let divVideoHeight = document.getElementById('div-video').offsetHeight
          let sendMsgHeight = document.getElementById('sendMsg').offsetHeight
          than.scrollbarHeight = (mainHeight - divVideoHeight - sendMsgHeight - 40) + "px"
        }, 1)
      }
    },

    onScrollbarRef() {
      //组件渲染回调,自动底部
      this.$refs.scrollbarRef.setScrollTop(this.$refs.scrollbarRef.wrapRef.firstChild.clientHeight)
    },
    outRoom() {
      ElMessage.success("下个版本再弄吧，手动返回就行,加入交流群一起开发:486601640")
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
      let socketMsg = otherFun.setMyassg(this.myMassg)
      this.MyWebSocket.send(socketMsg) //发送消息
      this.$store.commit("setRoomChatInfoMassg", JSON.parse(socketMsg))
      this.myMassg = "" //清空编辑框
      ElMessage.success('发送成功.')
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
</style>