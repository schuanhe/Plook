import request from '../utils/request'

/**
 * 创建房间api
 */
export function createRoom(data) {
  return request({
    url: '/room/createRoom',
    method: 'post',
    data
  })
}

/**
 * 加入房间
 * @param {*} id 房间id
 * @returns 
 */
export function joinRoom(id) {
  // 使用request函数发送一个PUT请求，请求的URL为/user/id
  return request({
    url: `/room/joinRoom/${id}`,
    method: 'get',
  })
}

/**
 * 获取房间列表
 */
export function getRoomList() {
  return request({
    url: '/room/getRoomList',
    method: 'get',
  })
}

/**
 * 退出房间
 */
export function exitRoom(id) {
  return request({
    url: `/room/exitRoom`,
    method: 'get',
  })
}