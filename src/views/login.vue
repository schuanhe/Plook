<template>
    <div class="w-div">
        <el-card shadow="always">
            <p class="p-div">登录</p>
            <el-input v-model="userName" placeholder="请输入用户名" />
            <el-button type="primary" @click="login" plain>登录</el-button>
        </el-card>
    </div>
</template>

<script>
import { ElMessage } from "element-plus";
import { mapState } from "vuex"
export default {
    name:"login",
    mounted(){
      this.userName = this.$store.getters.getUserUserName
    },
    data(){
        return{
            userName:null
        }
    },
    methods:{
        login(){
            if (this.userName==null) {
                ElMessage.error("请输入用户名")
            }else{
                this.$store.commit("setUserUserName",this.userName)
                this.$router.push('pc')
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
  height: 100vh;

}

.el-card {
    height: 30%;

}

.p-div {
    font-size: 20px;
    text-align: center;
    margin: 5%;
}



/* Set the width of the el-button to 100% to match the width of the el-input */
.el-input {
    margin: 10% 0;
}

.el-button {
  width: 100%;
  padding: 5%;
  
}
</style>