<template>
  <view class="container">
    <huanhe-card title="新增房间">
      <uni-forms ref="validForm" :rules="rules" :modelValue="addRoom">
        <uni-forms-item label="房间名称">
          <uni-easyinput v-model="addRoom.roomName" placeholder="请输入用户名" ></uni-easyinput>
        </uni-forms-item>
        <uni-forms-item label="房间密码(留空不需要)">
          <uni-easyinput v-model="addRoom.password" placeholder="请输入密码"></uni-easyinput>
        </uni-forms-item>
        <uni-forms-item label="房间视频">
          <uni-easyinput v-model="addRoom.roomVideoUrl" placeholder="请输入视频地址"></uni-easyinput>
        </uni-forms-item>
        <uni-forms-item label="是否公开">
          <switch checked="" @change="checkIsValid"></switch>
        </uni-forms-item>
      </uni-forms>
      <button size="mini" @click="addRoomFun" type="primary">新增</button>
    </huanhe-card>
  </view>
</template>

<script setup>
import UniEasyinput from "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue";
import UniFormsItem from "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue";
import UniForms from "../../uni_modules/uni-forms/components/uni-forms/uni-forms.vue";
import HuanheCard from "../../components/SchuanheComponent/HuanheCard.vue";
import { createRoom } from "../../api/room";

const addRoom = {
  "roomName":"测试房间",
  "roomVideoUrl":"https://baidu.com",
  "isVisible": true,
  "password": ""
}
const rules = {
  roomName: [
    {
      required: true,
      message: "请输入房间名称",
      trigger: ["change", "blur"],
    },
  ],
  roomVideoUrl: [
    {
      required: true,
      message: "请输入房间视频地址",
      trigger: ["change", "blur"],
    },
  ],
};

function checkIsValid(e){
  addRoom.isVisible = e.detail.value
}

function addRoomFun(){
  createRoom(addRoom).then(res => {
    if (res.code === 200) {
      uni.showToast({
        title: res.message,
        icon: "none",
        duration: 2000,
      });
      uni.navigateBack()
    }
  })
}

</script>

<style scoped>

</style>