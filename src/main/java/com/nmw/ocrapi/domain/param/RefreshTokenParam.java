package com.nmw.ocrapi.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author :ljq
 * @date :2023/9/11
 * @description:刷新access_token参数
 */
@Data
public class RefreshTokenParam {
    @NotBlank(message = "refreshToken不能为空")
    private String refreshToken;
}
