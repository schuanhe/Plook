<template>
    <el-form-item label="用户名">
    <el-input placeholder="请输入用户名" v-model="userInfo.userName" />
  </el-form-item>
  <el-button type="primary" @click="login">登 录</el-button>


  <el-form-item label="选择房间">
    <el-select v-model="userInfo.roomId" class="m-2" placeholder="选则一个存在的房间">
      <el-option v-for="item in socketInfo.roomList" :key="item" :label="item" :value="item" />
    </el-select>
  </el-form-item>
  <el-button @click="loginRoom" type="primary">进入这个房间</el-button>


</template>

<script>
export default {
  name:"login",
  

}
</script>

<style>

</style>