<template>
    <el-container>


        <el-header>
            <layoutHeader />
        </el-header>

        <el-main>
            <router-view @getIntoRoom="getIntoRoom"></router-view>
        </el-main>

        <el-footer>
            <layoutFooter />
        </el-footer>
    </el-container>
</template>

<script >
import layoutHeader from "../layout/layoutHeader.vue";
import layoutFooter from "../layout/layoutFooter.vue";
import otherFun from "../utils/socketMsgother"
import socket from '../utils/socketFun';
import { mapState } from "vuex"
// import HuanheVideo from '../components/Huanhe-video.vue';
// import chat from "../components/chat.vue"
export default {
    name: "viewsVideo",
    components: {
        layoutFooter,
        layoutHeader,
        // HuanheVideo,
        // chat
    },
    //专门来读取vux的数据
    computed:{
      ...mapState(["Allinfo","MyWebSocket"])
    },
    data() {
        return {}
    },
    mounted() {
        //用户名暂时按照username存的
        socket.initWebSocket(this.Allinfo.user.userName)
        this.$store.commit("initMyWebSocket",socket.getWebsock())

    },
    methods: {

      // 进入房间回调
      getIntoRoom(){
        console.log(this.Allinfo);
        this.MyWebSocket.send(otherFun.getIntoRoomFun(this.Allinfo))
        this.$router.push("video")
      },
    }

}
</script>
<style scoped>
/* Use flexbox to center the div-video element horizontally and vertically */
/* 将#div-video元素设置为flex布局，使其内部元素在水平和垂直方向上都居中 */
#div-video {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 20px;
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