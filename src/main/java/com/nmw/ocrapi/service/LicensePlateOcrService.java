package com.nmw.ocrapi.service;

import java.util.List;

/**
 * @author :ljq
 * @date :2023/8/8
 * @description:
 */
public interface LicensePlateOcrService {

    /**
     * 车牌识别,仅返回文本
     * @param imgBase64
     * @param imgType
     * @return
     */
    List<String> ocrLicensePlateText(String imgBase64, String imgType);

}
