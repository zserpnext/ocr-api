package com.nmw.ocrapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :ljq
 * @date :2023/9/8
 * @description:认证成功后返回的token等信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthTokenDto {
    /**
     * 请求接口的token
     */
    private String accessToken;

    /**
     * accessToken过期时，用来刷新accessToken的token
     */
    private String refreshToken;
}
