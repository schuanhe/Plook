<template>
    <div class="common-layout-header">
            <img height="50"  src="../assets/favicon.svg"/>
            <p>Plook 一起看！</p>

    <el-dropdown>
      <el-button type="primary">菜单</el-button>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="this.$store.state.setRoom = true">设置房间视频</el-dropdown-item>
          <el-dropdown-item @click="outRoom">退出房间</el-dropdown-item>
          <!-- <el-dropdown-item>Action 3</el-dropdown-item>
          <el-dropdown-item>Action 4</el-dropdown-item>
          <el-dropdown-item>Action 5</el-dropdown-item> -->
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    </div>

  </template>

<script>
import { ElMessage } from "element-plus";
export default {
  methods:{
    outRoom() {
      ElMessage.success("下个版本再弄吧，手动返回就行,加入交流群一起开发:486601640")
    },
  }
}
</script>
  <style>

.common-layout-header {
  padding: 5px;
  display: flex;
  align-items: center;
  /* justify-content: flex-start; aligns items to the left */
}

.common-layout-header p {
  color: rgb(150,80,250);
  font-weight: bold;
  margin: 0; /* removes default margin */
}

.common-layout-header img {
  margin-right: 30px;
  margin-left: 0; /* removes default margin */
}

.common-layout-header .el-dropdown {
  position:absolute;
  right:0
}

.title{
    position: relative;
    text-align: center;
    overflow: hidden;
    font-size: 14px;
    color: #999;
}
.title::before,.title::after{
    content: '';
    display: inline-block;
    width: 100%;
    height: 1px;
    position: absolute;
    background: #ccc;
    top: 50%;
}
.title::before{
    margin-left: -10px;
    transform: translateX(-100%);
}
.title::after{
    margin-left: 10px;
}


</style>