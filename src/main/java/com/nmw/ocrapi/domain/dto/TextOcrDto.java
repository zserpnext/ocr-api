package com.nmw.ocrapi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description:只返回识别的文本
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TextOcrDto {
    /**
     * 识别的文本
     */
    private String text;
}
