<template>
  <div :class="['Message', position]" :data-id="id" :data-type="type">
    <div v-if="hasTime && createdAt" class="Message-meta">
      <Time :date="createdAt" />
    </div>
    <div class="Message-main">
      <!-- 根据位置显示头像和用户名 -->
      <Avatar v-if="isRL && avatar" :src="avatar" :alt="name" :url="user.url" />
      <div class="Message-inner">
        <!-- 根据消息类型渲染内容 -->
        <template v-if="type === 'system'">
          <SystemMessage :content="content.text" :action="content.action" />
        </template>
        <template v-else>
          <div v-if="position === 'left'" class="Message-author">{{ user.userName || '匿名用户' }}</div>
          <div class="Message-content">
            <Bubble :content="content.text" :user="user" />
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import Avatar from '../Avatar/Avatar.vue';
import Time from '../Time/Time.vue';
import SystemMessage from './SystemMessage.vue';
import Bubble from "../Bubble/Bubble.vue";

const props = defineProps({
  _id: [String, Number],
  type: String,
  content: {
    type: Object,
    default: () => ({
      text: '',
      // action: ''
    })
  },
  createdAt: Number,
  user: {
    type: Object,
    default: () => ({})
  },
  position: {
    type: String,
    default: 'left'
  },
  hasTime: {
    type: Boolean,
    default: true
  },
  status: Object,
  // TODO 渲染消息内容的函数
  renderMessageContent: {
    type: Function,
    default: () => null
  }
});

const { type, content, user, _id: id, position, hasTime, createdAt } = props;
const { name, avatar } = user;


const isRL = computed(() => position === 'right' || position === 'left');

const message = {
  ...props,
  user,
  _id: id
};

// if (type === 'system') {
//   // If it's a system message, directly return the SystemMessage component
//   return () => (
//       <SystemMessage :content="content.text" :action="content.action" />
// );
// }
</script>

<style scoped>
.Message {
  position: relative;
  padding: 4px 15px;
}

.Message + .Message {
  margin-top: 8px;
}

.Message.right {
  text-align: right;
}

.Message.right .Message-main {
  flex-direction: row-reverse;
}

.Message-main {
  display: flex;
  align-items: flex-start;
}

.Message-inner {
  flex: 1;
  min-width: 0;
}

.Message-author {
  margin-bottom: 4px;
  padding-left: 12px;
  color: #999;
  font-size: 12px;
}

.Message-content {
  display: inline-block;
  max-width: 80%;
}

.Message-meta {
  text-align: center;
  margin: 8px 0;
  color: #999;
  font-size: 12px;
}

.Message.system {
  text-align: center;
}
</style>