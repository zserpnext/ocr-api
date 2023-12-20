package com.nmw.ocrapi.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :ljq
 * @date :2023/11/15
 * @description: post请求paddleocr参数
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaddleOcrPostParam {
    private String imgPath;
}
