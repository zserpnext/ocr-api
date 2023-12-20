package com.nmw.ocrapi.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.nmw.ocrapi.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

import static com.nmw.ocrapi.exception.ExceptionEnum.TOKEN_INVALID;


/**
 * @author :ljq
 * @date :2023/9/11
 * @description: jwt token生成和解码相关的工具方法
 */
@Slf4j
public final class JwtTokenTool {

    public static final String KEY_APP_KEY = "appKey";

    /**
     * 生成 token
     *
     * @param appKey
     * @param tokenSecret 生成token的密码
     * @param expireTime
     * @return
     * @throws JWTCreationException
     */
    public static String createToken(String appKey, String tokenSecret, long expireTime) {

        Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
        return JWT.create()
                .withExpiresAt(new Date(expireTime))
                //正常来讲，这里还会将userId等信息也加到token里面，以便解析token时能直接获取userId
                .withClaim(KEY_APP_KEY, appKey)
                .sign(algorithm);
    }

    /**
     * 解析token,获取appKey
     *
     * @param token
     * @param tokenSecret 生成token的密码
     * @return appKey
     * @throws ServiceException token解码失败
     */
    public static String decodeToken(String token, String tokenSecret) throws ServiceException {
        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwt = verifier.verify(token);
        } catch (Exception ex) {
            log.error("解析token失败,token:{}",token);
            throw new ServiceException(TOKEN_INVALID);
        }
        //正常来讲还会获取userId等信息返回，以便业务后续处理
        return jwt.getClaims().get(KEY_APP_KEY).asString();
    }
}
