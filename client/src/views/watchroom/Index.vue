<template>
    <div class="video" :style="initTheme()">
        <header-bar></header-bar>
        <div class="video-main">
            <div class="content-left">
                <div v-if="!loading" class="video-player">
                    <watchroom-player
                    :vid="vid" 
                    :theme="theme.primaryColor" 
                    :part="part"
                    :resources="resources" 
                    :isAdmin="isAdmin"
                    @getInstance="getInstance"
                    @change="changePart"
                    @ready="ready"
                    @play="play"
                    @pause="pause"
                    @seek="seek"
                    @switchPid="switchPid"
                    ></watchroom-player>
                    <div class="video-title-box">
                        <p class="video-title">{{ videoInfo.title }}</p>
                        <p v-show="videoInfo.copyright" class="copyright">
                            <n-icon class="icon" color='#fd6d6f'>
                                <forbid />
                            </n-icon>
                            <span>{{ t("video.copyright") }}</span>
                        </p>
                    </div>
                </div>
                <div v-else class="player-skeleton">
                    <n-skeleton height="500px" />
                    <n-skeleton text :repeat="2" />
                    <n-skeleton text width=" 60%" />
                </div>
            </div>
            <div class="content-right">
                <!--作者信息-->
                <author-card :loading="loading" :author="videoInfo?.author"></author-card>
                <!-- 视频分集 -->
                    <n-tabs type="line" animated>
                        <n-tab-pane name="oasis" tab="房间聊天">
                           <msg 
                           @webSendMesg = "webSendMesg"
                           :msgDetails = "msgDetails" 
                           :refNub = "updateFlag"
                           ></msg>
                        </n-tab-pane>
                    <n-tab-pane name="jay chou" tab="播放列表">
                        <div v-if="resources.length > 1">
                            <part-list :resources="resources" :active="part" @change="changePart"></part-list>
                        </div>
                    </n-tab-pane>
                </n-tabs>
            </div>
        </div>
    </div>
</template>

<script setup >
import { useRoute, useRouter } from 'vue-router';
import { NIcon, NTime, NButton, NSkeleton, NTabs,NTabPane, useNotification,useDialog } from 'naive-ui';
import { onBeforeMount, onBeforeUnmount, ref } from 'vue';
import PartList from './component/PartList.vue';

import AuthorCard from './component/AuthorCard.vue';
import watchroomPlayer from "../../components/video-player/watchroomPlayer.vue";
import msg from './component/msg.vue';
import { storageData,generateRandomString } from '../../utils/localStorage';


const route = useRoute();
const router = useRouter();
const dialog = useDialog();
const roomId = parseInt(route.params.roomId.toString());
const part = ref(1);//当前分集
const vid = ref(0)
const isAdmin = ref(false);//是否是管理员
const more = ref(false);//是否展开简介
const instance = ref<Artplayer | null>(null)
const socketMsgStatus = ref(true); //socket链接消息回调 true:正常发送消息 false:不发送消息
const newUser = ref(true);//是否是新用户

const roomInfo = ref(null);//房间信息

const updateFlag = ref(0);//响应式数据，用于更新页面

const msgDetails = [];//消息详情

const initTheme = () => {
    return {
        "--primary-color": theme.primaryColor,
        "--hover-color": theme.primaryHoverColor
    }
}


const notification = useNotification();
//获取视频信息
const loading = ref(true);
const resources = ref<Array>([]);
const videoInfo = ref(null);
const getVideoInfo = (vid) => {
    getVideoInfoAPI(vid).then((res) => {
        if (res.data.code === statusCode.OK) {
            videoInfo.value = res.data.data.video;
            resources.value = res.data.data.video.resources;

            //设置播放的资源
            if (!resources.value[part.value - 1]) {
                part.value = 1;
            }
            //修改网站标题
            document.title = `和朋友一起看:${res.data.data.video.title}-${globalConfig.title}`
            loading.value = false;
        }
    })
}



let SocketURL = "";
let websocket = null;
//初始化weosocket
const initWebSocket = async(rid) => {
    let clientId = localStorage.getItem("ws_client_id");
    if (!clientId) {
        clientId = createUuid();
        localStorage.setItem("ws_client_id", clientId);
    }
    const wsProtocol = window.location.protocol === 'http:' ? 'ws://' : 'wss://';
    const domain = globalConfig.domain || window.location.host;
    SocketURL = wsProtocol + domain + `/api/v1/websocket/watchroom/${rid}/`;
        // 获取token
    const res = await getAccessToken();
    if (res.data.code === statusCode.OK) {
        storageData.set("access_token", res.data.data.token, 5);
        SocketURL += storageData.get("access_token");
        websocket = new WebSocket(SocketURL);
        websocket.onmessage = websocketOnmessage;
        websocket.onclose = websocketClose;
        // 获取视频信息        
        if (roomInfo.value) {
            vid.value = roomInfo.value.vid;
            getVideoInfo(roomInfo.value.vid);
        }
    }
    
}


//数据接收
const websocketOnmessage = (e) => {
    // 将消息转换为json对象
    const data = JSON.parse(e.data);
    // 消息处理
    console.log(data);
    if(data.type && instance.value){
        socketMsgStatus.value = false;
        switch(data.type){
            case watchroomCode.play:
                // 播放
                instance.value.play();
                break;
            case watchroomCode.pause:
                instance.value.pause()
                break;
            case watchroomCode.seek:
                instance.value.seek = data.data         
                break;
            case watchroomCode.switchPiv:
                // 切换分集
                socketMsgStatus.value = true;
                part.value = data.data;
                break;
            case watchroomCode.sendMsg:
                // 处理发送的消息
                socketMsgStatus.value = true;
                msgDetails.push(data.data);
                updateFlag.value += 1;
                break;
            case watchroomCode.ready:
                // 新进入用户准备就绪
                socketMsgStatus.value = true;
                syncVideo()
                break;
            case watchroomCode.readyAck:
                // 新进入同步视频
                if(newUser.value){
                    if(data.data.currentTime !== instance.value.currentTime){
                        instance.value.seek = data.data.currentTime;
                    }
                    if(data.data.playing){
                        instance.value.play();
                    }
                    newUser.value = false;
                }
                break;
        }
    }

}

