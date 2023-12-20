该项目主要技术栈是SpringBoot+PaddleOCR,实现了 **文本识别、车牌识别、身份证识别**等功能。

1.该项目是OCR文字识别零基础实战教程对应的API实现，当然完全也可以独立部署使用（相关paddleocr运行环境需要搭建）。对应B站视频教程：<https://www.bilibili.com/video/BV1RK411b7DU/?vd_source=b4307343204f5c0271966f7fe276f0eb> 感兴趣的小伙伴不妨关注点个赞

2.项目使用了 jwt token实现身份认证、sentinel熔断限流、参数校验、全局异常处理

3.[ocr\_web.py](https://github.com/CoderBigL/ocr-api/blob/main/ocr_web.py "ocr_web.py") 为python flask 实现的内部ocr api，SpringBoot服务内部调用改接口实现ocr功能。 需要先通过 "python [cr\_web.py](https://github.com/CoderBigL/ocr-api/blob/main/ocr_web.py "ocr_web.py")" 运行代码

4.项目中 appKey和appSecret都是写死的android\_client,真正应该到项目中，为了安全这儿可能需要改需要申请的形式。
