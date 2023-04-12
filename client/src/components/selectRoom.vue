<template>
    <div class="w-div">
        <el-card shadow="always">
            <p class="p-div">选择进入一个放映厅</p>
      <el-select v-model="roomId" class="m-2" placeholder="选则一个存在的房间">
        <el-option v-for="item in getUserSocketInfoRoomList" :key="item" :label="item" :value="item" />
      </el-select>
    <el-button @click="loginRoom" type="primary">进入这个房间</el-button>
        </el-card>
    </div>
</template>

<script >
import { mapGetters,mapMutations } from "vuex"
import { ElMessage } from "element-plus";
export default {
    name:"selectRoom",
    setup(){

    },
    mounted(){
    },
    //vuex的函数
    computed:{
        ...mapGetters(["getUserSocketInfoRoomList"]),
    },
    data(){
        return{
            roomId:null,
        }
    },
    methods:{
        loginRoom(){
            if (this.roomId == null) {
                ElMessage.error("请选择房间")
            }else{
                this.$store.commit("setRoomRoomId",this.roomId)//更新房间id
                this.$emit('getIntoRoom',this.roomId) //进入房间参数
                // this.$router.push('video')
            }
        }
    }
}
</script>

<style scoped>
.w-div {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  height: 100%;

}

.el-card {
    height: 40%;

}

.p-div {
    font-size: 20px;
    text-align: center;
    margin: 5%;
}



/* Set the width of the el-button to 100% to match the width of the el-input */
.el-select {
    margin: 10% 0;
    width: 100%;
}

.el-button {
  width: 100%;
  padding: 5%;
  
}
</style>