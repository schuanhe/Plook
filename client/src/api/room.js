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
 */
export function joinRoom(data) {
  return request({
    url: `/room/joinRoom/${id}`,
    method: 'post',
    data
  })
}

/**
 * 获取房间信息
 */
export function getRoomInfo(rid) {
  return request({
    url: `/room/getRoom/${rid}`,
    method: 'get',
  })
}

/**
 * 获取公开房间列表
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
export function exitRoom(rid) {
  return request({
    url: `/room/exitRoom/${rid}`,
    method: 'get',
  })
}