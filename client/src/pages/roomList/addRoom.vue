<template>
  <view class="container">
    <huanhe-card title="新增房间">
      <uni-forms ref="validForm" :rules="rules" :modelValue="form">
        <uni-forms-item label="房间名称">
          <uni-easyinput v-model="form.username" placeholder="请输入用户名" ></uni-easyinput>
        </uni-forms-item>
        <uni-forms-item label="密码">
          <uni-easyinput v-model="form.password" placeholder="请输入密码"></uni-easyinput>
        </uni-forms-item>
      </uni-forms>
      <button size="mini" @click="loginFun" type="primary">登录</button>
    </huanhe-card>
  </view>
</template>

<script setup>
import UniEasyinput from "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.vue";
import UniFormsItem from "../../uni_modules/uni-forms/components/uni-forms-item/uni-forms-item.vue";
import UniForms from "../../uni_modules/uni-forms/components/uni-forms/uni-forms.vue";
import HuanheCard from "../../components/SchuanheComponent/HuanheCard.vue";


const addRoom = ref({
  username: "",
  password: "",
});

</script>

<style scoped>

</style>