<template>
  <div ref="artRef"></div>
</template>

<script>
import Artplayer from "artplayer";
import Hls from "hls.js";
import artplayerPluginDanmuku from "artplayer-plugin-danmuku";

export default {
data() {
  return {
    instance: null,
  };
},
props: {
  option: {
    type: Object,
    required: true,
  },
},
mounted() {
  function playM3u8(video, url, art) {
    if (Hls.isSupported()) {
        if (art.hls) art.hls.destroy();
        const hls = new Hls();
        hls.loadSource(url);
        hls.attachMedia(video);
        art.hls = hls;
        art.on('destroy', () => hls.destroy());
    } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = url;
    } else {
        art.notice.show = 'Unsupported playback format: m3u8';
    }
}


  this.instance = new Artplayer({
    ...this.option,
    setting: true, //设置标签
    aspectRatio: true,  //设置长宽比
    loop: true, //循环播放
    autoMini: true, //当播放器滚动到浏览器视口以外时，自动进入 迷你播放 模式
    pip: true, //是否在底部控制栏里显示 画中画 的开关按钮
    fullscreen: true, //是否在底部控制栏里显示播放器 窗口全屏 按钮
    playbackRate: true,
    fullscreenWeb: true, //是否在底部控制栏里显示播放器 网页全屏 按钮
    miniProgressBar: true, //迷你进度条，只在播放器失去焦点后且正在播放时出现
    lock: true, //是否在移动端显示一个 锁定按钮 ，用于隐藏底部 控制栏
    playsInline: true,
    autoHeight: true, //当容器只有宽度，该属性可以自动计算出并设置视频的高度
    autoSize: true, //设置视频是否自适应尺寸
    mini: true, //设置和获取播放器迷你模式
    container: this.$refs.artRef,
    customType: {
        m3u8: playM3u8,
    },
    plugins: [
          artplayerPluginDanmuku({
            // 弹幕数组
          }),
        ],
  });

  this.$nextTick(() => {
    this.$emit("get-instance", this.instance);
  });
},
beforeDestroy() {
  if (this.instance && this.instance.destroy) {
    this.instance.destroy(false);
  }
},
};
</script>

<style>


</style>