# Plook


## 最近更新

1. 优化了ui，并且重构了大部分代码
2. 视频可支持m3u8格式视频
3. 收到房间里消息会自动显示最后一条消息

## 其他

该项目还处于开发阶段，欢迎大家加入交流群
qq群:[486601640](https://jq.qq.com/?_wv=1027&k=kSkx46a7)

## 功能介绍

Plook-client 提供了以下功能：

1. 视频播放：用户可以通过选择播放视频的源来观看视频。
2. 视频控制：支持视频的暂停、播放和进度条拖动操作。
3. 实时聊天室：用户可以在视频播放的同时与其他观看视频的用户进行实时的文字聊天交流。



## 技术栈

- Vue.js
- Element-UI
- video.js
- WebSocket

## 项目运行

克隆项目（github）

```bash
git clone -b nodb https://github.com/schuanhe/Plook.git
```


进入项目目录

```bash
cd Plook/client
```

安装依赖

```bash
npm install
```

启动服务

```bash
# 可以直接用vite打包
vite run build 
```

然后替换到后端的静态文件目录

具体可以看后端的自述文档

