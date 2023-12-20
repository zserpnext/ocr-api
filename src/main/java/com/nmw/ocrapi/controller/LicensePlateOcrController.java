package com.nmw.ocrapi.controller;

import com.nmw.ocrapi.domain.param.OcrParam;
import com.nmw.ocrapi.response.ResponseResult;
import com.nmw.ocrapi.service.LicensePlateOcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author :ljq
 * @date :2023/8/8
 * @description: 车牌识别相关接口
 */
@Slf4j
@RestController
@RequestMapping("/license-plate")
public class LicensePlateOcrController {

    @Autowired
    private LicensePlateOcrService licensePlateOcrService;

    @PostMapping("/text")
    public ResponseResult<List<String>> textOnly(@RequestBody @Valid OcrParam ocrParam) {
        List<String> plateTextList = licensePlateOcrService.ocrLicensePlateText(ocrParam.getImgBase64(),ocrParam.getImgType());
        return ResponseResult.success(plateTextList);
    }
}
