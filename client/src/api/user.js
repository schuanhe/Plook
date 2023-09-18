import request from '../utils/request'

/**
 * 登录api
 */
export function loginApi(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

/**
 * 注册
 */
export function registerApi(data) {
    return request({
        url: '/user/register',
        method: 'post',
        data
    })
}