// 链接失败
const websocketClose = (e) => {
    console.log("链接关闭",e);
}
// 发送消息
const websocketSend = (data) => {
    if (websocket) {
        websocket.send(JSON.stringify(data));
    }
}


// 房间管理员对新进入的视频同步
const syncVideo = () => {
    if (isAdmin.value && instance.value) {
        websocketSend({
            type: watchroomCode.readyAck,
            data: { part: part.value, currentTime: instance.value.currentTime, playing: instance.value.playing }
        })
    }
}


// 获取房间信息
const getRoomInfo = (rid) =>{
    getRoomInfoApi(rid).then((res) => {
        if(res.data.code === statusCode.OK) {
            // 进入成功
            roomInfo.value = res.data.data;
            part.value = res.data.data.pid;
            if(res.data.data.roomInfo.adminId === storageData.get("user_info").uid){
                isAdmin.value = true;
            }
            dialog.success({
                title: "进入房间成功",
                content: "开始一起看吧!",
                positiveText: "准备就绪",
                onPositiveClick: () => {
                    initWebSocket(rid);
                },
    });  
            
        }else{
            // 其他错误
            console.log(res.data.message);
            notification.error({
                title: res.data.message,
                duration: 3000,
            });
            router.push({
                name: 'goinRoom',
                params: {
                    roomId: roomId
                }
            })
        }
    })
}


const webSendMesg = (data) => {
     // 发送消息
     websocketSend({
        type:watchroomCode.sendMsg,
        data:data
     })
     msgDetails.push(data);
     
} 


// 数据处理
// const emits = defineEmits(['change','ready','play','pause','seek']);
// 更新instance
const getInstance = (art) => {
    instance.value = art;
}

const ready = () => {
    console.log("redi");
    
    if(roomInfo.value){
        websocketSend({
            type: watchroomCode.ready
        })
    }else{
        socketMsgStatus.value = true;
    }

}

const play = () => {
    if(roomInfo.value && socketMsgStatus.value){
        websocketSend({
            type: watchroomCode.play
        })
    }else{
        socketMsgStatus.value = true;
    }
}

const pause = () => {
    if(roomInfo.value && socketMsgStatus.value){
        websocketSend({
            type: watchroomCode.pause
        })
    }else{
        socketMsgStatus.value = true;
    }
}

const seek = (seek) => {
    if(roomInfo.value && socketMsgStatus.value){
        websocketSend({
            type: watchroomCode.seek,
            data: seek
        })
    }else{
        socketMsgStatus.value = true;
    }
}

const switchPid=(pid) =>{
    console.log("切换分集",pid,socketMsgStatus.value);
    if(roomInfo.value && socketMsgStatus.value){
        roomInfo.value.pid = pid;
        websocketSend({
            type: watchroomCode.switchPiv,
            data: pid
        })
        if(isAdmin.value){
            // 房间管理员切换分集
            updateRoomApi(roomId,roomInfo.value).then((res)=>{
                if(res.data.code === statusCode.OK){
                    console.log("切换分集成功",roomInfo.value);
                }
            })
        }
    }else{
        socketMsgStatus.value = true;
    }
}


const changePart = (target) => {
    if (resources.value[target - 1]) {
        part.value = target;
    }
    router.replace({ query: { p: part.value } });
}

onBeforeMount(() => {
    // 尝试获取视频信息，并且初始化websocket
    getRoomInfo(roomId);
    if (route.query.p) {
        part.value = Number(route.query.p);
    }
})

onBeforeUnmount(() => {
    if (websocket) {
        websocket.close();
    }
})
</script>

<style lang="less" scoped>
.video {
    min-height: 100%;
    width: 100%;
}

.video-main {
    width: calc(100% - 100px);
    background: #fff;
    margin: 20px auto 0 auto;
    display: flex;
    flex-wrap: nowrap;
}

.content-left {
    width: calc(100% - 350px);
    min-width: 680px;

    .video-player {
        margin: 0 auto;
        width: calc(100% - 120px);
        /*16:9*/
        min-width: 680px;
        min-height: 382px;
    }

    .video-title-box {
        width: 100%;
        height: 36px;
        display: flex;

        .video-title {
            width: calc(100% - 160px);
            margin: 0;
            font-size: 20px;
            color: #303030;
            line-height: 30px;
            font-weight: 500;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .copyright {
            width: 180px;
            display: flex;
            align-items: center;
            justify-content: flex-end;
            font-size: 12px;
            color: #636363;

            .icon {
                padding: 0 6px;
            }
        }
    }

    //骨架占位
    .player-skeleton {
        margin: 0 auto;
        width: calc(100% - 120px);
    }
}

.video-toolbar {
    color: #505050;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid #e5e9f0;

    .toolbar-right {
        display: inline-block;

        span {
            margin-left: 20px;
        }
    }
}

//简介
.desc {
    .desc-content {
        max-height: 36px;
        font-size: 14px;
        color: #212121;
        line-height: 18px;
        overflow: hidden;
        white-space: pre-line;
        transition: height 0.3s;
    }

    .open {
        // height: auto;
        max-height: none;
    }
}

/**右半部分 */
.content-right {
    width: 350px;
    height: 600px;
    min-width: 350px;
}
</style>