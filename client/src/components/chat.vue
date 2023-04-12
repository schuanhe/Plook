<template>
    <el-row class="row-bg" v-if="(chatType == 0)">
    <el-card class="box-card" body-style="padding:0px 10px; margin:10px;">
        <div>
            <p style="font-size: 5px;height: 20px; text-align:left">{{ chatList.ownerId }}</p>
            <el-divider style="margin:0px" />
            <p>{{ chatList.data.msg }}</p>            
        </div>

    </el-card>
  </el-row>
  <el-row class="row-bg" v-else-if="(chatType == 1)">
    <p>欢迎:{{ chatList.ownerId }} 进入房间</p>
  </el-row>
  <el-row class="row-bg" v-else-if="(chatType == 3)">
    <p>{{ chatList.ownerId }} 离开了房间</p>
  </el-row>
  
  <el-row class="row-bg" justify="end" v-else-if="(chatType == 2)">
    <el-card class="box-card" body-style="padding:0px 10px; margin:10px;">
        <p style="font-size: 5px;height: 20px; text-align:right; color: green;">{{ userName }}</p>
        <el-divider style="margin:0px" />
        <p>{{ chatList.data.msg }}</p>
    </el-card>    
  </el-row>

</template>

<script>

export default ({
    name: "chat",
    components: {

    },
    props: {
        chats: {
            type:Object
        },
        userName:{
            type:String
        }
    },
    data() {
        return {
            chatList:null,
            chatType:null
        }
    },
    created() {
        // //属性渲染前
         this.chatList = this.chats
            if (this.chats.ownerId == this.userName) {
                this.chatType = 2
            }else if (this.chats.type == 1) {
                this.chatType = 1
                if(this.chats.data.type == 2){
                    this.chatType = 3 //退出房间
                }
            }else{
                this.chatType = 0
            }
    },
    mounted(){
        this.$emit('ScrollbarRef')
    },
    methods: {

    }

})
</script>


<style>
.el-row {
  margin-bottom: 20px;
}
#main {
    display: flex;
}

.msg_right {
    text-align: right;
}

#p-msginfo {

}
</style>