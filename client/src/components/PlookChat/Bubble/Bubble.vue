<template>
  <div class="Bubble">
    {{ content }}
  </div>
</template>

<script setup>
defineProps({
  content: {
    type: String,
    required: true
  },
  user: {
    type: Object,
    default: () => ({})
  }
});
</script>

<style scoped>
.Bubble {
  display: inline-block;
  padding: 8px 12px;
  border-radius: 4px;
  background: #f5f5f5;
  word-break: break-word;
  font-size: 14px;
  line-height: 1.4;
}

:deep(.Message.right) .Bubble {
  background: #e1f3fb;
  color: #333;
}

:deep(.Message.left) .Bubble {
  background: #f5f5f5;
  color: #333;
}
</style>
