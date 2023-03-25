import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import VueVideoPlayer from 'vue-video-player'
import 'video.js/dist/video-js.css'

createApp(App).use(ElementPlus).use(VueVideoPlayer).mount('#app')
