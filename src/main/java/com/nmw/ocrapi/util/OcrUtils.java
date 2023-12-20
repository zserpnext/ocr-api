package com.nmw.ocrapi.util;

/**
 * @author :ljq
 * @date :2023/8/9
 * @description:Ocr识别，相关处理
 */
public final class OcrUtils {

    private static final String PREFIX_CHINESE_ID_NUMBER = "公民身份号码";

    /**
     * 车牌验证
     *
     * @param plate
     * @return
     */
    public static boolean isPlate(String plate) {
        return isCommonPlate(plate) || isNewEnergyVehiclePlate(plate);
    }

    /**
     * <pre>
     * 普通车牌（蓝牌、黄牌）
     * 车牌号码长度：7 位
     * 车牌号码格式：
     * 第1位 第2位    第3位    第4位    第5位    第6位    第7位
     * 省份简称    发证机关代码 号码 号码 号码 号码 号码
     * 省份简称：
     * 京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新
     * 发证机关代码：
     * ABCDEFGHxJKLMNxPQRSTUVWXYx
     * 说明：无 I, O , Z 三个字母。其中O和Z属于特殊车牌类型，见其它类型说明。
     * 车牌号码：
     * 数字：0123456789
     * 字母：ABCDEFGHxJKLNMxPQRSTUVWXY
     *
     * 说明：无 I, O 字母；
     * </pre>
     * 普通车牌
     *
     * @param plate
     * @return
     */
    public static boolean isCommonPlate(String plate) {
        String commonPlatePattern = "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新][ABCDEFGHJKLMNPQRSTUVWXY][\\dABCDEFGHJKLNMPQRSTUVWXYZ]{5}$";
        return plate.matches(commonPlatePattern);
    }

    /**
     * 新能源车牌
     * <pre>
     * 车牌号码长度：8 位
     * 车牌号码格式：
     * 第1位 第2位    第3位    第4位    第5位    第6位    第7位    第8位
     * 省份  发证机关   号码 号码 号码 号码 号码 号码
     * 新能源车牌现阶段号码长度为8位，按照现行新能源车牌号码规则：
     * 第3位：1-9DF
     * 第4位：0-9A-Z，无I、O字母；
     * 第5-7位：0-9
     * 第8位：1-9DF
     *
     * @param plate
     * @return
     */
    public static boolean isNewEnergyVehiclePlate(String plate) {
        String newPattern = "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新][ABCDEFGHJKLMNPQRSTUVWXY][1-9DF][0-9ABCDEFGHJKLMNPQRSTUVWXYZ]\\d{3}[1-9DF]$";
        return plate.matches(newPattern);
    }


    public static String replaceIdNumberPrefix(String idNumber) {
        return idNumber.replace(PREFIX_CHINESE_ID_NUMBER, "");
    }

    public static boolean isIdNumber(String idNumber) {
        /**
         * 参考 https://juejin.cn/post/6844903575877861390
         * 正则规则 https://zhuanlan.zhihu.com/p/28672572?group_id=883065970518790144
         * 去掉 “公民身份号码” 这几个汉字
         */
        if (idNumber.startsWith(PREFIX_CHINESE_ID_NUMBER)) {
            idNumber = idNumber.replace(PREFIX_CHINESE_ID_NUMBER, "");
        }

        /**
         * 以数字1-9开头  5位为0-9的数字   年份：19或20开头,剩余为0-9数字   月份：以0开头，则第二位为1-9，或者为10、11、12   日期：第一位为0-2，第二位为1-9 或 是10，20，30，31    后4位
         * ^[1-9]        \\d{5}         (19|20)\\d{2}                  ((0[1-9])|(1[0-2]))                           (([0-2][1-9])|10|20|30|31)                         \\d{3}[0-9Xx]$
         *
         *                                                             (02)                  二月份最高到29号         ([0-2][1-9])|20
         *
         * 最后是：
         *                                                        除了二月的         月份                和        日期
         * ^[1-9]        \\d{5}         (19|20)\\d{2}             (        ( ((0(1|[3-9])) | (1[0-2]))     (([0-2][1-9])|10|20|30|31) )  |   (02 ([0-2][1-9])|10|20)     )
         *
         */

        //原正则表达式没有办法校验2月的日期超过29号
        String pattern = "^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

        return idNumber.matches(pattern);
    }



}
