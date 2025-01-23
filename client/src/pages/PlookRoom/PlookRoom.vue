<template>

  <view class="container">
    <view>

      <uni-fab ref="fab" :pattern="pattern" :content="content" horizontal="right"
               @trigger="trigger" />
    </view>
    <view class="video-card" >
      <view class="video-container  ">
        <plook-video ref="videoRef"/>
      </view>
      <view class=" uni-flex uni-column" >
        <text class="flex-item">当前房间号: {{ currentRoomId }} \n</text>
        <text class="flex-item">{{ roomInfo }}</text></view>
    </view>

    <view class="comments-container video-card video-card-min">
      <message-container v-if="videoRef" :video-ref="videoRef" />
    </view>
  </view>

  <view class="set-video" @click="showSetRoom = !showSetRoom" v-show="showSetRoom.valueOf()">
    <view class="set-imt video-card" @click.stop>
      <uni-forms ref="validForm" :rules="rules" :modelValue="roomData" style="padding: 10px">
        <uni-forms-item label="名称" required name="name">
          <uni-easyinput v-model="roomData.name" placeholder="请输入房间名称" />
        </uni-forms-item>
        <uni-forms-item label="链接" required name="url">
          <uni-easyinput v-model="roomData.url" placeholder="请输入房间链接" />
        </uni-forms-item>
      </uni-forms>
      <button :type="'primary'" style="width: 50%" size="mini" @click="submit">提交</button>
      <button :type="'default'" style="width: 50%; margin-top: 10px" size="mini" @click="showSetRoom = !showSetRoom">取消</button>
    </view>
  </view>


</template>


<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import PlookVideo from "../../components/PlookVideo.vue";
import MessageContainer from "../../components/PlookChat/MessageContainer/Index.vue";
import UniCard from "../../uni_modules/uni-card/components/uni-card/uni-card.vue";
import UniFab from "../../uni_modules/uni-fab/components/uni-fab/uni-fab.vue";
import UniPopup from "../../uni_modules/uni-popup/components/uni-popup/uni-popup.vue";
import UniDrawer from "../../uni_modules/uni-drawer/components/uni-drawer/uni-drawer.vue";
import UniForms from "../../uni_modules/uni-forms/components/uni-forms/uni-forms.vue";
import UniFormsItem from "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue";
import UniEasyinput from "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue";
import {socketIo, socketMessage} from "../../utlis/socketIo"
import { useRoomStore } from "../../store/room";
import { onLoad } from '@dcloudio/uni-app';
// 定义props
const props = defineProps({
  roomId: {
    type: [String, Number],
    default: ''
  }
});

// 设置是否可见
let showSetRoom = ref(false)

const roomData = {
  name: '',
  url: ''
}

const rules = {
  name: {
    required: true,
    message: '请输入房间名称',
    trigger: ['blur', 'change']
  },
  url: {
    required: true,
    message: '请输入房间链接',
    trigger: ['blur', 'change']
  }
}

const pattern = {
  color: '#fff',
  backgroundColor: '#007aff',
  selectedColor: '#fff',
  selectedBackgroundColor: '#007aff',
  shadow: true,
  zIndex: 99
}

const direction = 'vertical'
const content = [
  {
    text: '配置',
    active: false
  },
]


const trigger = (e) => {
  if (e.index === 0){
    showSetRoom.value = true
    console.log('打开')
  }else {
    console.log('关闭')
  }
}
const roomStore = useRoomStore()
const videoRef = ref(null);
const currentRoomId = ref(null);
const roomInfo = ref('');

// socket连接状态
const isSocketConnected = ref(false);

// 更新视频源
const updateVideoSource = (url) => {
  videoRef.value?.changeVideoSource(url);
};

// 处理设置表单提交
const submit = () => {
  if (roomData.url) {
    updateVideoSource(roomData.url);
    showSetRoom.value = false;
  }
};

