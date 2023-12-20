package com.nmw.ocrapi.service;


import com.nmw.ocrapi.domain.dto.AuthTokenDto;

/**
 * @author :ljq
 * @date :2023/9/8
 * @description: 鉴权认证接口
 */
public interface AuthService {

    /**
     * 认证接口，返回相关的token
     * @param appKey
     * @param appSecret
     * @return
     */
    AuthTokenDto auth(String appKey, String appSecret);

    /**
     * token刷新，由refresh_token获取新的access_token
     * @param refreshToken
     * @return
     */
    String refreshToken(String refreshToken);
}
