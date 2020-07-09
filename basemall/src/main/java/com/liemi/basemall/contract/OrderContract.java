package com.liemi.basemall.contract;


import com.netmi.baselibrary.presenter.BasePresenter;
import com.netmi.baselibrary.ui.BaseView;

import java.util.List;

/**
 * Created by Bingo on 2018/6/8.
 */

public interface OrderContract {
    interface UpdateOrderStateView extends BaseView {
        void updateOrderStateSuccess();
        void updateOrderStateFailure(String msg);
    }

    interface OrderCommentView extends BaseView {
        void orderCommentSuccess();
        void orderCommentFailure(String msg);
    }

    interface Presenter extends BasePresenter {
        void updateOrderState(String orderId, String updateType);
        void orderComment(String orderId, String skuId, float star, String content, List<String> imgs);
    }
}
