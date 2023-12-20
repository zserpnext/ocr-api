package com.nmw.ocrapi.exception;

/**
 * @author :ljq
 * @date :2023/11/23
 * @description: 业务异常枚举类
 */
public enum ExceptionEnum {
    /**
     * 传入的参数有问题
     */
    PARAM_ERROR(1001, "参数有误"),

    /**
     * paddler_ocr识别图片发生错误
     */
    PADDLE_OCR_ERROR(1101,"识别图片出错"),

    OCR_BASE64_SAVE_TO_IMG_ERROR(1102,"base64字符串转存为图片出错"),

    LICENSE_OCR_ERROR(1200,"未识别出有效车牌"),

    ID_CARD_ORC_ERROR(1200, "未识别出有效身份证号"),

    /**
     * access_token无效
     */
    TOKEN_INVALID(1301, "token无效"),

    /**
     * access_token创建失败
     */
    TOKEN_CREATE_FAIL(1302,"access_token获取失败，appKey 或 appSecret 不正确"),

    /**
     * access_token刷新失败
     */
    TOKEN_REFRESH_FAIL(1303,"access_token刷新失败"),

    /**
     * paddle-ocr-api 调用被限流或熔断
     */
    PADDLE_OCR_TOO_FREQUENTLY(1400,"paddle-ocr-api调用被限流或熔断");

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ExceptionEnum(int code, String getMessage) {
        this.code = code;
        this.message = getMessage;
    }
}
