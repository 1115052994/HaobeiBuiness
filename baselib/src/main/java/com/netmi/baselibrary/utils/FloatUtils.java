package com.netmi.baselibrary.utils;

import android.text.TextUtils;

import com.bumptech.glide.manager.RequestManagerTreeNode;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 9:53
 * 修改备注：
 */
public class FloatUtils {

    private static DecimalFormat format;

    /**
     * Float 数据保留两位小数
     *
     * @param source
     * @return
     */
    public static String twoDecimal(float source) {
        try {
            if (source - 0 == 0) {
                return "0";
            }
            String p = String.format("%.2f", source);
            return p.replace(".00", "");
        } catch (Exception e) {
            return "0";
        }
    }

    /*
     * 小数保留小数点后五位
     * */
    public static String fiveDecimal(float source) {
        try {
            if (source - 0 == 0) {
                return "0.00000";
            }
            String str = String.format("%.5f", source);
            return str;
        } catch (Exception e) {
            return "0.00000";
        }
    }

    /**
     * 格式化小数点后八位
     *
     * @param source     需要格式化的字符串
     * @param ignoreZero 对0的处理，小数点后不够八位的处理，如果为true，就有几位取几位，如果为false，就使用0代替
     * @return
     */
    public static String eightDecimal(String source, boolean ignoreZero) {
        Logs.i("要转换的数据是：" + source);
        if (TextUtils.isEmpty(source)) {
            Logs.e("formatException:source is null");
            if (ignoreZero) {
                return "0";
            }
            return "0.00000000";
        }
        try {
            double value = Double.parseDouble(source);
            Logs.i("转换成double之后的数据：" + value);
            DecimalFormat format = new DecimalFormat();
            if (ignoreZero) {
                format.applyPattern("#.########");
            } else {
                format.applyPattern("0.00000000");
            }
            Logs.i("转换的结果是：" + format.format(value));
            return format.format(value);
        } catch (NumberFormatException ex) {
            Logs.e("NumberFormatException:" + ex.getMessage());
            if (ignoreZero) {
                return "0";
            }
            return "0.00000000";
        }
    }

    public static String twoDecimal(String source, boolean ignoreZero) {
        if (TextUtils.isEmpty(source)) {
            Logs.e("formatException:source is null");
            if (ignoreZero) {
                return "0";
            }
            return "0.00";
        }
        try {
            double value = Double.parseDouble(source);
            DecimalFormat format = new DecimalFormat();
            if (ignoreZero) {
                format.applyPattern("#.##");
            } else {
                format.applyPattern("0.00");
            }
            return format.format(value);
        } catch (NumberFormatException ex) {
            Logs.e("NumberFormatException:" + ex.getMessage());
            if (ignoreZero) {
                return "0";
            }
            return "0.00";
        }
    }

    /**
     * Float 数据保留两位小数
     *
     * @param source
     * @return float
     */
    public static float twoDecimalFloat(float source) {
        return (float) (Math.round(source * 100)) / 100;
    }

    /**
     * Double 数据保留两位小数
     *
     * @param source
     * @return
     */
    public static String twoDecimal(double source) {
        try {
            if (source - 0 == 0) {
                return "0";
            }
            String p = String.format("%.2f", source);
            return p.replace(".00", "");
        } catch (Exception e) {
            return "0";
        }
    }


    /**
     * Double 数据保留2位小数
     */
    public static String formatDouble(double source) {
        if (format == null) {
            format = new DecimalFormat("###0.00");
            format.setRoundingMode(RoundingMode.HALF_UP);
        }
        return format.format(source);
    }

    public static String formatDouble(String source) {
        return formatDouble(Strings.toDouble(source));
    }


    /**
     * Double 数据保留4位小数
     */
    public static String formatFourDouble(double source) {
        if (format == null) {
            format = new DecimalFormat("###0.0000");
            format.setRoundingMode(RoundingMode.HALF_UP);
        }
        return format.format(source);
    }

    /**
     * Double 数据保留4位小数
     */
    public static String formatFour(double source) {
        if (format == null) {
            format = new DecimalFormat("###0.0000");
            format.setRoundingMode(RoundingMode.HALF_UP);
        }
        return format.format(source);
    }

    /**
     * Double 数据保留5位小数
     */
    public static String formatFive(double source) {
        if (format == null) {
            format = new DecimalFormat("###0.00000");
            format.setRoundingMode(RoundingMode.HALF_UP);
        }
        return format.format(source);
    }

    public static String formatFour(String source) {
        return formatFour(Strings.toDouble(source));
    }

    public static String formatMoney(double source) {
        return "¥" + formatDouble(source);
    }

    public static String formatMoneydel(double source) {
        return "—¥" + formatDouble(source);
    }

    public static String formatMoney(String source) {
        return "¥" + formatDouble(source);
    }

    public static String formatMoneyYms(String source) {
        return formatDouble(source);
    }

    public static String formatFourMoney(String source) {
        return formatFour(source) + "YMS";
    }

    public static String formatRMB(String source) {
        return "¥" + formatDouble(source);
    }

    public static String formatMoneyGoodDetailPrice(String source) {
        return formatDouble(source);
    }

    public static String formatScore(String source) {
        if (TextUtils.isEmpty(source))
            return "0贝";
        return String.format("%s贝", source);
    }

    /*如果只有积分：100贝
     * 如果只有价钱：¥100
     * 积分和价钱： 100贝 + ¥100
     * */
    public static String formatResult(String pay_score, String pay_amount) {
        if (Strings.toLong(pay_score) > 0 && Strings.toDouble(pay_amount) > 0) {
            return FloatUtils.formatMoney(pay_amount) + "+" + FloatUtils.formatScore(pay_score);
        } else if (Strings.toLong(pay_score) > 0) {
            return FloatUtils.formatScore(pay_score);
        } else {
            return FloatUtils.formatMoney(pay_amount);
        }
    }

    //String转Float
    public static float string2Float(String value) {
        if (Strings.isEmpty(value)) {
            return 0;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
