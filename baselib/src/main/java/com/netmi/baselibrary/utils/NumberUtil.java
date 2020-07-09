package com.netmi.baselibrary.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/28.
 */

public class NumberUtil {

    /**
     * 保留1小数
     */
    public static double roundOneDecimals(double number) {
        BigDecimal bg = new BigDecimal(number);
        double result = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//        double result;
//        int buff = (int) (number * 100);
//        int lastNumber = buff % 10;
//        int intResult = buff / 10;
//        if (lastNumber >= 5) {
//            intResult++;
//        }
//        result = ((double) intResult) / 10;
        return result;
    }

    /**
     * 保留2小数
     */
    public static double roundTwoDecimals(double number) {
        BigDecimal bg = new BigDecimal(number);
        double result = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        double result;
//        int buff = (int) (number * 1000);
//        int lastNumber = buff % 10;
//        int intResult = buff / 10;
//        if (lastNumber >= 5) {
//            intResult++;
//        }
//        result = ((double) intResult) / 100;
        return result;
    }
}
