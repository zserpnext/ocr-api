package com.nmw.ocrapi.service.impl;

import com.nmw.ocrapi.domain.dto.AuthTokenDto;
import com.nmw.ocrapi.exception.ServiceException;
import com.nmw.ocrapi.service.AuthService;
import com.nmw.ocrapi.util.JwtTokenTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.nmw.ocrapi.exception.ExceptionEnum.TOKEN_CREATE_FAIL;
import static com.nmw.ocrapi.exception.ExceptionEnum.TOKEN_REFRESH_FAIL;

/**
 * @author :ljq
 * @date :2023/9/11
 * @description:鉴权认证、获取token、刷新token
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * 用来进行演示校验过程的虚拟 APP_KEY和 APP_SECRET
     */
    private final static String APP_KEY = "android_client";
    private final static String APP_SECRET = "android_client";

    /**
     * 生成access_token的密码
     */
    @Value("${auth.access-token-secret}")
    private String accessTokenSecret;

    /**
     * 生成刷新refresh_token的密码
     */
    @Value("${auth.refresh-token-secret}")
    private String refreshTokenSecret;

    /**
     * access_token有效时间
     */
    @Value("${auth.access-token-expire-seconds}")
    private Long accessTokenExpireSeconds;

    /**
     * refresh_token有效时间
     */
    @Value("${auth.refresh-token-expire-days}")
    private Integer refreshTokenExpireDays;

    @Override
    public AuthTokenDto auth(String appKey, String appSecret) {
        /**
         * 1.校验appKey 和 appSecret 是否正确。 正常来讲是需要查询数据库，这里我们简单内定一个虚拟的，模拟一下校验吧。
         */
        try {
            if (APP_KEY.equals(appKey) && APP_SECRET.equals(appSecret)) {
                long accessTokenExpireAt = System.currentTimeMillis() + accessTokenExpireSeconds * 1000;
                long refreshTokenExpireAt = System.currentTimeMillis() + refreshTokenExpireDays * 24 * 3600 * 1000L;
                String accessToken = JwtTokenTool.createToken(appKey, accessTokenSecret, accessTokenExpireAt);
                String refreshToken = JwtTokenTool.createToken(appKey, refreshTokenSecret, refreshTokenExpireAt);
                log.info("获取access_token:{} refresh_token:{}",accessToken,refreshToken);
                return new AuthTokenDto(accessToken,refreshToken);
            }
            throw new ServiceException(TOKEN_CREATE_FAIL);
        } catch (Exception e) {
            throw new ServiceException(TOKEN_CREATE_FAIL);
        }
    }

    @Override
    public String refreshToken(String refreshToken) {
        String appKey = JwtTokenTool.decodeToken(refreshToken, refreshTokenSecret);
        try {
            long accessTokenExpireAt = System.currentTimeMillis() + accessTokenExpireSeconds * 1000;
            return JwtTokenTool.createToken(appKey, accessTokenSecret, accessTokenExpireAt);
        } catch (Exception ex) {
            log.error("刷新token失败，refreshToken:{} 错误是：",refreshToken,ex);
            throw new ServiceException(TOKEN_REFRESH_FAIL);
        }
    }
}
