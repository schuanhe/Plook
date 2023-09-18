<template>
    <div class="msg">
        <!--右侧-->
        <div class="msg-right">
            <div ref="msgBox" class="msg-main">
                <div class="content-container" v-for="(item, index) in props.msgDetails" :key="index">
                    <!--自己发送的-->
                    <div v-if="item.uid == userInfo.uid">
                        <!-- 一个靠左的标签 -->
                        <common-avatar class="avatar-right" :url="userInfo.avatar" :size="45"></common-avatar>
                        <!-- <span style="float: right;">{{ item.nickname }}</span> -->
                        <span class="content-right">{{ item.content }}</span>
                    </div>
                    <!--收到的-->
                    <div v-else>
                        
                        <common-avatar class="avatar-left" :url="item.avatar" :size="45"></common-avatar>
                        <!-- <span style="float: left;">{{ item.nickname }}</span> -->
                        <div class="content-left-box">
                            <span class="content-left">{{ item.content }}</span>
                        </div>
                    </div>
                    <!-- 解决div无法撑开的问题 -->
                    <div style="clear:both;"></div>
                </div>
            </div>
            <div class="msg-input">
                <n-input v-model:value="message" type="text" placeholder="发个消息呗~" maxlength="255" show-count @keydown.enter="sendMsg"></n-input>
                <div class="btn-box">
                    <div></div>
                    <n-button type="primary" :loading="sendLoading" @click="sendMsg">发送</n-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { NInput, NButton, useNotification } from 'naive-ui';
import { onBeforeMount, onBeforeUnmount, reactive, ref, nextTick, watch } from 'vue';
import { CommonAvatar } from '@plooks/components';

import { storageData } from '@plooks/utils';
import type { UserInfoType,roomChatType } from "@plooks/apis";


const props = defineProps<{
    msgDetails: Array<roomChatType>, // 消息详情
    refNub:number
}>()

const readyAck = ref(props.refNub)

const updateFlag = ref(0);//响应式数据，用于更新页面

const allowToBottom = ref(true);//是否允许自动跳转到底部

const notification = useNotification();//通知
const emits = defineEmits(['webSendMesg']);

const sendLoading = ref(false);//发送消息loading

const msgForm = reactive<roomChatType>({
    uid: 0, // 用户id
    avatar: "", // 用户头像
    nickname: "", // 用户昵称
    content: '',
    createdAt: ''
})

// 发送的消息
const message = ref("");

// 用户信息
const userInfo = ref<UserInfoType>({
    uid: 0,
    nickname: "",
    avatar: "",
    spaceCover: ""
});

const msgBox = ref<HTMLElement | null>(null);

//到达底部
const toBottom = () => {
    if (allowToBottom.value) {
        console.log("到达底部");
        
        msgBox.value!.scrollTop = msgBox.value!.scrollHeight;
    } else {
        msgBox.value!.scrollTop = 100;
        allowToBottom.value = true;
    }
}

// 发送消息
const sendMsg = async () => {
    if(message.value == ""){
        notification.error({
            title: '发送失败',
        });
        return;
    }
    msgForm.createdAt = new Date().toLocaleString();
    msgForm.content = message.value
    const newMsg:roomChatType = {
        uid: msgForm.uid,
        avatar: msgForm.avatar,
        nickname: msgForm.nickname,
        content: msgForm.content,
        createdAt: msgForm.createdAt
    }
    emits("webSendMesg",newMsg)
    
    message.value = "";
    nextTick(() => {
        toBottom();
    })
}


// 监听
// 
watch(readyAck, () => {
    updateFlag.value++;
    nextTick(() => {
        toBottom();
    })
})

onBeforeMount(async () => {
    userInfo.value = storageData.get('user_info');
    msgForm.uid = userInfo.value.uid;
    msgForm.avatar = userInfo.value.avatar;
    msgForm.nickname = userInfo.value.nickname;
    // TODO 加载历史消息
})

onBeforeUnmount(() => {

})
</script>

<style lang="less" scoped>
.msg {
    display: flex;
    height: 660px;
}

.msg-left {
    width: 230px;
    border-right: 1px solid #e1e2e3;

    .left-top {
        height: 40px;
        border-bottom: 1px solid #e1e2e3;
    }

    .msg-user-item {
        width: 100%;
        height: 60px;
        background-color: #fff;
        position: relative;
        border-bottom: 1px solid #e0e0e0;

        &:hover {
            background-color: #f7f7f7;
        }

        .msg-avatar {
            margin: 8px 0 0 6px;
        }

        .dot {
            top: 10px;
            left: 42px;
            width: 10px;
            height: 10px;
            position: absolute;
            border-radius: 50%;
            background-color: #f5222d;
        }

        .msg-name {
            position: absolute;
            top: 8px;
            left: 60px;
            color: #333;
            font-size: 14px;
        }

        .msg-date {
            position: absolute;
            top: 32px;
            left: 60px;
            color: #999;
            font-size: 12px;
        }
    }
}

.msg-right {
    width: calc(100%);

    .right-top {
        color: #333;
        text-align: center;
        font-size: 16px;
        line-height: 40px;
        border-bottom: 1px solid #e1e2e3;
    }

    .msg-main {
        height: 450px;
        background-color: #f7f7f7;
        border-bottom: 1px solid #e1e2e3;
        overflow-y: auto;

        /**修改滚动条样式 */
        &::-webkit-scrollbar {
            width: 6px;
        }

        &::-webkit-scrollbar-thumb {
            background-color: #d7d7d8;
            outline: none;
            border-radius: 2px;
        }

        &::-webkit-scrollbar-track {
            box-shadow: none;
            border-radius: 2px;
        }
    }
}

/**聊天部分 */
.content-container {
    min-height: 70px;
    margin: 6px 20px;

    &:nth-child(1) {
        margin-top: 10px;
    }

    .avatar-right {
        float: right;
    }

    .content-right {
        float: right;
        max-width: 80%;
        margin-right: 10px;
        margin-top: 6px;
        background-color: #40a9ff;
        color: #fff;
        font-size: 16px;
        border-radius: 3px;
        padding: 5px 10px 5px 10px;
    }

    .avatar-left {
        float: left;
    }

    .content-left-box {
        float: left;
        margin-left: 10px;
        margin-top: 10px;
        max-width: 80%;
        background: #fff;
        padding: 5px 10px 5px 10px;
        border-radius: 3px;
    }

    .content-left {
        font-size: 1rem;
    }
}

.msg-input {
    padding: 10px;
    position: relative;

    .btn-box {
        height: 50px;
        display: flex;
        margin-left: 10px;
        align-items: center;
        justify-content: space-between;

        button {
            width: 100px;
            height: 30px;
        }
    }
}
</style>