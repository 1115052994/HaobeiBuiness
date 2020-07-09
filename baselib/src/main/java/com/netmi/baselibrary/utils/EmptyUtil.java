package com.netmi.baselibrary.utils;

import java.util.List;

/**
 * Created by Bingo on 2018/12/11.
 */

public class EmptyUtil {
    public static <T> boolean  isEmpty(List<T> list){
        return list==null || list.isEmpty();
    }
}
