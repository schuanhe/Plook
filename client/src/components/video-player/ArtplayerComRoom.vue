<template>
  <div ref="artRef"></div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, nextTick, watch, reactive } from 'vue';
import Artplayer from 'artplayer';
import artplayerPluginDanmuku from 'artplayer-plugin-danmuku'
import Hls from 'hls.js';


export default {
  data() {
    return {
      instance: null,
    };
  },
  components: {
    ArtplayerComRoom,
  },
  props: {
    vid: {
      type: Number,
      required: true,
    },
    theme: {
      type: String,
      required: true,
    },
    resources: {
      type: Array,
      required: true,
    },
    part: {
      type: Number,
      required: true,
    },
    isAdmin: {
      type: Boolean,
      required: true,
    },
  },
  methods: {
    // 初始话播放器
    initPlayer() {
      this.instance = new Artplayer({
        url: this.resources[this.part - 1].url,
        container: this.$refs.artRef,
        // 直播模式
        isLive: !this.isAdmin,
        playbackRate: false, // 播放速度
        autoPlayback: true, // 视频回放功能
        pip: true, // 画中画
        fullscreen: true, // 全屏
        fullscreenWeb: true, // 全屏Web
        miniProgressBar: true, // 进度条
        lock: true, // 锁定
        setting: true, // 设置
        playsInline: true, // 内联播放
        //autoHeight: false, // 自动高度
        autoSize: false, // 自动尺寸
        //mini: true, // 迷你模式
        theme: '#409EFF', // 主题色
        customType: {
          m3u8: playM3u8, // 自定义播放类型
        },
        plugins: [
          artplayerPluginDanmuku({  // 弹幕
            // 弹幕数组
            danmuku:  [], // 弹幕数组，弹幕用来聊天使用
            speed: 5, // 弹幕持续时间，单位秒，范围在[1 ~ 10]
            opacity: 1, // 弹幕透明度，范围在[0 ~ 1]
            fontSize: 25, // 字体大小，支持数字和百分比
            color: '#FFFFFF', // 默认字体颜色
            mode: 0, // 默认模式，0-滚动，1-静止
            margin: [10, '25%'], // 弹幕上下边距，支持数字和百分比
            antiOverlap: true, // 是否防重叠
            useWorker: true, // 是否使用 web worker
            synchronousPlayback: false, // 是否同步到播放速度
            filter: (danmu) => danmu.text.length < 50, // 弹幕过滤函数，返回 true 则可以发送
            lockTime: 5, // 输入框锁定时间，单位秒，范围在[1 ~ 60]
            maxLength: 100, // 输入框最大可输入的字数，范围在[0 ~ 500]
            minWidth: 200, // 输入框最小宽度，范围在[0 ~ 500]，填 0 则为无限制
            maxWidth: 400, // 输入框最大宽度，范围在[0 ~ Infinity]，填 0 则为 100% 宽度
            theme: 'light', // 输入框自定义挂载时的主题色，默认为 dark，可以选填亮色 light
            beforeEmit: (danmu) => !!danmu.text.trim(), // 发送弹幕前的自定义校验，返回 true 则可以发送
            // 通过 mount 选项可以自定义输入框挂载的位置，默认挂载于播放器底部，仅在当宽度小于最小值时生效
            // mount: document.querySelector('.artplayer-danmuku'),
          }),
        ],
      });
    },
    // m3u8 播放器
    playM3u8(video, url, art) {
      if (Hls.isSupported()) {
        const hls = new Hls();
        hls.loadSource(url);
        hls.attachMedia(video);
        // 销毁 Artplayer 实例
        art.on('destroy', () => hls.destroy());
        hls.on(Hls.Events.MANIFEST_PARSED, () => {
          video.play();
        });
      } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = url;
        video.addEventListener('loadedmetadata', () => {
          video.play();
        });
      }
    },
    // 绑定播放器的各种事件
    bindArtplayerEvents() {
      if (this.instance) {
        this.instance.on('ready', () => {
          this.$emit("ready");
        });
        this.instance.on('play', () => {
          this.$emit("play");
        });
        this.instance.on('pause', () => {
          this.$emit("pause");
        });
        this.instance.on('seek', () => {
          this.$emit("seek", this.instance?.currentTime);
        });
        this.instance.on('restart', () => {
          this.$emit("switchPid", this.part)
        });
        // 视频播放完毕自动播放下一集
        this.instance.on('ended', () => {
          this.$emit("switchPid", this.part + 1)
        });
        // 弹幕事件绑定
        this.instance.on('artplayerPluginDanmuku:emit', (danmu) => {
          //判断danmu.text是否存在
          const danmuku = danmu
          const addDanmu = {
            ...this,
            ...danmuku
          }
          this.sendDanmu(addDanmu)
        });
      }
    },
    // 发送弹幕
    async sendDanmu(danmu) {
      const res = await sendDanmukuAPI(danmu);
      if (res.data.code === statusCode.OK) {
        // 发送弹幕成功
      }
    },
    // 销毁 Artplayer 实例
    destroyArtplayerInstance() {
      if (this.instance && this.instance.destroy) {
        this.instance.destroy(false);
      }
    },
    // 切换实例
    changeArtplayerInstance() {
      this.destroyArtplayerInstance();
      this.initPlayer();
      this.bindArtplayerEvents();
      nextTick(() => {
        this.$emit("get-instance", this.instance);
      });
    },
    // 切换pid只用切换视频源
    switchPid(pid) {
      if (this.instance) {
        // 设置更新的url
        this.instance.switchUrl(this.resources[pid - 1].url);
      }
    },
  },
  watch: {
    vid(val) {
      if (this.instance) {
        // 设置更新的url
        this.changeArtplayerInstance();
      }
    },
    part(val) {
      if (this.instance) {
        // 设置更新的url
        this.switchPid(val)
      }
    }
  },
  mounted() {
    if (this.vid === 0) {
      console.log("vid为0,不创建播放器");
    } else {
      this.initPlayer()
    }
  },
  beforeUnmount() {
    this.destroyArtplayerInstance();
  },
};


</script>
