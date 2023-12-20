package com.nmw.ocrapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description:识别结果包含文本、可信度、字符位置
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TextAccuracyLocationDto {
    /**
     * 识别的字符文本
     */
    private String text;
    /**
     * 可信度
     */
    private String accuracy;
    /**
     * 识别的字符文本在图片上的位置，按照顺时针 左上、右上、右下、左下
     */
    private Map<String,OcrPoint> location;
}
