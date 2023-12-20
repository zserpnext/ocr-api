package com.nmw.ocrapi.service.impl;

import com.nmw.ocrapi.exception.ServiceException;
import com.nmw.ocrapi.service.IdCardService;
import com.nmw.ocrapi.service.OcrService;
import com.nmw.ocrapi.util.OcrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.nmw.ocrapi.exception.ExceptionEnum.ID_CARD_ORC_ERROR;

/**
 * @author :ljq
 * @date :2023/9/8
 * @description:身份证识别
 */
@Slf4j
@Service
public class IdCardServiceImpl implements IdCardService {

    @Autowired
    private OcrService ocrService;

    @Override
    public List<String> ocrIdNumberText(String imgBase64, String imgType) {
        List<String> textOcrList = ocrService.ocrText(imgBase64, imgType);

        //对识别出来的数据，进行处理，过滤掉不是车牌的数据
        List<String> idNumberTextList = textOcrList.stream()
                .map(text->OcrUtils.replaceIdNumberPrefix(text))
                .filter(text -> OcrUtils.isIdNumber(text))
                .collect(Collectors.toList());

        //未识别出有效身份证号
        if (CollectionUtils.isEmpty(idNumberTextList)) {
            throw new ServiceException(ID_CARD_ORC_ERROR.getCode(), ID_CARD_ORC_ERROR.getMessage());
        }

        return idNumberTextList;
    }
}
