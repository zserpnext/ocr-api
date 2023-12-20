package com.nmw.ocrapi.service;

import java.util.List;

/**
 * @author :ljq
 * @date :2023/9/8
 * @description: 身份证识别服务
 */
public interface IdCardService {

    /**
     * 身份证识别,仅返回文本
     * @param imgBase64
     * @param imgType
     * @return
     */
    List<String> ocrIdNumberText(String imgBase64, String imgType);

}
