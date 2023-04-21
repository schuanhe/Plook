import { createRouter,createWebHashHistory, createWebHistory } from "vue-router"

const routes = [
    {
        path:"/",
        name:"登录",
        component:()=>import("../views/login.vue")
    },
    {
        path:"/pc",
        name:"首页",
        component:()=>import('../views/viewsVideo.vue'),
        //重定向页面，默认显示子类
        redirect:"/pc/selectRoom",
        children:[
            {
                path:"selectRoom",
                name:"选择房间",
                
                component:()=>import("../components/selectRoom.vue")
            },
            {
                path:"room/:roomId",
                name:"进入房间",
                component:()=>import("../components/roomVideo.vue")
            }
        ]
    },
    {
        path:"/test",
        component:()=>import("../views/test.vue")
    }
]

const router = createRouter({
    //
    history:createWebHashHistory(),
    // history:createWebHistory(import.meta.env.VITE_BASE_URL),
    routes
})

router.beforeEach((to, from,next) =>{
    // console.log(to,from)
    next()
})


export default router