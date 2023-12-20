package com.nmw.ocrapi.response;

import static com.nmw.ocrapi.constant.ResponseConstants.*;


/**
 * @author : ljq
 * @date :2023/11/23
 * @description: 接口返回消息体
 */
public class ResponseResult<T>{

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误提示
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;


    /**
     * 返回成功消息，默认消息提示
     * @return
     */
    public static ResponseResult success() {
        return new ResponseResult<>(CODE_SUCCESS, MSG_SUCCESS);
    }

    /**
     * 返回成功消息
     * @param msg 消息提示
     * @return
     */
    public static ResponseResult success(String msg) {
        return new ResponseResult<>(CODE_SUCCESS, msg);
    }

    /**
     * 返回成功消息，包含数据，默认消息提示
     *
     * @param result 接口数据
     * @param <T>    返回数据类型
     * @return
     */
    public static <T> ResponseResult<T> success(T result) {
        return new ResponseResult<>(CODE_SUCCESS, MSG_SUCCESS, result);
    }

    /**
     * 返回成功消息，包含数据
     * @param result 接口数据
     * @param msg 消息提示
     * @param <T> 返回数据类型
     * @return
     */
    public static <T> ResponseResult<T> success(T result,String msg) {
        return new ResponseResult<>(CODE_SUCCESS, msg, result);
    }


    /**
     * 返回错误消息，错误码默认
     *
     * @param msg 返回错误提示
     * @return 警告消息
     */
    public static <T> ResponseResult<T> error(String msg) {
        return new ResponseResult<>(CODE_FAIL, msg);

    }


    /**
     * 返回错误消息，自定义错误信息和错误码
     *
     * @param code 错误码
     * @param msg 返回错误提示
     * @param <T>
     * @return
     */
    public static <T> ResponseResult<T> error(int code, String msg) {

        return new ResponseResult<>(code, msg);

    }


    public ResponseResult() {
    }

    private ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
