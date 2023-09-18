<template>
    <div class="w-div">
        <el-card shadow="always">
<el-tabs>
    <el-tab-pane label="进入公开房间" name="goinRoomBySelect">
            <p class="p-div">选择进入一个放映厅</p>
            <div class="form-group">

            <el-select v-model="selectRoomId" filterable placeholder="选则一个存在的房间">
                <el-option v-for="item in RoomList" :key="item" :label="item.name" :value="item.name" />
            </el-select>
            <el-button @click="setRoomList" type="success">刷新房间</el-button>
            <el-button @click="loginRoom(0)" type="primary">进入这个房间</el-button>
            <el-button @click="setDialogSetRoom" type="primary">创建房间</el-button>
            </div>        
    </el-tab-pane>
    <el-tab-pane label="输入房间号进入" name="goinRoomByRoomId">
            <p class="p-div">输入指定放映厅id</p>
            <div class="form-group">
            <el-input placeholder="输入房间id" :min="1" type="number" v-model="rid"></el-input>

            <el-input placeholder="输入房间密码，公开房间不需要" type="string" v-model="password"></el-input>
            <el-button @click="loginRoom(1)" type="primary">进入这个房间</el-button>
            <el-button @click="setDialogSetRoom" type="primary">创建房间</el-button>
            </div>            
    </el-tab-pane>
    <el-tab-pane label="创建房间" name="addRoom">
            <div class="form-group">
            <el-input placeholder="输入房间名称" type="string" v-model="roomInfo.roomName"></el-input>
            <el-input placeholder="输入资源标题" type="string" v-model="roomInfo.title"></el-input>
            <el-input placeholder="输入资源Url" type="string" v-model="roomInfo.url"></el-input>
            <el-switch inactive-text="公开" active-text="需要密码" inline-prompt v-model="roomInfo.isPassword" active-color="#13ce66" inactive-color="#ff4949" />
            <el-input placeholder="输入房间密码，公开房间不需要" type="string" v-if="roomInfo.isPassword" v-model="roomInfo.password"></el-input>
            <el-button @click="setDialogSetRoom" type="primary">创建这个房间</el-button>
            </div>            
    </el-tab-pane>    
</el-tabs>



        </el-card>
    </div>
</template>

<script setup>
import { mapGetters, mapMutations } from "vuex"
import { ElMessage } from "element-plus";
import { getRoomList, joinRoom, createRoom } from "../api/room"
import { ref } from "vue";

const selectRoomId = ref(0) //选择的房间id
const roomId = ref("") //选择的房间id
const password = ref("") //选择的房间密码
const RoomList = ref([]) //房间列表

const dialogSetRoom = ref({
    type: true,
    form: {
        roomName: "",
        isPassword: false,
        password: "",
        title: "",
        url: "",
    }
})

const roomInfo = ref({
        roomName: "",
        isPassword: false,
        password: "",
        title: "",
        url: "",    
})

const loginRoom = (selectRoomId) => {
    if(selectRoomId==0){
        this.roomId=this.roomId
    }else{
        this.roomId=this.rid
    }

    if (this.roomId == null || this.roomId == "") {
        ElMessage.error("请选择房间")
    } else {
        this.$store.commit("setRoomRoomId", this.roomId)//更新房间id
        //进入房间
        joinRoom(this.roomId).then(res => {
            ElMessage.success("进入房间成功")
            this.$router.push("room/" + this.roomId)
        })
    }
}
//刷新房间列表
const setRoomList = () => {
    getRoomList().then(res => {
        this.RoomList = res.data
        ElMessage.success("刷新房间列表成功")
    })
}
//创建房间
const setRoom = () => {
    if (this.dialogSetRoom.form.name == null) {
        ElMessage.error("请输入房间名")
    } else {
        createRoom(this.dialogSetRoom.form).then(res => {
            ElMessage.success("创建房间成功")
            //跳转视频播放
            this.$router.push("room/" + res.data.name)
        }).catch(err => {
            ElMessage.error("创建房间失败", err)
        })//错误处理 可以将错误发送到屏幕上面 或者在你的
    }
}
const setDialogSetRoom = () => {
    dialogSetRoom.form.name = this.getUserUserName + "的房间"
    dialogSetRoom.form.admin = this.getUserUserName
    dialogSetRoom.type = true
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
    width: 30%;
    min-width: 300px;
    height: 50%;
    min-height: 350px;
}

.p-div {
    font-size: 20px;
    text-align: center;
    margin: 5%;
}

.form-group {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}


/* Set the width of the el-button to 100% to match the width of the el-input */
.el-select {
    width: 100%;
    margin: 5% 0;
}


/* 两个.el-button最大长度 */
.el-button {
    width: 98%;
    margin: 5px;
}
.el-input {
    width: 100%;
    margin: 5px 0;
}

.el-dialog {
    min-width: 50%;
}
</style>