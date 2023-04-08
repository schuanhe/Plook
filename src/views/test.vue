<template>
<p>1</p>

<router-view></router-view>

<p>vuews读取路由{{ csvuex }}</p>
<button @click="test02">更改参数cs值魏211</button>
<button @click="test03">更改vuex参数211</button>
<button @click="test04">发送消息</button>
<button @click="test05">进入房间</button>
<button @click="test06">vuex对视频控制</button>
<button @click="test07">通过id拿属性</button>

</template>

<script >
import socket from '../utils/socketFun';
import testZj from '../components/testZj.vue';
import { mapState } from "vuex"
import HuanheVideo from '../components/Huanhe-video.vue';
export default {
    name:"test",
    components: {
        testZj,
        HuanheVideo,
    },
    data(){
        return{
            cs:133
        }
    },
    //专门来读取vux的数据
    computed:{
        ...mapState(["csvuex"])
    },
    mounted(){
        socket.initWebSocket("666")
        this.$store.commit("initMyWebSocket",socket.getWebsock())
    },
    methods:{
        test01(){
            console.log("我懂");
        },
        test02(){
            this.cs = 6
            console.log(this.cs);
        },
        test03(){
            this.$store.commit("addcsvuex")
            console.log(this.csvuex);
        },
        test04(){
            //socket.send('{"type":2,"data":{"type":0,"play":1},"roomId":"系统默认房间(幻鹤)","ownerId":"666"}')
            console.log(this.$store.getters.getMyWebSocket.send('{"type":2,"data":{"type":0,"play":1},"roomId":"系统默认房间(幻鹤)","ownerId":"666"}')); 
        },
        test05(){
            // socket.send('{"type":1,"data":{"type":1},"roomId":"系统默认房间(幻鹤)","ownerId":"666"}')
            console.log(this.$store.getters.getMyWebSocket.send('{"type":1,"data":{"type":1},"roomId":"系统默认房间(幻鹤)","ownerId":"666"}'))
        },
        test06(){
            console.log(this.$store.getters.getMyVideo.aVideo.play(1)); 
        },
        test07(){
            // Media = ;
            console.log(document.getElementById("dPlayerVideoMain"));
        },
        //子控件发送函数
        WebSocketSend(msg){
            socket.send(msg)
        }
    }

}
</script>

<style>

</style>