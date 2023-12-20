package com.nmw.ocrapi.filter;

import com.alibaba.fastjson2.JSON;
import com.nmw.ocrapi.response.ResponseResult;
import com.nmw.ocrapi.util.JwtTokenTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.nmw.ocrapi.constant.AuthConstants.HEADER_KEY_ACCESS_TOKEN;
import static com.nmw.ocrapi.constant.AuthConstants.REQUEST_APP_KEY;
import static com.nmw.ocrapi.exception.ExceptionEnum.TOKEN_INVALID;

/**
 * @author :ljq
 * @date :2023/12/5
 * @description: 拦截请求，从header中取出token，进行token校验
 */
@Slf4j
@Component
@Order(1)
public class AuthFilter implements Filter {

    @Value("${auth.access-token-secret}")
    private String accessTokenSecret;

    @Value("#{'${auth.white-list}'.split(',')}")
    private List<String> whiteList;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String uri = httpServletRequest.getRequestURI();

        //如果请求路径在白名单中，那么放行
        for (String whiteUri : whiteList) {
            if (uri.contains(whiteUri)) {
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }

        //如果不在白名单中，获取token，并验证token是否有效
        String accessToken = httpServletRequest.getHeader(HEADER_KEY_ACCESS_TOKEN);
        try {
            String appKey = JwtTokenTool.decodeToken(accessToken, accessTokenSecret);
            //将appKey放到request中，在其它需要appKey的地方，可以通过request的attribute获取到
            httpServletRequest.setAttribute(REQUEST_APP_KEY,appKey);
            filterChain.doFilter(servletRequest,servletResponse);
        } catch (Exception ex) {
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType("application/json");
            ResponseResult responseResult = ResponseResult.error(TOKEN_INVALID.getCode(), TOKEN_INVALID.getMessage());
            String errorMsg = JSON.toJSONString(responseResult);
            httpServletResponse.getWriter().write(errorMsg);
            httpServletResponse.getWriter().close();
        }

    }
}
