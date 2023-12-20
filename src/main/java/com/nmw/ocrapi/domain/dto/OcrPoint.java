package com.nmw.ocrapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description:文本字符的像素坐标
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OcrPoint {
    /**
     * 横坐标的值
     */
    private String x;

    /**
     * 纵坐标的值
     */
    private String y;
}
