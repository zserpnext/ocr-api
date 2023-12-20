package com.nmw.ocrapi.util;

import com.nmw.ocrapi.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import static com.nmw.ocrapi.exception.ExceptionEnum.OCR_BASE64_SAVE_TO_IMG_ERROR;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description: Base64转图片
 */
@Slf4j
public class Base64Utils {

    /**
     * 将base64字符串，转换成图片，保存到指定位置
     *
     * @param base64  图片的base64字符串
     * @param imgPath 保存的指定位置
     */
    public final static void base64ToImg(String base64, String imgPath) {
        byte[] bytes = Base64.getDecoder().decode(base64);
        try {
            FileUtils.writeByteArrayToFile(new File(imgPath), bytes);
            log.info("base64转换成图片成功 {}", imgPath);
        } catch (IOException e) {
            log.error("{} 转换成图片识别，error:", base64, e);
            throw new ServiceException(OCR_BASE64_SAVE_TO_IMG_ERROR.getCode(), OCR_BASE64_SAVE_TO_IMG_ERROR.getMessage());
        }
    }
}
