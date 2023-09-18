<template>
    <div class="video" :style="initTheme()">
        <header-bar></header-bar>
        <div class="center-container">
            <div class="center-tabs">
                <n-tabs type="line" size="large" :tabs-padding="20" pane-style="padding: 20px;">
                    <n-tab-pane name="加入房间">
                        <n-card title="进入房间" hoverable>
                            <n-form :rules="rules">
                            <n-form-item label="房间号" path="rid">
                                <n-input-number v-model:value="roomid" :show-button="false" />
                            </n-form-item>
                            <n-form-item label="密码" path="rules.password">
                                <n-input v-model:value="password" placeholder="请输入密码(可为空)" />
                            </n-form-item>
                            <n-button type="primary" @click="gotoRoom(roomid,password)">进入房间</n-button>
                            </n-form>
                        </n-card>
                    </n-tab-pane>
                </n-tabs>
            </div>

        </div>

    </div>
</template>

<script setup lang="ts">
import { getTheme } from "@/theme"
import { useRoute,useRouter } from 'vue-router';
import { NFormItem,NForm, NInput, NButton, NInputNumber, NCard, NTabs,NTabPane, useNotification, type FormRules} from 'naive-ui';
import { onBeforeMount, onBeforeUnmount, ref } from 'vue';
import HeaderBar from '@/components/header-bar/Index.vue';
import type {  } from '@plooks/apis';
import { inRoomApi } from '@plooks/apis';
import {  statusCode } from '@plooks/utils';


const router = useRouter();
const route = useRoute();
const theme = getTheme();
const roomid = parseInt(route.params.roomId.toString());
const password = ref("");

const notification = useNotification();


const initTheme = () => {
    return {
        "--primary-color": theme.primaryColor,
        "--hover-color": theme.primaryHoverColor
    }
}


//校验规则
const rules: FormRules = {
    rid: [
        { required: true, message: '请输入房间号' },
        { type: 'number', message: '房间号必须为数字' }
    ],
    password: [
        { required: false, message: '请输入密码' },
    ]
}


// 进入房间
const gotoRoom = (rid: number,password:String|null) =>{
    inRoomApi(rid,password).then((res) => {
        if(res.data.code === statusCode.OK) {
            // 进入成功
            //TODO 跳转一起看页面
            router.push({
                name: 'WatchRoom',
                params: {
                    roomId: rid.toString()
                }
            })
        }else{
            // 其他错误
            notification.error({
                title: res.data.message,
                duration: 3000,
            });
        }
    })
}

onBeforeMount(() => {
    // 进入房间
    gotoRoom(roomid,null)
})

onBeforeUnmount(() => {

})
</script>

<style lang="less" scoped>
.center-container {
    // 距离顶部的距离
    margin-top: 10%;
    // 左右居中
    margin-left: auto;
    // 宽度
    width: 50%;
    margin: 0 auto;
}

.center-tabs {
  /* 使内容水平居中 */
  display: flex;
  flex-direction: column; /* 垂直排列子元素 */
  align-items: center;
  // 居中
margin-left: auto;
    // 宽度
    
}


.video-main {
    display: flex;
  flex-direction: column; /* 垂直排列子元素 */
  align-items: center;    
}

</style>