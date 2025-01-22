<template>
  <view class="container">
    <uni-card title="房间选择">
      <view class="room-select">
        <view>
          <uni-data-checklist @change="filterRooms" v-model="checkbox1" :localdata="selectConditions">  </uni-data-checklist>
        </view>
      </view>
    </uni-card>
    <uni-card title="房间列表">
      <scroll-view class="room-list" scroll-y="true">
        <view class="room-container">
          <uni-card class="room-item">
            <view class="room-item-view" >
              <text class="add-room-item"> 创建房间 </text>
              <button size="mini" @click="createRoom"  type="default" class="room-button"> 创建 </button>
            </view>
          </uni-card>
          <view v-if="filteredRooms.length === 0" class="empty-list">
            <text>暂无房间</text>
          </view>
          <uni-card class="room-item" v-for="room in filteredRooms" :key="room.id">
            <view class="room-item-view" >
              <text style="font-weight: bold; height: 80px; ">{{ room.roomName }}</text>
              <view class="room-tag">
                <uni-tag size="mini" :text="room.isPassword ? '有密码' : '无密码'" :type="room.isPassword ? 'primary' : 'default'"/>
                <uni-tag size="mini" :text="room.isInRoom ? '已加入' : '未加入'" :type="room.isInRoom ? 'success' : 'default'"/>
              </view>
              <button size="mini" type="primary" class="room-button" @click="joinRoomHandler(room)"> 加入 </button>
            </view>
          </uni-card>
        </view>
      </scroll-view>
    </uni-card>
    <uni-popup ref="passwordPopup" type="dialog">
      <view class="password-popup">
        <text>请输入房间密码</text>
        <uni-easyinput v-model="password" placeholder="请输入密码" />
        <button size="mini" type="primary" @click="confirmPassword">确认</button>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref } from 'vue';
import UniCard from "../../uni_modules/uni-card/components/uni-card/uni-card.vue";
import { getRoomList, joinRoom } from "../../api/room";
import UniDataChecklist from "../../uni_modules/uni-data-checkbox/components/uni-data-checkbox/uni-data-checkbox.vue";
import UniEasyinput from "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue";
import UniFab from "../../uni_modules/uni-fab/components/uni-fab/uni-fab.vue";
import UniTag from "../../uni_modules/uni-tag/components/uni-tag/uni-tag.vue";
import UniPopup from "../../uni_modules/uni-popup/components/uni-popup/uni-popup.vue";
import {onShow} from "@dcloudio/uni-app";
// 示例数据
const rooms = ref([
  {
    "isPassword": false,
    "id": 1,
    "userId": 1,
    "roomName": "不带密码公开加入",
    "roomVideoUrl": "https://baidu.com",
    "isVisible": true,
    "password": null,
    "createdAt": "2024-06-12T09:43:10.000Z",
    "updatedAt": "2024-06-12T09:43:10.000Z",
    "isInRoom": 1
  },
  {
    "isPassword": true,
    "id": 2,
    "userId": 1,
    "roomName": "带密码隐藏加入",
    "roomVideoUrl": "https://baidu.com",
    "isVisible": false,
    "password": "212",
    "createdAt": "2024-06-12T09:49:43.000Z",
    "updatedAt": "2024-06-12T09:49:43.000Z",
    "isInRoom": 1
  },
  {
    "isPassword": false,
    "id": 4,
    "userId": 1,
    "roomName": "不带密码隐藏加入",
    "roomVideoUrl": "https://baidu.com",
    "isVisible": false,
    "password": null,
    "createdAt": "2024-06-13T01:36:54.000Z",
    "updatedAt": "2024-06-13T01:36:54.000Z",
    "isInRoom": 1
  },
  {
    "isPassword": true,
    "id": 8,
    "userId": 2,
    "roomName": "带密码公开",
    "roomVideoUrl": "https://baidu.com",
    "isVisible": true,
    "createdAt": "2024-09-05T15:00:16.000Z",
    "updatedAt": "2024-09-05T15:00:18.000Z",
    "isInRoom": 0
  },
  {
    "isPassword": true,
    "id": 9,
    "userId": 2,
    "roomName": "不带密码公开",
    "roomVideoUrl": "https://baidu.com",
    "isVisible": true,
    "createdAt": "2024-09-05T15:00:48.000Z",
    "updatedAt": "2024-09-05T15:00:51.000Z",
    "isInRoom": 0
  }
]);
// rooms实际显示数据
const filteredRooms = ref([]);

