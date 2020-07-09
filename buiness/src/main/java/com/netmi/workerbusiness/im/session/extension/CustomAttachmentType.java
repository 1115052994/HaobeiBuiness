package com.netmi.workerbusiness.im.session.extension;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public interface CustomAttachmentType {
    // 多端统一
    int Guess = 1;
    int SnapChat = 2;
    int Sticker = 3;
    int RTS = 4;
    int RedPacket = 5;
    int OpenedRedPacket = 6;
    int transfer = 7;
    int OpenedTransfer = 8;

    int CUSTOM_GOOD=501;        //自定义商品
    int CUSTOM_ORDER=502;          //自定义订单
}
