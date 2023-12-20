package com.nmw.ocrapi.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description: OCR 接口参数
 */
@Data
public class OcrParam {

    /**
     * 图片base64编码后的字符串
     */
    @NotBlank(message = "图片base64字符串不能为空")
    private String imgBase64;

    /**
     * 图片格式
     */
    @NotBlank(message = "imgType图片类型不能为空")
    private String imgType;
}
