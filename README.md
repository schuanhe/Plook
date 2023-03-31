# Plook

这个分支这种开发，请先切换到nodb分支

## 项目介绍

Plook 是一个基于 WebSocket 实现的网页端一起看功能的项目，允许用户在同一房间内共享视频，并且可以实时聊天。本项目为 Plook 的服务器端，使用 Spring Boot 框架开发，提供视频的管理、房间的管理以及 WebSocket 通信等功能。

## 项目初衷

大多数视频平台的一起看功能只能看平台上的视频，用户想看的资源如果平台上没有就无法实现共享观看，所以开发了 Plook 项目，允许用户自定义视频源，实现共享观看的功能。另外，该项目也可以被异地恋情侣用于共享观看视频，增强情感交流。

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
git clone -b nodb https://github.com/schuanhe/Plook-client.git
```

gitee

```bash
git clone -b nodb https://gitee.com/huanhe/Plook-client.git
```

进入项目目录

```bash
cd Plook-client
```

安装依赖

```bash
npm install
```

启动服务

如果你需要前后端分离，那么在项目的` src\components\HellWord.vue`的172行可以改后端地址，

```bash
# 可以直接用vite打包
vite run build 
```

然后替换到后端的静态文件目录

具体可以看后端的自述文档

