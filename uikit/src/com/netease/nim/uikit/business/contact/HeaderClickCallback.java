package com.netease.nim.uikit.business.contact;

import android.view.View;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/7/3
 * 修改备注：
 */
public interface HeaderClickCallback {
    //系统通知
    int systemNotify = 0;
    //平台公告
    int platformNotice = 1;
    //订单通知
    int orderNotice = 6;
    //个人验证消息
    int verificationMessage = 2;
    //群消息验证
    int verificationTeamMessage = 3;
    //高级群
    int advancedGroups = 4;
    //黑名单
    int blackList = 5;

    void doClick(int type, View view);


}
