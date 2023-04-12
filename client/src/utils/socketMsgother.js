

const data = {
    "type": 0,    // 事件相关
    "data": 0,   //数据
    // "room": {   //房间信息
    //   "id":null,
    //   "name":null,
    //   "admin":null
    // }, 
    // "owner": {    //该消息所有者信息
    //   "id":null,
    //   "name":null,
    //   "time": time.toLocaleString()   //消息时间
    // },
    // 要删的
    "roomId":"默认房间",
    "ownerId":"默认名称"
};

// 其他函数
const otherFun = {
    getIntoRoomFun(Allinfo){
        //房间加入
        data
        data.type = 1
        data.data = {"type":1}
        data.roomId = Allinfo.room.roomId 
        data.ownerId = Allinfo.user.userName  //
    return JSON.stringify(data)
    },
    setMyassg(massg){
        //发送消息
        data
        data.type = 3
        data.data = {"type":0,"msg":massg}
        return JSON.stringify(data)
    },
    setVideoSrcFun(src){
        data
        data.type = 2
        data.data = {"type":2,"src":src.src,"srcType":src.type}
        return JSON.stringify(data)
    }
  }


  export default otherFun