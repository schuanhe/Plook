<template>
    <el-container>
        <el-header>
            <layoutHeader />
        </el-header>
        <el-main>
            <router-view></router-view>
        </el-main>
        <el-footer>
            <layoutFooter />
        </el-footer>
    </el-container>
</template>

<script setup>
import layoutHeader from "../layout/layoutHeader.vue";
import layoutFooter from "../layout/layoutFooter.vue";
import socket from '../utils/socketFun';
import Cookies from "js-cookie";
import { mapState } from "vuex"
import { onBeforeMount } from "vue"
import { ElMessage } from "element-plus";
import { useRouter } from 'vue-router';
import { getRoomList } from "../api/room";
// import HuanheVideo from '../components/Huanhe-video.vue';
// import chat from "../components/chat.vue"
const router = useRouter();
      //进入房间回调

// 初始话
const init = () =>{
    if(Cookies.get("userId")){
        // 去获取房间列表
        router.push("/pc/selectRoom")
    }else{
        ElMessage.error("请先登录")
        router.push("/")
    }    
}

// 页面挂载前请求
onBeforeMount(()=>{
    init()
})
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