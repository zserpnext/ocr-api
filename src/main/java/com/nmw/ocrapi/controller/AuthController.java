package com.nmw.ocrapi.controller;

import com.nmw.ocrapi.domain.dto.AuthTokenDto;
import com.nmw.ocrapi.domain.param.AuthParam;
import com.nmw.ocrapi.domain.param.RefreshTokenParam;
import com.nmw.ocrapi.response.ResponseResult;
import com.nmw.ocrapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author :ljq
 * @date :2023/9/8
 * @description:认证授权接口，返回token
 */
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 认证接口获取token
     * @param authParam
     * @return
     */
    @PostMapping("/access-token")
    public ResponseResult<AuthTokenDto> createToken(@RequestBody @Valid AuthParam authParam) {
        AuthTokenDto authTokenDto = authService.auth(authParam.getAppKey(), authParam.getAppSecret());
        return ResponseResult.success(authTokenDto);
    }

    /**
     * 刷新token
     * @param refreshTokenParam
     * @return
     */
    @PostMapping("/refresh-token")
    public ResponseResult<String> refreshToken(@RequestBody @Valid RefreshTokenParam refreshTokenParam) {
        String accessToken = authService.refreshToken(refreshTokenParam.getRefreshToken());
        return ResponseResult.success(accessToken);
    }
}
