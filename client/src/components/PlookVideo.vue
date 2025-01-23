<template>
  <view class="plook-video">
    <video 
      @play="videoPlayCallback" 
      @timeupdate="videoTimeUpdateCallback" 
      @pause="videoPauseCallback"
      @seeking="handleUserAction"
      @seeked="handleUserAction"
      id="myVideo" 
      :src="src" 
      :danmu-list="danmuList"
      danmu-btn=true 
      enable-danmu=true
    >
    </video>
  </view>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { socketIo, socketMessage } from "../utlis/socketIo";

const src = ref('https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/2minute-demo.mp4');
const danmuList = ref([
  {
    text: '第 1s 出现的弹幕',
    color: '#ff0000',
    time: 1
  },
  {
    text: '第 3s 出现的弹幕',
    color: '#ff00ff',
    time: 3
  }
]);
const danmuValue = ref('');
let videoContext = null;

// 视频进度
let videoTime = ref(0);

// 视频当前播放时常
let videoCurrentTime = ref(0);
//
let interval;

// 只保留用户操作标记
const isUserAction = ref(false);

// 上次记录的时间点
const lastTime = ref(0);
// 上次发送同步的时间戳
const lastSyncTimestamp = ref(0);

// 视频事件处理
const videoPlayCallback = (e) => {
  if (interval) {
    videoTime.value = videoCurrentTime.value;
  } else {
    interval = setInterval(() => {
      videoTime.value++;
    }, 1000);
  }
  
  // 播放事件立即发送
  socketIo.send(socketMessage.sendRoomInfo({
    type: 'videoAction',
    data: {
      roomId: getCurrentRoomId(),
      action: 'play',
      currentTime: videoCurrentTime.value
    }
  }));
};

const videoPauseCallback = (e) => {
  clearInterval(interval);
  interval = null;
  
  // 暂停事件立即发送
  socketIo.send(socketMessage.sendRoomInfo({
    type: 'videoAction',
    data: {
      roomId: getCurrentRoomId(),
      action: 'pause',
      currentTime: videoCurrentTime.value
    }
  }));
};

const videoTimeUpdateCallback = (e) => {
  const currentTime = e.detail.currentTime;
  const timeDiff = Math.abs(currentTime - lastTime.value);
  const now = Date.now();
  
  // 检测是否为用户拖动进度条：
  // 1. 时间差大于0.5秒
  // 2. 距离上次同步超过500ms（防止频繁发送）
  if (timeDiff > 0.5 && now - lastSyncTimestamp.value > 500) {
    socketIo.send(socketMessage.sendRoomInfo({
      type: 'videoAction', 
      data: {
        roomId: getCurrentRoomId(),
        action: 'seek',
        currentTime: currentTime
      }
    }));
    lastSyncTimestamp.value = now;
  }
  
  // 更新记录的时间点
  lastTime.value = currentTime;
  videoCurrentTime.value = currentTime;
};

// 用户操作事件监听（只用于进度条拖动）
const handleUserAction = () => {
  isUserAction.value = true;
};

// 视频操作监听
const listenVideoActions = () => {
  socketIo.getSocket().on('videoAction', (message) => {
    const { action, currentTime, videoUrl } = message.data;
    
    switch(action) {
      case 'play':
        videoContext?.play();
        if (Math.abs(currentTime - videoCurrentTime.value) > 0.5) {
          videoContext?.seek(currentTime);
        }
        break;
      case 'pause':
        videoContext?.pause();
        break;
      case 'seek':
        if (Math.abs(currentTime - videoCurrentTime.value) > 0.5) {
          videoContext?.seek(currentTime);
        }
        break;
      case 'changeSource':
        src.value = videoUrl;
        break;
    }
  });
};

// 切换视频源
const changeVideoSource = (newUrl) => {
  src.value = newUrl;
  // 发送视频源更新事件
  socketIo.send(socketMessage.sendRoomInfo({
    type: 'videoAction',
    data: {
      roomId: getCurrentRoomId(),
      action: 'changeSource',
      videoUrl: newUrl
    }
  }));
};

// 获取当前房间ID
const getCurrentRoomId = () => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  return currentPage.$page?.options?.roomId;
};

// 暴露发送弹幕方法给父组件
const sendDanmuToVideo = (text, color) => {
  console.log('sendDanmuToVideo', text, color);
  
  videoContext?.sendDanmu({
    text,
    color
  });
};

// 监听弹幕消息
const listenDanmuMessages = () => {
  socketIo.getSocket().on('setRoomInfo', (message) => {
    if (message.data.messageType === 'danmu') {
      const { text, color } = message.data.danmu;
      sendDanmuToVideo(text, color);
    }
  });
};

const videoErrorCallback = (e) => {
  uni.showModal({
    content: e.target.errMsg,
    showCancel: false
  });
};

const getRandomColor = () => {
  const rgb = [];
  for (let i = 0; i < 3; ++i) {
    let color = Math.floor(Math.random() * 256).toString(16);
    color = color.length === 1 ? '0' + color : color;
    rgb.push(color);
  }
  return '#' + rgb.join('');
};

onMounted(() => {
  videoContext = uni.createVideoContext('myVideo');
  listenVideoActions();
  listenDanmuMessages();
  lastTime.value = 0;
  lastSyncTimestamp.value = Date.now();
});

onBeforeUnmount(() => {
  clearInterval(interval);
  socketIo.getSocket()?.off('videoAction');
  socketIo.getSocket()?.off('setRoomInfo');
});

// 暴露方法给父组件
defineExpose({
  changeVideoSource,
  sendDanmuToVideo
});
</script>

<style scoped>
.plook-video {
  width: 100%;
  height: 100%;
}

.plook-video video {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>