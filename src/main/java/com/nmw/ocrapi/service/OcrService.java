package com.nmw.ocrapi.service;

import com.nmw.ocrapi.domain.dto.TextAccuracyLocationDto;
import com.nmw.ocrapi.domain.dto.TextOcrDto;

import java.util.List;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description: OCR识别实现
 */
public interface OcrService {

    /**
     * OCR识别图片，返回只包含文本的结果
     * @param imgBase64
     * @param imgType
     * @return
     */
    List<String> ocrText(String imgBase64, String imgType);


    /**
     * 返回识别结果，包含文本、位置信息、精度
     * @param imgBase64
     * @param imgType
     */
    List<TextAccuracyLocationDto> ocrTextAccuracyLocation(String imgBase64, String imgType);
}
