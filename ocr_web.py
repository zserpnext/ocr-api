import json
import logging
from flask import Flask, request, jsonify
from paddleocr import PaddleOCR


def init_log():
    # 设置打印到控制台的格式和等级
    logging.basicConfig(format='%(asctime)s %(filename)s %(levelname)s %(message)s', datefmt='%a %d %b %Y %H:%M:%S',
                        level=logging.INFO)
    # 设置输出到的文件和编码
    file_handler = logging.FileHandler("ocr.log", encoding="utf-8")
    # 设置输出等级
    file_handler.setLevel(logging.INFO)
    # 设置输出到文件的日志格式
    file_handler.setFormatter(logging.Formatter('%(asctime)s %(filename)s %(levelname)s %(message)s'))
    logger = logging.getLogger()
    logger.handlers.append(file_handler)


init_log()

# name 是python中的特殊变量，如果文件作为主程序执行（例如直接执行），那么__name__的值就是__main__,如果是被其它模块引入，那么__name__就是模块名称
app = Flask(__name__)

# 创建一个PaddleOCR对象，使用方向识别器，不使用gpu进行技术，通过cpu进行计算。PaddleOCR我们只需要初始化一次，会将模型加载到内存，会将相关模型下载如果是第一次使用
ocr = PaddleOCR(usr_angle_cls=True, use_gpu=False)


# 通过POST方法识别图片，传入参数为图片的路径
@app.route("/ocr", methods=["POST"])
def learn_post_method():
    try:
        data = json.loads(request.data)
        img_path = data["imgPath"]
        logging.info("ocr imgPath : %s", img_path)
        ocr_result = ocr.ocr(img_path)
        return jsonify({"code": 0, "msg": "ok", "data": ocr_result}), 200
    except Exception as e:
        logging.error("ocr error: %s", str(e))
        ocr_result = {"code": -1, "msg": str(e)}
    return jsonify(ocr_result), 200


if __name__ == '__main__':
    # 可以返回中文字符
    app.config['JSON_AS_ASCII'] = False
    # 接收所有的IP请求，debug=True表示代码修改web容器会重启
    app.run(host='0.0.0.0', debug=True, port=8888)
