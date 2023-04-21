// 引入axios库
import axios from "axios"
import { ElMessage } from "element-plus"

// 基础url
let baseURL
if (import.meta.env.VITE_BASE_URL_S == "true") {
  baseURL=window.location.protocol
}else{
  baseURL=import.meta.env.VITE_BASE_URL
}

// 创建axios实例
const service = axios.create({
  baseURL: baseURL, // api的base_url
  timeout: 5000, // 请求超时时间
  //带上cookie
  withCredentials: true,
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    return config
  },
  error => {
    // 对请求错误做些什么
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// respone拦截器
service.interceptors.response.use(
  /**
   * 如果您想获得http信息，如头信息或状态信息
   * 请返回response => response
   */

  /**
   * 通过自定义代码确定请求状态
   * 这里只是一个例子
   * 您还可以通过HTTP状态码来判断状态
   */
  response => {
    const res = response.data
    if (res.code !== 200) {
      console.log(res);
      //出现错误
      return response.data
    } else {
      return response.data
    }
  },
  error => {
    console.log('err' + error)// for debug
    alert('网络错误')
    return Promise.reject(error)
  }
)

export default service

// 这是一个封装了axios的工具类，用于发送http请求
// 可以通过调用该工具类中的方法来发送请求
// 该工具类还包含了request和response的拦截器，用于在请求和响应时进行一些处理