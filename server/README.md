# Plook 服务器端项目自述文档



## 使用技术

- Spring Boot：快速搭建应用程序
- WebSocket：基于 TCP 协议实现全双工通信，提供实时性的数据传输
- Maven：项目构建和依赖管理工具

## 搭建教程

### 方法




### 直接打包

我们需要克隆Plook-server代码，并使用Maven构建项目。请执行以下命令：

```
git clone https://github.com/schuanhe/Plook.git
cd Plook/server
mvn package
```

执行以上命令后，Maven会下载项目依赖并构建项目，生成可执行的JAR文件。构建完成后，可以在`target`目录下找到`Plook-server-1.0-SNAPSHOT.jar`文件。

运行Plook-server

最后，我们可以使用以下命令来运行Plook-server：

```
java -jar target/Plook-server-1.0-SNAPSHOT.jar
```

> 如果要前端自定义，直接替换static文件夹中的内容就行

### 宝塔部署

可以直接下载jar包

1. 点击网站->点击Java项目
2. 点击添加啊springboot项目
3. 项目路径选择你上传的jar包的位置
4. 端口1999
5. 绑定域名可以直接写
6. 提交，然后访问服务器ip:1999