// 连接socket并加入房间
const connectSocket = async () => {
  try {
    if (!isSocketConnected.value) {
      await socketIo.start();
      isSocketConnected.value = true;
    }

    // 发送加入房间消息
    socketIo.send(socketMessage.sendRoomInfo({
      type: 'join',
      data: {
        roomId: currentRoomId.value,
        userName: useRoomStore().getUserInfo().userName
      }
    }));

    // 监听房间信息
    socketIo.getSocket().on('roomInfo', (message) => {
      roomInfo.value = message.data;
    });

    // 监听错误信息
    socketIo.getSocket().on('error', (error) => {
      uni.showToast({
        title: error,
        icon: 'none'
      });
    });

    // 监听连接断开
    socketIo.getSocket().on('disconnect', () => {
      isSocketConnected.value = false;
      reconnectSocket();
    });

  } catch (error) {
    console.error('Socket连接失败:', error);
    uni.showToast({
      title: 'Socket连接失败，正在重试...',
      icon: 'none'
    });
    setTimeout(reconnectSocket, 3000);
  }
};

// 重连socket
const reconnectSocket = async () => {
  if (!isSocketConnected.value && currentRoomId.value) {
    await connectSocket();
  }
};

// 组件销毁时清理
onBeforeUnmount(() => {
  if (isSocketConnected.value && currentRoomId.value) {
    try {
      socketIo.send(socketMessage.sendRoomInfo({
        type: 'leave',
        data: {
          roomId: currentRoomId.value,
          userName: useRoomStore().getUserInfo().userName
        }
      }));
      socketIo.getSocket()?.off('roomInfo');
      socketIo.getSocket()?.off('error');
      socketIo.getSocket()?.off('disconnect');
      socketIo.close();
    } catch (error) {
      console.error('Socket关闭失败:', error);
    }
    isSocketConnected.value = false;
  }
});

// 使用 onLoad 获取路由参数并连接
onLoad(async (option) => {
  currentRoomId.value = option.roomId || props.roomId;
  if (currentRoomId.value) {
    await connectSocket();
  }
});

</script>


<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}

.video-container {
  width: 100%;
  height: calc(100vw * 9 / 16);
  max-height: 100vh;
}

.comments-container {
  width: 100%;
  flex-grow: 1; /* 让其占据剩余空间 */
  margin-top: 10px;
  overflow-y: auto; /* 如果内容过多，添加滚动条 */
}

.set-video {
  position: fixed;

  transform: translate(0%, -100%); /* 通过transform移动元素自身的位置，使其真正的中心与窗口的中心对齐 */
  z-index: 10; /* 确保元素位于其他元素之上 */
  width: 100%; /* 设置元素的宽度 */
  height: 100%; /* 设置元素的高度 */

  display: flex; /* 使用flex布局 */
  align-items: center; /* 垂直居中子元素 */
  justify-content: center; /* 水平居中子元素 */
}

.set-imt {
  position: fixed;
  transform: translate(0, 50%); /* 通过transform移动元素自身的位置，使其真正的中心与窗口的中心对齐 */
  z-index: 10; /* 确保元素位于其他元素之上 */
  width: 100%; /* 设置元素的宽度 */
  height: 300px; /* 设置元素的高度 */
  background: white;
  /* 设置剧中 */
  display: flex;
  flex-direction: column;
}

.video-card-min {
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 卡片阴影 */
  border-radius: 5px; /* 卡片边框圆角 */
}

.video-card-min-inset {
  box-shadow: inset 0 2px 5px rgba(0, 0, 0, 0.3); /* 内凹阴影 */
  border-radius: 5px; /* 卡片边框圆角 */
}


@media (min-width: 768px) {
  .container {
    flex-direction: row;
    justify-content: space-between;
  }

  .video-container {
    width: 60vw;
    height: calc(60vw * 9 / 16);
  }

  .video-card {
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 卡片阴影 */
    border-radius: 5px; /* 卡片边框圆角 */
    padding: 5px;
    margin: 10px;
  }
  .set-imt {
    transform: translate(0, 100%);
  }

  .comments-container {
    width: 35%;
    height: auto;
  }
}
</style>
