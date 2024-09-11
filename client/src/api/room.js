import { request } from "./request";


// 获取房间列表
export function getRoomList() {
    return request({
        url: `rooms`,
    });
}

// 新增房间
export function createRoom(data) {
    return request({
        url: `rooms`,
        method: "POST",
        data,
    });
}