// 选择条件
const selectConditions = ref([
  {
    text: '全部',
    value: 'all'
  },
  {
    text: '公开',
    value: 'public'
  },
  {
    text: '已加入',
    value: 'join'
  },
]);

const checkbox1 = ref([])

// 选择后过滤房间列表
function filterRooms(condition) {
  if (condition === 'all') {
    return rooms.value;
  } else if (condition === 'public') {
    return rooms.value.filter(room => room.isPublic);
  } else if (condition === 'join') {
    return rooms.value.filter(room => room.isJoined);
  } else if (condition === 'unJoined') {
    return rooms.value.filter(room => !room.isJoined);
  }
}

// 新建房间
function createRoom() {
  uni.navigateTo({
    url: '/pages/roomList/addRoom'
  })
}

const password = ref('');
const selectedRoom = ref(null);
const passwordPopup = ref(false);

// 加入房间
async function joinRoomHandler(room) {
  if (room.isPassword) {
    selectedRoom.value = room;
    password.value = '';
    passwordPopup.value.open();
  } else {
    const res = await joinRoom(room.id);
    if (res.code === 200) {
      uni.navigateTo({
        url: '/pages/PlookRoom/PlookRoom?roomId=' + room.id
      });
    } else {
      uni.showToast({
        title: res.message,
        icon: 'none'
      });
    }
  }
}

async function confirmPassword() {
  if (selectedRoom.value && password.value === selectedRoom.value.password) {
    const res = await joinRoom(selectedRoom.value.id);
    if (res.code === 200) {
      uni.navigateTo({
        url: '/pages/PlookRoom/PlookRoom?roomId=' + selectedRoom.value.id
      });
    } else {
      await uni.showToast({
        title: res.message,
        icon: 'none'
      });
    }
    passwordPopup.value.close();
  } else {
    uni.showToast({
      title: '密码错误',
      icon: 'none'
    });
  }
}

// 获取房间列表
async function fetchRoomList() {
  getRoomList().then(res => {
    if (res.code === 200) {
      rooms.value = res.data;
      filteredRooms.value = rooms.value;
    } else {
      console.log("获取房间列表失败", res.code);
      uni.showToast({
        title: res.message,
        icon: 'none'
      })
    }
  })
}

// // 页面加载时
// onMounted(() => {
//   fetchRoomList();
// })

// 每次回到该页面时
onShow(() => {
  fetchRoomList();
})

</script>

<style scoped>
.container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.room-select {
  display: flex;
  flex-wrap: wrap;
}

.room-list {
  width: 100%;
  max-height: 500px;
  overflow-y: auto;
}

.room-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}

.room-item {
  margin: 5px;
  box-sizing: border-box;
  width: 150px;
  min-width: 130px;
  max-width: 150px;

  height: 180px;
  min-height: 160px;
  max-height: 180px;
  flex-shrink: 0;
}

.room-item-view {
  display: flex;
  flex-direction: column;
  /* 均匀y轴排列 */
  justify-content: space-between;
  align-items: stretch;
}

.empty-list {
  width: 100%;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.room-button {
  margin: 4px;
}

.room-tag {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.add-room-item {
  display: grid;
  place-items: center;
  font-weight: bold;
  height: 100px; /* 容器的高度 */
}

.password-popup {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}
</style>
