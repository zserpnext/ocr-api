package com.nmw.ocrapi.controller;

import com.nmw.ocrapi.domain.dto.TextAccuracyLocationDto;
import com.nmw.ocrapi.domain.param.OcrParam;
import com.nmw.ocrapi.response.ResponseResult;
import com.nmw.ocrapi.service.OcrService;
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
 * @date :2023/11/9
 * @description: OCR文本识别接口
 */
@RestController
@RequestMapping("/text")
@Validated
public class OcrTextController {

    @Autowired
    private OcrService ocrService;

    @PostMapping("/text-only")
    public ResponseResult<List<String>> textOnly(@RequestBody @Valid  OcrParam ocrParam){
        List<String> textList =  ocrService.ocrText(ocrParam.getImgBase64(), ocrParam.getImgType());
        return ResponseResult.success(textList);
    }

    @PostMapping("/text-location-accuracy")
    public ResponseResult<List<TextAccuracyLocationDto>> textLocationAccuracy(@RequestBody @Valid OcrParam ocrParam) {
        List<TextAccuracyLocationDto> ocrList = ocrService.ocrTextAccuracyLocation(
                ocrParam.getImgBase64(),
                ocrParam.getImgType()
        );
        return ResponseResult.success(ocrList);
    }
}
