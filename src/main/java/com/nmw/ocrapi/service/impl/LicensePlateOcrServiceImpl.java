package com.nmw.ocrapi.service.impl;

import com.nmw.ocrapi.exception.ServiceException;
import com.nmw.ocrapi.service.LicensePlateOcrService;
import com.nmw.ocrapi.service.OcrService;
import com.nmw.ocrapi.util.OcrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.nmw.ocrapi.exception.ExceptionEnum.LICENSE_OCR_ERROR;

/**
 * @author :ljq
 * @date :2023/8/8
 * @description:车牌号识别
 */
@Service
public class LicensePlateOcrServiceImpl implements LicensePlateOcrService {

    @Autowired
    private OcrService ocrService;

    @Override
    public List<String> ocrLicensePlateText(String imgBase64, String imgType) {
        List<String> textList = ocrService.ocrText(imgBase64, imgType);

        //对识别出来的数据，进行处理，过滤掉不是车牌的数据
        List<String> plateTextList = textList.stream()
                .map(text -> text.replace("·",""))
                .filter(text -> OcrUtils.isPlate(text))
                .collect(Collectors.toList());

        //未识别出有效车牌号
        if (CollectionUtils.isEmpty(plateTextList)) {
            throw new ServiceException(LICENSE_OCR_ERROR.getCode(),LICENSE_OCR_ERROR.getMessage());
        }

        return plateTextList;
    }

}
