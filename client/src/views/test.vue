<template>
    <div id="div-main" class="div-main">
        <div class="container">
            <div id="div-video">
                <huanhe-video ref="huanheVideo" />
            </div>
            <div class="chat-wrapper">
                <el-scrollbar ref="scrollbarRef" height="300px">
                    <chat v-for="item in getRoomChatInfoMassgs" :key="item" :chats="item" :user-name="getUserUserName"
                        @ScrollbarRef="onScrollbarRef" />
                </el-scrollbar>
                <div class="send-msg">
                    <el-input v-model="myMassg" @change="setMyassg" placeholder="发送消息" />
                    <el-button @click="setMyassg">发送</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import HuanheVideo from '../components/Huanhe-video.vue';
import chat from "../components/chat.vue"
export default {
    components: {
    HuanheVideo,
    chat
  },
}
</script>

<style>

/* cs */
@media (min-width: 700px) {
  
  .container {
    display: grid;
    grid-template-columns: 18fr 6fr;
    gap: 10px;
  }
  
  #div-video {
    grid-column: 1;
  }
  
  .chat-wrapper {
    display: flex;
    flex-direction: column;
    max-height: 640px;
  }
  
  .send-msg {
    display: flex;
    align-items: center;
  }
  
  .send-msg el-input {
    margin-right: 10px;
  }
  
  .send-msg el-button {
    min-width: 60px;
  }
}

@media (min-width: 700px) {
  .el-scrollbar__wrap {
    max-height: none !important;
    overflow: hidden;
  }
}


</style>