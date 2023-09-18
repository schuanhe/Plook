<template>
    <div class="w-div">
        <el-card class="custom-card" shadow="always"  v-loading="loading">
            <el-tabs v-model="currentTab">
                <el-tab-pane label="登录" name="login">
                    <div class="form-group">
                        <el-input v-model="form.username" placeholder="请输入用户名" />
                        <el-input v-model="form.password" placeholder="请输入密码" />
                        <el-button type="primary" @click="login" plain>登录</el-button>
                    </div>
                </el-tab-pane>
                <el-tab-pane label="注册" name="register">
                    <div class="form-group">
                        <el-input v-model="form.username" placeholder="请输入用户名" />
                        <el-input v-model="form.password" placeholder="请输入密码" />
                        <el-input v-model="form.qq" placeholder="请输入邮箱" />
                        <el-button type="primary" @click="register" plain>注册</el-button>
                    </div>
                </el-tab-pane>
            </el-tabs>
        </el-card>
    </div>
</template>

<script setup>
import { ElMessage } from "element-plus";
import Cookie from "js-cookie";
import { ref,reactive } from "vue";
import { useRouter } from 'vue-router';
import { loginApi,registerApi } from "../api/user.js";

const loading = ref(false)

const router = useRouter();

const currentTab='login'

// 设置表单数据
const form = reactive({
    username: null,
    password: null,
    qq: null,
})

const login = () => {
    if (form.username == null || form.username=="" || form.password == null || form.password == "") {
        ElMessage.error("请输入用户名和密码")
    } else {
        loading.value = true
        loginApi(form).then(res => {
            loading.value = false
            if (res.code == 200) {
                ElMessage.success("登录成功")
                Cookie.set("userId",res.data.id)
                // TODO 成功事件
                router.push("/pc")
            } else {
                ElMessage.error(res.message)
            }
        }).catch(err => {
            loading.value = false
            ElMessage.error("其他错误:" + err.message)
        })
    }
}
const register = () => {
    if (form.username == null || form.username=="" || form.password == null || form.password == "" || form.qq == null || form.qq == "") {
        ElMessage.error("表单不完整")
    } else {
        loading.value = true
        registerApi(form).then(res => {
            loading.value = false
            if (res.code == 200) {
                ElMessage.success("注册成功")
                // TODO 成功事件
                Cookie.set("userId",res.data.id)
                router.push("/pc")
            } else {
                ElMessage.error(res.message)
            }
        }).catch(err => {
            loading.value = false
            ElMessage.error("其他错误:" + err.message)
        });
    }
}


</script>

<style scoped>
.w-div {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    /* 这会使外层容器占据整个视口高度，实现垂直居中 */
}

.custom-card {
    width: 30%;
    min-width: 300px;
    height: 30%;
    min-height: 250px;
    /* 可以根据需要添加其他样式 */
}

.form-group {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.el-input {
    margin: 3px; /* 上下边距，根据需要进行调整 */
    max-width: 99%;
}
.el-button {
    margin: 10px 0; /* 上下边距，根据需要进行调整 */
}
</style>