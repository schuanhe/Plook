<template>
  <div class="messageContainer">
    <uni-list class="messageList">
      <Message v-for="msg in msgList" :key="msg._id" v-bind="msg"/>
    </uni-list>
    <view class="messageInputView">
      <uni-easyinput
        class="messageInput"
        v-model="inputValue"
        placeholder="发送消息或弹幕"
        @confirm="sendMessage"
      ></uni-easyinput>
      <view class="button-group">
        <button size="mini" @click="sendMessage">发送</button>
        <button size="mini" type="primary" @click="sendAsDanmu">弹幕</button>
      </view>
    </view>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue';
import Message from '../Message/Message.vue';
import UniList from "../../../uni_modules/uni-list/components/uni-list/uni-list.vue";
import UniEasyinput from "../../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue";
import { useRoomStore } from "../../../store/room";
import { useUserStore } from "../../../store/user";
import { socketIo, socketMessage } from "../../../utlis/socketIo";

var userStore = useUserStore().getUserInfo()

const props = defineProps({
  videoRef: {
    type: Object,
    required: true,
    default: null,
    validator: (value) => {
      return value && typeof value.sendDanmuToVideo === 'function';
    }
  }
});

const roomStore = useRoomStore();
roomStore.setUserInfo(userStore.id, userStore.username)
const msgList = computed(() => roomStore.roomMsgList);
const inputValue = ref('');

// 获取当前房间ID
const getCurrentRoomId = () => {
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  return currentPage.$page?.options?.roomId;
};

// 生成随机颜色
const getRandomColor = () => {
  const rgb = [];
  for (let i = 0; i < 3; ++i) {
    let color = Math.floor(Math.random() * 256).toString(16);
    color = color.length === 1 ? '0' + color : color;
    rgb.push(color);
  }
  return '#' + rgb.join('');
};

// 生成消息ID
const generateMessageId = () => {
  return `msg_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
};

// 添加消息到列表
const addMessage = (message) => {
  console.log(message)
  const messageObj = {
    _id: generateMessageId(),
    type: message.type || 'text',
    content: {
      text: typeof message.content === 'string' ? message.content : message.content.text
    },
    user: message.user || {},
    position: message.position || 'left',
    createdAt: Date.now()
  };

  try {
    roomStore.addMessage(messageObj);
  } catch (error) {
    console.error('添加消息失败:', error, messageObj);
  }
};

// 添加系统消息
const addSystemMessage = (content) => {
  addMessage({
    type: 'system',
    content,
    position: 'center'
  });
};

// 发送弹幕
const sendAsDanmu = () => {
  if (!inputValue.value || !props.videoRef) return;

  const color = getRandomColor();
  const text = inputValue.value;

  try {
    // 发送弹幕到本地视频
    props.videoRef.sendDanmuToVideo(text, color);

    // 发送弹幕同步消息
    socketIo.send(socketMessage.sendRoomInfo({
      type: 'setRoomInfo',
      data: {
        roomId: getCurrentRoomId(),
        messageType: 'danmu',
        danmu: {
          text,
          color
        }
      }
    }));

    inputValue.value = '';
  } catch (error) {
    console.error('发送弹幕失败:', error);
  }
};

// 发送普通消息
const sendMessage = () => {
  console.log("useUserStore",useUserStore().getUserInfo())
  if (!inputValue.value) return;

  const messageData = {
    type: 'text',
    content: {
      text: inputValue.value
    },
    user: {
      userName: roomStore.getUserInfo().userName
    }
  };

  // 发送消息
  socketIo.send(socketMessage.sendRoomInfo({
    type: 'setRoomInfo',
    data: {
      roomId: getCurrentRoomId(),
      messageType: 'message',
      message: messageData
    }
  }));

  // 不再在这里添加本地消息，等待服务器广播后统一处理
  inputValue.value = '';
};

// 监听消息
const listenMessages = () => {
  socketIo.getSocket().on('setRoomInfo', (message) => {
    const { messageType, message: msgData, userId, action } = message.data;

    switch (messageType) {
      case 'message':
        // 判断消息位置：自己发送的在右边，收到的在左边
        const position = msgData.user.userId === userStore.id ? 'right' : 'left';
        addMessage({
          ...msgData,
          position
        });
        break;
      case 'system':
        addSystemMessage(message.data.content);
        break;
      case 'videoAction':
        const actionTexts = {
          play: '播放视频',
          pause: '暂停视频',
          seek: '调整进度到',
          changeSource: '更改视频源为'
        };
        addSystemMessage(`用户${userId}${actionTexts[action]}${action === 'seek' ? message.data.currentTime + '秒' : ''}`);
        break;
    }
  });
};

onMounted(() => {
  listenMessages();
});

onBeforeUnmount(() => {
  socketIo.getSocket()?.off('setRoomInfo');
  roomStore.clearMessages();
});
</script>

<style scoped>
.messageContainer {
  height: 100%;
  width: 100%;
  background: white;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.messageList {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 10px 0;
}

.messageInputView {
  display: flex;
  align-items: center;
  padding: 10px;
  gap: 10px;
  background: #f5f5f5;
  border-top: 1px solid #eee;
}

.messageInput {
  flex: 1;
}

.button-group {
  display: flex;
  gap: 10px;
}

.button-group button {
  min-width: 60px;
}
</style>
