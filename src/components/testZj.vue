<template>
  <p>组件传参测试</p>
  <p>子：传过来cs的值:{{ cs }}</p>
  <p>子：vueX的cs值:{{ csvuex }}</p>
  <huanhe-video/>
</template>

<script >
//vue 快速读取方式
import { mapState } from "vuex"
import HuanheVideo from './Huanhe-video.vue'

export default {
    name:"testZj",
    components:{
        HuanheVideo
    },
    props:{
        cs:Number,
    },
        //专门来读取vux的数据
    computed:{
        ...mapState(["csvuex"])
    },

}
</script>

<style>

</style>