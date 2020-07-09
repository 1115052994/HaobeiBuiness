package com.netmi.baselibrary.utils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/10/24 16:03
 * 修改备注：
 */
public class PageUtil {

    /**
     * 将条数转页数
     *
     * @param page 起始条数
     * @param rows 每页数据
     */
    public static int toPage(int page, int rows) {
        return (int) Math.ceil(page / (double) rows);
    }

    /**
     * 将条数转页数，默认分页条数10
     *
     * @param page 起始条数
     */
    public static int toPage(int page) {
        return (int) Math.ceil(page / 10.0);
    }
}
