package com.netmi.baselibrary.data.event;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/4/7 2:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class SwitchTabEvent {

    //在app中初始化
    public static int defaultRbId;

    public SwitchTabEvent() {
        rbId = defaultRbId;
    }

    public SwitchTabEvent(int id) {
        rbId = id;
    }

    public int rbId;
}
