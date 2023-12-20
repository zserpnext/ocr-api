package com.nmw.ocrapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description: 返回识别结果包含文本和可信度
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TextAccuracyDto {
    /**
     * 识别文本
     */
    private String text;
    /**
     * 识别可信度
     */
    private String accuracy;
}
