<template>
    <div>
    <videoPlay
      ref="video"
      title="幻鹤开源"
      :src=this.getVideoInfoSrc.url
      :type=this.getVideoInfoSrc.type
      v-bind="options"
      @play="onPlay"
      @pause="onPause"
      @seeked="onPlayerSeeked"
      @loadstart="loadstart"
    />

    <p id="rommInfoP" style="margin: 5px;">【{{ Allinfo.room.roomId }}】({{ getUserUserName }})</p>
    

  </div>

</template>

<script >
import "../assets/css/top01-button.css"
import { reactive } from "vue";
import { mapState,mapGetters } from "vuex"
import { videoPlay } from 'vue3-video-play'
import "vue3-video-play/dist/style.css";
import socketMsg from "../utils/socketMsg";
 
export default ({
    name: "huanheVideo",
    components: {
    videoPlay,
  },
  props:{},
  //专门来读取vux的数据
  computed:{
        ...mapState(["MyWebSocket","socketMsgStatus","adaptiveMin","Allinfo"]),
        ...mapGetters(["getSocketMsgInit","getVideoInfoSrc","getUserUserName"])
  },
    data() {
        return {
          options:{},
          scoketThrottle:true,   //进度条节流函数
        }
    },
    mounted() {
      //初始化数据
      //挂载监听事件
      window.addEventListener('resize',this.setWandH)
      // console.log(this.overall.width);
        //引入播放器
       this.options =  reactive({
            width: null, //播放器高度
            height: null, //播放器高度
            src: this.getVideoInfoSrc.src,
            type: this.getVideoInfoSrc.type
            // color: "#409eff", //主题色
            // speed: true, //关闭进度条拖动
            // title: "555", //视频名称
            // src: "https://live-hls-web-aje.getaj.net/AJE/index.m3u8", //视频源
            // src:"https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
            // src:"https://p1-webcast-xgcdn.byteimg.com/origin/tos-alisg-v-0000/ocCq4mA92AheZonwIPbG3eCslHDg4GtueUXRn6?filename=1.mp4&tg_qun=yhzyw",
            // type:"m3u8"
    });
    this.setWandH()
    },
    
watch: {
    getVideoInfoSrc: { 
      handler(nv) {
        //对属性监听
        console.log(nv);
        this.options.src = nv.src
        this.options.type = nv.type
      },
      deep: true,
    },

  },

    methods: {
      // 设置长宽
      setWandH(){
        let screenWidth = document.body.offsetWidth;

        //自适应
        this.$store.state.adaptiveMin = (screenWidth < 700)


        if (this.adaptiveMin) {
        //移动端
        this.options.width = screenWidth  + "px"
        this.options.height = (screenWidth*(9/16)) + "px"          
        } else {
        //pc端
        this.options.width = (screenWidth * 0.62) + "px"
        this.options.height = ((screenWidth * 0.62)*(9/16)) + "px"          
        }
      },

      onPlay(){
        this.MyWebSocket.send(socketMsg.videoFun.play(1))
        console.log("播放");
      },
      onPause(){
        this.MyWebSocket.send(socketMsg.videoFun.play(0))
      },
      onPlayerSeeked(ev){
        // 进度条更新
        if (this.socketMsgStatus) {
          this.MyWebSocket.send(socketMsg.videoFun.reach(ev.target.currentTime))
        }else{
          this.$store.commit("setSocketMsgStatus",true)
        }
        // if (this.scoketThrottle) {
        //   this.scoketThrottle = false
        //   setTimeout(() => {
        //     this.MyWebSocket.send(socketMsg.videoFun.reach(ev.target.currentTime))
        //     this.scoketThrottle = true
        //   }, 100);
        // }
      },
      //浏览器开始请求资源
      loadstart(ev){
        this.$store.commit("initMyVideo",{
          aVideo:this.$refs.video,
          allVideo:ev.target
        })  //初始化数据

        socketMsg.init(this.getSocketMsgInit)                       //初始化sock里面的数据      
      },
      test01(){
        // let vi = document.getElementById("dPlayerVideoMain")
        // console.log(vi.src("https://live-hls-web-aje.getaj.net/AJE/index.m3u8"));
        // this.options.type = "m3u8"
        // console.log(this.options.src = "https://live-hls-web-aje.getaj.net/AJE/index.m3u8");
        console.log(this.options);

      },
      test02(){
        // console.log(doucument.getElementById("dPlayerVideoMain"));
        // console.log(this.$store.getters.getMyVideo.allVideo);
        // console.log(this.$store.getters.getMyVideo.allVideo.currentTime = 2);

        console.log();
        // this.options.width
        
      }

    }
})


</script>
<style>
</style>