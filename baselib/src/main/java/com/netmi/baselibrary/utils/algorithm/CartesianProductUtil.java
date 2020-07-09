package com.netmi.baselibrary.utils.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：笛卡尔积工具类
 * 创建人：Sherlock
 * 创建时间：2019/5/9
 * 修改备注：
 */
public class CartesianProductUtil {

    /**
     * 2个List的笛卡尔积算法
     *
     * @param symbol 自定义的连接符号
     */
    public static List<String> listCartesianProduct(List<String> a, List<String> b, String symbol) {
        List<String> result = new ArrayList<>();
        for (String tempA : a) {
            for (String tempB : b) {
                result.add(tempA + symbol + tempB);
            }
        }
        return result;
    }

    /**
     * 未知维度的List的笛卡尔积算法
     *
     * @param symbol 自定义的连接符号
     */
    public static List<String> listCartesianProduct(List<List<String>> lists, String symbol) {
        List<String> result = new ArrayList<>();
        if (lists == null || lists.size() == 0) {
            return result;
        }
        if (lists.size() == 1) {
            return lists.get(0);
        }
        result = lists.get(0);
        for (int i = 1; i < lists.size(); i++) {
            result = listCartesianProduct(result, lists.get(i), symbol);
        }
        return result;
    }
}
