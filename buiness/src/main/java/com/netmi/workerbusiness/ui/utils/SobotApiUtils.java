package com.netmi.workerbusiness.ui.utils;

import android.content.Context;
import android.text.TextUtils;

import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.workerbusiness.data.entity.mess.CustomerEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.ConsultingContent;
import com.sobot.chat.api.model.Information;

public class SobotApiUtils {


    public static SobotApiUtils getInstance() {
        return SobotApiUtils.SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final SobotApiUtils instance = new SobotApiUtils();
    }


    //跳转到智齿客服页面
    public void toCustomServicePage(Context context, ShopInfoEntity shopInfoEntity, GoodsDetailedEntity goodEntity, CustomerEntity customerEntity) {

        Information information = new Information();
        information.setAppkey("f96f28f095e64d589d5438571d9c272b");
        //设置返回时弹出用户满意度评价
        information.setShowSatisfaction(true);
        //设置要咨询的商品信息
        if (goodEntity != null) {
            ConsultingContent content = new ConsultingContent();
            //咨询内容标题
            content.setSobotGoodsTitle(goodEntity.getTitle());
            //咨询内容图片
            content.setSobotGoodsImgUrl(goodEntity.getImg_url());
            //咨询来源页
            if (customerEntity != null && !TextUtils.isEmpty(customerEntity.getImg())) {
                content.setSobotGoodsFromUrl(customerEntity.getImg());
            } else {
                content.setSobotGoodsFromUrl(Constant.SHARE_GOOD + goodEntity.getItem_id());
            }
            //描述
            content.setSobotGoodsDescribe(goodEntity.getRemark());
            //标签
            content.setSobotGoodsLable(goodEntity.getShowPrice());
            information.setConsultingContent(content);
        }

        //设置用户信息
        if (shopInfoEntity != null) {
            information.setUid(shopInfoEntity.getId());
            information.setUname(shopInfoEntity.getName());
            information.setFace(shopInfoEntity.getLogo_url());
            information.setTel(shopInfoEntity.getShop_tel());
        }

        //转接类型(0-可转入其他客服，1-必须转入指定客服)
        information.setTranReceptionistFlag(0);
        if (customerEntity != null
                && customerEntity.getToken() != null
                && !TextUtils.isEmpty(customerEntity.getToken())) {
            information.setReceptionistId(customerEntity.getToken());
        }

        //1仅机器人 2仅人工 3机器人优先 4人工优先
        information.setInitModeType(3);
        //可转入其它客服
        SobotApi.startSobotChat(context, information);
    }


}

