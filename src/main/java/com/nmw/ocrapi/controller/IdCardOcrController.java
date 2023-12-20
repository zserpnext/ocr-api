package com.nmw.ocrapi.controller;

import com.nmw.ocrapi.domain.param.OcrParam;
import com.nmw.ocrapi.response.ResponseResult;
import com.nmw.ocrapi.service.IdCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author :ljq
 * @date :2023/9/8
 * @description: 身份证号识别
 */
@Slf4j
@RestController
@RequestMapping("/id-card")
@Validated
public class IdCardOcrController {

    @Autowired
    private IdCardService idCardService;

    @PostMapping("/text")
    public ResponseResult<List<String>> textOnly(@RequestBody @Valid OcrParam ocrParam) {
        List<String> idNumberTextList = idCardService.ocrIdNumberText(ocrParam.getImgBase64(),ocrParam.getImgType());
        return ResponseResult.success(idNumberTextList);
    }
}
