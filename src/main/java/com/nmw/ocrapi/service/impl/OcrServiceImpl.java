package com.nmw.ocrapi.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.yitter.idgen.YitIdHelper;
import com.nmw.ocrapi.domain.dto.OcrPoint;
import com.nmw.ocrapi.domain.dto.TextAccuracyDto;
import com.nmw.ocrapi.domain.dto.TextAccuracyLocationDto;
import com.nmw.ocrapi.domain.param.PaddleOcrPostParam;
import com.nmw.ocrapi.exception.ServiceException;
import com.nmw.ocrapi.service.OcrService;
import com.nmw.ocrapi.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.nmw.ocrapi.exception.ExceptionEnum.PADDLE_OCR_ERROR;
import static com.nmw.ocrapi.exception.ExceptionEnum.PADDLE_OCR_TOO_FREQUENTLY;

/**
 * @author :ljq
 * @date :2023/11/9
 * @description: ocr识别service实现
 */
@Slf4j
@Service
public class OcrServiceImpl implements OcrService {

    /**
     * 调用paddle-ocr解析图片成功
     */
    private static final int CODE_PADDLE_OCR_SUCCESS = 0;

    private static final String FORMAT_OCR_POINT_NAME = "point%s";

    /**
     * 保存图片的名称格式
     */
    private final String FORMAT_IMG_NAME = "%s.%s";

    /**
     * 图片的保存路径
     */
    @Value("${ocr.img-path}")
    private String imgPath;

    @Value("#{'${ocr.paddle-ocr-url}'.split(',')}")
    private List<String> paddleUrlList;

    private static AtomicLong paddleCount = new AtomicLong();


    @Value("${sentinel-rule.resource}")
    private String sentinelRuleResource;


    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<String> ocrText(String imgBase64, String imgType) {

        //1.需要将base64编码保存成图片文件
        String imgPath = saveImage(imgBase64, imgType);

        //2.需要调用flask实现的内部ocr接口，通过paddleocr进行图片识别。解析识别结果，返回。
        List<TextAccuracyLocationDto> ocrResultList = ocrImage(imgPath);

        return ocrResultList.stream()
                .map(TextAccuracyLocationDto::getText)
                .collect(Collectors.toList());
    }

    @Override
    public List<TextAccuracyLocationDto> ocrTextAccuracyLocation(String imgBase64, String imgType) {
        //1.需要将base64编码保存成图片文件
        String imgPath = saveImage(imgBase64, imgType);

        //2.需要调用flask实现的内部ocr接口，通过paddleocr进行图片识别。解析识别结果，返回。
        return ocrImage(imgPath);
    }

    /**
     * 将base64字符串保存为图片
     *
     * @param imgBase64 base64编码
     * @param imgType   图片类型
     * @return 图片路径
     */
    private String saveImage(String imgBase64, String imgType) {
        //通过雪花算法获取唯一的自增Id
        long imgId = YitIdHelper.nextId();
        String imgName = String.format(FORMAT_IMG_NAME, imgId, imgType);
        //图片的完整保存路径
        String imgFullPath = String.format(imgPath, imgName);
        //保存图片到服务器
        Base64Utils.base64ToImg(imgBase64, imgFullPath);
        return imgFullPath;
    }

    private List<TextAccuracyLocationDto> ocrImage(String imgPath) {
        /**
         * 1.调用flask实现的接口，进行paddle-ocr图片识别
         * 由于paddle-ocr在普通的部署方式下，无法利用cpu的多核
         * 因此其中的一种解决方案是部署多个paddle-ocr实例
         * 这里通过取余的方式，实现简单的负载均衡，调用多个paddle-ocr实例
         */
        Entry entry = null;
        try {
            entry = SphU.entry(sentinelRuleResource);
            int index = (int) (paddleCount.incrementAndGet() % paddleUrlList.size());
            String ocrResult = restTemplate.postForObject(paddleUrlList.get(index), new PaddleOcrPostParam(imgPath), String.class);
            //2.解析接口返回结果
            JSONObject ocrResultJsonObject = JSON.parseObject(ocrResult);
            Integer code = ocrResultJsonObject.getInteger("code");

            //如果调用paddle-ocr接口没有成功，返回异常信息
            if (code != CODE_PADDLE_OCR_SUCCESS) {
                String errorMsg = ocrResultJsonObject.getString("msg");
                log.error("调用paddle-ocr接口异常，错误信息是：{}", errorMsg);
                //抛出paddle-ocr接口调用失败异常
                throw new ServiceException(PADDLE_OCR_ERROR.getCode(), errorMsg);
            }

            //解析paddle-ocr接口返回的data字段对应的数据
            JSONArray dataJsonArray = ocrResultJsonObject.getJSONArray("data");
            JSONArray jsonArray1 = dataJsonArray.getJSONArray(0);

            List<TextAccuracyLocationDto> textAccuracyLocationDtoList = new ArrayList<>(jsonArray1.size());

            for (int i = 0; i < jsonArray1.size(); i++) {
                JSONArray jsonArray2 = jsonArray1.getJSONArray(i);
                //获取坐标数组
                JSONArray jsonArrayLocation = jsonArray2.getJSONArray(0);
                Map<String, OcrPoint> ocrPointMap = decodeLocation(jsonArrayLocation);

                //获取字符和可信度数组
                JSONArray jsonArrayText = jsonArray2.getJSONArray(1);
                TextAccuracyDto textAccuracyDto = decodeText(jsonArrayText);

                TextAccuracyLocationDto textAccuracyLocationDto = new TextAccuracyLocationDto(
                        textAccuracyDto.getText(),
                        textAccuracyDto.getAccuracy(),
                        ocrPointMap);
                textAccuracyLocationDtoList.add(textAccuracyLocationDto);
            }

            log.info("ocr img path:{} 结果：{}",imgPath,textAccuracyLocationDtoList);

            return textAccuracyLocationDtoList;
        } catch (BlockException ex) {
            log.error("请求paddle-ocr-api 发生限流或熔断", ex);
            throw new ServiceException(PADDLE_OCR_TOO_FREQUENTLY);
        } catch (Exception ex) {
            log.error("对图片进行OCR识别时发生异常",ex);
            //统计一下异常的数量，这样才会触发熔断
            Tracer.trace(ex);
            throw ex;
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }

    }


    /**
     * 解析ocr结果中位置相关的数据
     *
     * @param jsonArrayLocation
     * @return
     */
    private Map<String, OcrPoint> decodeLocation(JSONArray jsonArrayLocation) {
        Map<String, OcrPoint> locationMap = new HashMap<>(jsonArrayLocation.size());

        /**
         * 从左上角的点开始，顺时针解析4个位置点，分别为
         * ocrPoint0,ocrPoint1,ocrPoint2,ocrPoint3
         */
        for (int i = 0; i < 4; i++) {
            JSONArray locationArray = jsonArrayLocation.getJSONArray(i);
            String x = locationArray.getString(0);
            String y = locationArray.getString(1);
            OcrPoint ocrPoint = new OcrPoint(x, y);
            locationMap.put(String.format(FORMAT_OCR_POINT_NAME, i), ocrPoint);
        }

        return locationMap;
    }

    /**
     * 解析ocr结果中文本和识别可信度
     *
     * @param jsonArrayText
     * @return
     */
    private TextAccuracyDto decodeText(JSONArray jsonArrayText) {
        String text = jsonArrayText.getString(0);
        String accuracy = jsonArrayText.getString(1);
        return new TextAccuracyDto(text, accuracy);
    }
}
