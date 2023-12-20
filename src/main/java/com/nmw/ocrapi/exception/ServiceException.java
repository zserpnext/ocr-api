package com.nmw.ocrapi.exception;

/**
 * @author :ljq
 * @date :2023/11/23
 * @description: 业务异常
 */
public class ServiceException extends RuntimeException {
    /**
     * 错误码
     */
    private int code;

    /**
     * 异常信息
     */
    private String message;


    public ServiceException(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public ServiceException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public ServiceException(ExceptionEnum exceptionEnum) {
        this.message = exceptionEnum.getMessage();
        this.code = exceptionEnum.getCode();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
