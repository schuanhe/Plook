<template>
  <view class="container">
    <uni-card title="房间列表">
      <scroll-view class="room-list" scroll-y="true">
        <view class="room-container">
          <uni-card class="room-item" >
            <view> 创建新房间 </view>
            <button size="mini" class="room-button">创建</button>
          </uni-card>
          <view v-if="rooms.length === 0" class="empty-list">
            <text>暂无房间</text>
          </view>
          <uni-card class="room-item" v-for="room in rooms" :key="room.id">
            <view>{{ room.name }}</view>
            <button size="mini" type="primary" class="room-button">{{ room.buttonText }}</button>
          </uni-card>
        </view>
      </scroll-view>
    </uni-card>
  </view>
</template>

<script setup>
import { ref,onMounted } from 'vue';
import UniCard from "../../uni_modules/uni-card/components/uni-card/uni-card.vue";
import { getRoomList } from "../../api/room";
// 示例数据
const rooms = ref([
  { id: 1, name: '房间1', buttonText: '进入' },
  { id: 2, name: '房间2', buttonText: '进入' },
  { id: 1, name: '房间1', buttonText: '进入' },
  { id: 2, name: '房间2', buttonText: '进入' },
  { id: 1, name: '房间1', buttonText: '进入' },
  { id: 2, name: '房间2', buttonText: '进入' },
  { id: 1, name: '房间1', buttonText: '进入' },
  { id: 2, name: '房间2', buttonText: '进入' },
  // { id: 1, name: '房间1', buttonText: '进入' },
  // { id: 2, name: '房间2', buttonText: '进入' },
  // { id: 1, name: '房间1', buttonText: '进入' },
  // { id: 2, name: '房间2', buttonText: '进入' },
  // 可继续添加房间
]);

// 新建房间
function createRoom() {
  uni.navigateTo({
    url: '/pages/createRoom/createRoom'
  })
}
// 加入房间
function joinRoom(room) {
  uni.navigateTo({
    url: '/pages/lookRoom/lookRoom?roomId=' + room.id
  })
}

// 获取房间列表
async function fetchRoomList() {
  getRoomList().then(res => {
    if (res.data === 200){
      rooms.value = res.data.data;
    }else {
      console.log("获取房间列表失败");
      uni.showToast({
        title: res.data.message,
        icon: 'none'
      })
    }
  })
}

// 页面加载时
onMounted(() => {
  fetchRoomList();
})

</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
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
  min-width: 120px;
  max-width: 150px;

  height: 180px;
  min-height: 160px;
  max-height: 180px;
  flex-shrink: 0;
}

.empty-list {
  width: 100%;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
