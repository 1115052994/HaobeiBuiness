package com.liemi.basemall.presenter;

import com.liemi.basemall.contract.OrderContract;
import com.liemi.basemall.data.api.OrderApi;
import com.liemi.basemall.vo.OrderCommentVo;
import com.liemi.basemall.vo.OrderCommentVo2;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by Bingo on 2018/6/8.
 */

public class OrderPresenterImpl implements OrderContract.Presenter {
    private OrderContract.UpdateOrderStateView updateOrderStateView;

    private OrderContract.OrderCommentView orderCommentView;


    public OrderPresenterImpl setOrderCommentView(OrderContract.OrderCommentView orderCommentView) {
        this.orderCommentView = orderCommentView;
        return this;
    }

    public OrderPresenterImpl setUpdateOrderStateView(OrderContract.UpdateOrderStateView updateOrderStateView) {
        this.updateOrderStateView = updateOrderStateView;
        return this;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        updateOrderStateView = null;
        orderCommentView = null;
    }

    @Override
    public void onError(String message) {
        ToastUtils.showShort(message);
    }

    /**
     * 修改订单状态
     * <p>
     * 业务场景 1： order_remind： 提醒发货 2：order_delete:删除订单,
     * 3：order_cancel：取消订单, 4：order_take：确认收货,
     */
    @Override
    public void updateOrderState(String orderId, String updateType) {
        Observable<BaseData> observable = null;
        switch (updateType) {
            case "order_remind":
                observable = RetrofitApiFactory.createApi(OrderApi.class)
                        .remindOrder(orderId);
                break;
            case "order_delete":
                observable = RetrofitApiFactory.createApi(OrderApi.class)
                        .delOrder(orderId);
                break;
            case "order_cancel":
                observable = RetrofitApiFactory.createApi(OrderApi.class)
                        .cancelOrder(orderId);
                break;
            case "order_take":
                observable = RetrofitApiFactory.createApi(OrderApi.class)
                        .confirmReceipt(orderId);
                break;
        }
        if (observable == null) return;

        updateOrderStateView.showProgress("");
        observable = observable.compose(RxSchedulers.<BaseData>compose());
        if (updateOrderStateView instanceof RxAppCompatActivity) {
            observable = observable.compose(((RxAppCompatActivity) updateOrderStateView).<BaseData>bindUntilEvent(ActivityEvent.DESTROY));
        } else {
            observable = observable.compose(((RxFragment) updateOrderStateView).<BaseData>bindUntilEvent(FragmentEvent.DESTROY));
        }
        observable.subscribe(new XObserver<BaseData>() {
            @Override
            protected void onError(ApiException ex) {
                updateOrderStateView.updateOrderStateFailure(ex.getMessage());
            }

            @Override
            public void onSuccess(BaseData data) {
                updateOrderStateView.updateOrderStateSuccess();
            }

            @Override
            public void onFail(BaseData data) {
                updateOrderStateView.updateOrderStateFailure(data.getErrmsg());
            }

            @Override
            public void onComplete() {
                updateOrderStateView.hideProgress();
            }
        });
    }

    /**
     * 订单评论
     *
     * @param content 内容
     * @param imgs    图片
     */
    @Override
    public void orderComment(String orderId, String skuId, float star, String content, List<String> imgs) {
        if (Strings.isEmpty(skuId) || Strings.isEmpty(orderId)) {
            onError("缺少订单相关参数");
            return;
        }
        OrderCommentVo2 base = new OrderCommentVo2();
        List<OrderCommentVo> commentVos = new ArrayList<>();

        OrderCommentVo commentVo = new OrderCommentVo();
        commentVo.setSku_id(skuId);
        commentVo.setStar(star);
        commentVo.setContent(content);
        commentVo.setImg_url(imgs);
        commentVos.add(commentVo);

        base.setOrder_id(orderId);
        base.setList(commentVos);
        orderCommentView.showProgress("");
        RetrofitApiFactory.createApi(OrderApi.class)
                .orderComment(base)
                .compose(RxSchedulers.<BaseData>compose())
                .compose(((RxAppCompatActivity) orderCommentView).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    public void onSuccess(BaseData data) {
                        orderCommentView.orderCommentSuccess();
                    }

                    @Override
                    public void onFail(BaseData data) {
                        orderCommentView.orderCommentFailure(data.getErrmsg());
                    }

                    @Override
                    public void onComplete() {
                        orderCommentView.hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        orderCommentView.orderCommentFailure(ex.getMessage());
                    }
                });
    }

}
