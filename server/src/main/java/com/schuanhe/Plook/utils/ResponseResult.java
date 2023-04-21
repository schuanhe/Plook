package com.schuanhe.Plook.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

/**
 * @Author schuanhe  github： https://github.com/schuanhe
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    // 构造函数
    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 成功
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<T>(HttpStatus.OK.value(), "success", null);
    }

    /**
     * 成功响应
     * @param data 返回的数据
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<T>(HttpStatus.OK.value(), "success", data);
    }

    /**
     * 失败 带
     * @param message
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> fail(String message) {
        return new ResponseResult<T>(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    // 错误的响应
    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    //错误的响应带错误代码
    public static <T> ResponseResult<T> error(String message, int code) {
        return new ResponseResult<T>(code, message, null);
    }

    public static <T> ResponseResult<T> fail(String message, int code) {
        return new ResponseResult<T>(code, message, null);
    }
}