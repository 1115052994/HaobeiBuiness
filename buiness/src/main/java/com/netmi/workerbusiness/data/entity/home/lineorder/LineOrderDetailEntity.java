package com.netmi.workerbusiness.data.entity.home.lineorder;

import com.liemi.basemall.data.entity.AddressEntity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.ResourceUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/23
 * 修改备注：
 */
public class LineOrderDetailEntity extends LineOrderListEntity implements Serializable {
    /**
     * 订单详情信息
     * 继承自订单信息，比订单信息多了一个发票信息
     */


    private int statusImage;//状态图片
    private String formatPayChannel;
    private MineOrderInvoiceEntity orderInvoice;//发票信息
    private AddressEntity addressEntity;//地址信息，根据请求到的数据拼接一个完整的地址信息
    private int display_price;//不希望看到带有价格的订单


    public AddressEntity getAddressEntity() {
        if (addressEntity == null) {
            addressEntity = new AddressEntity();
        }
        if (!Strings.isEmpty(to_tel)) {
            addressEntity.setTel(to_tel);
        }
        if (!Strings.isEmpty(to_name)) {
            addressEntity.setName(to_name);
        }
        addressEntity.setFull_name(to_address);
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    public int getStatusImage() {
        Logs.i("状态图片：" + status);
        switch (status) { //0-未付款1-待发货2-待收货3-待评价4-退货申请
            // 5-退货申请驳回6-退款退货中7-已退货8-取消交易9-交易完成10-支付失败
            case Constant.ORDER_WAIT_PAY:
                return R.mipmap.sharemall_ic_order_wait_receive;
            case Constant.ORDER_WAIT_SEND:
                return R.mipmap.sharemall_ic_order_wait_receive;
            case Constant.ORDER_WAIT_RECEIVE:
                return R.mipmap.sharemall_ic_order_wait_receive;
            case Constant.ORDER_WAIT_COMMENT:
                return R.mipmap.sharemall_ic_order_wait_comment;
            case Constant.ORDER_CANCEL:
                return R.mipmap.sharemall_ic_order_cancel;
            default:
                return R.mipmap.sharemall_ic_order_finish;
        }
    }


    //返回支付方式文字

    public String getFormatPayChannel() {
        switch (pay_channel) {
            case "0":
                return ResourceUtil.getString(R.string.sharemall_wx_pay);
            case "1":
                return ResourceUtil.getString(R.string.sharemall_ali_pay);
            case "2":
                return ResourceUtil.getString(R.string.sharemall_apple_pay);
            case "3":
                return ResourceUtil.getString(R.string.sharemall_integral_pay);
            case "4":
                return ResourceUtil.getString(R.string.sharemall_eth);
            case "8":
                return ResourceUtil.getString(R.string.sharemall_union_pay);
            case "10":
                return ResourceUtil.getString(R.string.sharemall_balance_payment);
        }
        return formatPayChannel;
    }

    public void setFormatPayChannel(String formatPayChannel) {
        this.formatPayChannel = formatPayChannel;
    }

    public void setStatusImage(int statusImage) {
        this.statusImage = statusImage;
    }

    public int getDisplay_price() {
        return display_price;
    }

    public void setDisplay_price(int display_price) {
        this.display_price = display_price;
    }

    public MineOrderInvoiceEntity getOrderInvoice() {
        return orderInvoice;
    }

    public void setOrderInvoice(MineOrderInvoiceEntity orderInvoice) {
        this.orderInvoice = orderInvoice;
    }

}

