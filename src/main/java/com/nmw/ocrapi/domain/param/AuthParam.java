package com.nmw.ocrapi.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author :ljq
 * @date :2023/9/8
 * @description:认证接口参数
 */
@Data
public class AuthParam {

    @NotBlank(message = "appKey不能为空")
    private String appKey;

    @NotBlank(message = "appSecret不能为空")
    private String appSecret;
}
