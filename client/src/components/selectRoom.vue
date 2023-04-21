<template>
    <div class="w-div">
        <el-card shadow="always">
            <p class="p-div">选择进入一个放映厅</p>
            <el-select v-model="roomId" filterable class="m-2" placeholder="选则一个存在的房间">
                <el-option v-for="item in RoomList" :key="item" :label="item.name" :value="item.name" />
            </el-select>
            <el-button @click="setRoomList" type="success">刷新房间</el-button>
            <el-button @click="loginRoom" type="primary">进入这个房间</el-button>
            <el-button @click="setDialogSetRoom" type="primary">创建房间</el-button>
        </el-card>
    </div>

    <el-dialog v-model="dialogSetRoom.type" title="创建房间">
    <el-form :model="dialogSetRoom.form" label-position="top">
      <el-form-item label="输入房间名:">
        <el-input v-model="dialogSetRoom.form.name" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="setRoom">
          创建房间
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script >
import { mapGetters,mapMutations } from "vuex"
import { ElMessage } from "element-plus";
import { getRoomList,joinRoom,createRoom } from "../api/room"

export default {
    name:"selectRoom",
    setup(){

    },
    mounted(){
        //获取房间列表
        this.setRoomList()

        
    },
    //vuex的函数
    computed:{
        ...mapGetters(["getUserUserName"]),
    },
    data(){
        return{
            //新建房间弹窗状态
            dialogSetRoom:{
                type:false,
                form:{
                    name:null,
                    admin:null,
                    room_aut:false,
                }
            },
            roomId:"",
            RoomList:[],
        }
    },
    methods:{
        loginRoom(){
            if (this.roomId == null||this.roomId == "") {
                ElMessage.error("请选择房间")
            }else{
                this.$store.commit("setRoomRoomId",this.roomId)//更新房间id
                //进入房间
                joinRoom(this.roomId).then(res=>{
                    ElMessage.success("进入房间成功")
                    this.$router.push("room/"+this.roomId)
                })
            }
        },
        //刷新房间列表
        setRoomList(){
            getRoomList().then(res=>{
                this.RoomList = res.data
                ElMessage.success("刷新房间列表成功")
            })
        },
        //创建房间
        setRoom(){
            if (this.dialogSetRoom.form.name == null) {
                ElMessage.error("请输入房间名")
            }else{
                createRoom(this.dialogSetRoom.form).then(res=>{
                    ElMessage.success("创建房间成功")
                    //跳转视频播放
                    this.$router.push("room/"+res.data.name)
                }).catch(err=>{
                    ElMessage.error("创建房间失败",err)
                })//错误处理 可以将错误发送到屏幕上面 或者在你的
            }
        },
        setDialogSetRoom(){
            this.dialogSetRoom.form.name = this.getUserUserName + "的房间"
            this.dialogSetRoom.form.admin = this.getUserUserName
            this.dialogSetRoom.type = true
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
    height: 60%;

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
/* 两个.el-button最大长度 */
.el-button { 	
    width: 100%;
    margin: 5px;
}
</style>