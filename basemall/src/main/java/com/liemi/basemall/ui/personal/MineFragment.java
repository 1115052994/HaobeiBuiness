package com.liemi.basemall.ui.personal;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.OrderCountEntity;
import com.liemi.basemall.data.entity.UserNoticeEntity;
import com.liemi.basemall.data.entity.user.CoinEntity;
import com.liemi.basemall.data.event.BackToAppEvent;
import com.liemi.basemall.databinding.FragmentMineBinding;
import com.liemi.basemall.ui.MainActivity;
import com.liemi.basemall.ui.login.LoginHomeActivity;
import com.liemi.basemall.ui.login.SetPayPasswordActivity;
import com.liemi.basemall.ui.personal.address.AddressManageActivity;
import com.liemi.basemall.ui.personal.collection.MineCollectionActivity;
import com.liemi.basemall.ui.personal.coupon.MineCouponActivity;
import com.liemi.basemall.ui.personal.digitalasset.TradePropertyActivity;
import com.liemi.basemall.ui.personal.helperservice.HelperServiceListActivity;
import com.liemi.basemall.ui.personal.setting.SettingActivity;
import com.liemi.basemall.ui.personal.userinfo.UserInfoActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.data.Constant.ORDER_STATE;
import static com.netmi.baselibrary.data.Constant.ORDER_WAIT_COMMENT;
import static com.netmi.baselibrary.data.Constant.ORDER_WAIT_PAY;
import static com.netmi.baselibrary.data.Constant.ORDER_WAIT_RECEIVE;
import static com.netmi.baselibrary.data.Constant.ORDER_WAIT_SEND;
import static com.netmi.baselibrary.router.BaseRouter.App_MineOrderActivity;
import static com.netmi.baselibrary.router.BaseRouter.App_MineOrderRefundActivity;

/**
 * 类描述：首页----我的
 * 创建人：Simple
 * 创建时间：2018/8/8 11:05
 * 修改备注：
 */
public class MineFragment extends BaseFragment<FragmentMineBinding> {

    public static final String TAG = MineFragment.class.getName();
    private UserInfoEntity mUserInfoEntity;
    private BaseRViewAdapter<CoinEntity,BaseViewHolder> adapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initUI() {
        mBinding.setDoClick(this);
        if (getActivity() instanceof MainActivity) {
            mBinding.vStatusBar.setVisibility(View.VISIBLE);
        }
        mBinding.tvTitlebarTitle.setText("我的");
        adapter = new BaseRViewAdapter<CoinEntity, BaseViewHolder>(getActivity()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_mine_wallet;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        if(position > 0){
                            showError("敬请期待");
                            return;
                        }
                        if(mUserInfoEntity.getIs_set_paypassword() != UserInfoEntity.BIND){
                            showError("请先设置交易密码");
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(SetPayPasswordActivity.PREVIEW_TRADE_INFO,items.get(position));
                            JumpUtil.overlay(getActivity(),SetPayPasswordActivity.class,bundle);
                            return;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(TradePropertyActivity.PREVIEW_COIN_INFO,items.get(position));
                        JumpUtil.overlay(getActivity(),TradePropertyActivity.class,bundle);
                    }
                };
            }

        };

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
            doGetUserInfo();
            doGetOrderCount();
            if(AccessTokenCache.get() != null && !TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
                doListData();
            }else{
                mBinding.setShowUnReadMessage(false);
            }
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        //点击消息图标
        if (i == R.id.iv_message) {
            if(MApplication.getInstance().checkUserIsLogin()) {
                JumpUtil.overlay(getActivity(), MessageActivity.class);
            }
            return;
        }
        //点击设置按钮
        if (i == R.id.iv_setting) {
            JumpUtil.overlay(getActivity(), SettingActivity.class);
            return;
        }

        //点击我的收藏
        if(i == R.id.ll_mine_collection){
            JumpUtil.overlay(getActivity(),MineCollectionActivity.class);
            return;
        }

        //点击客服
        if(i == R.id.ll_link_help){
            JumpUtil.overlay(getActivity(),HelperServiceListActivity.class);
            return;

        }

        if (i== R.id.ll_mine_address_manager){
            JumpUtil.overlay(getContext(),AddressManageActivity.class);
            return;
        }

        //点击优惠券
        if(i == R.id.ll_mine_coupon){
            JumpUtil.overlay(getActivity(),MineCouponActivity.class);
        }

        if (i == R.id.tv_all_order) {
            //全部订单
            ARouter.getInstance().build(App_MineOrderActivity).navigation();

        } else if (i == R.id.tv_wait_pay) {
            //待付款状态的订单
            startOrder(ORDER_WAIT_PAY);

        } else if (i == R.id.tv_wait_send) {
            //待发货状态的订单
            startOrder(ORDER_WAIT_SEND);
            return;
        } else if (i == R.id.tv_wait_receive) {
            //待收货状态的订单
            startOrder(ORDER_WAIT_RECEIVE);

        } else if (i == R.id.tv_wait_comment)

        {
            //待评价状态的订单
            startOrder(ORDER_WAIT_COMMENT);

        } else if (i == R.id.tv_wait_refund)

        {
            if (AppUtils.hasFunction()) return;

            //退款状态的订单
            ARouter.getInstance().build(App_MineOrderRefundActivity).navigation();

        }

    }

    private void startOrder(int state) {
        ARouter.getInstance()
                .build(App_MineOrderActivity)
                .withInt(ORDER_STATE, state)
                .navigation();
    }

    //设置区块链的数据
    private void setCoinInfo(UserInfoEntity userInfoEntity){
        List<CoinEntity> coinEntityList = new ArrayList<>();
        CoinEntity coinEntity = new CoinEntity("ETH",R.mipmap.icon_block_eth,userInfoEntity.getEth_remain(),userInfoEntity.getEth_cny());
        coinEntityList.add(coinEntity);
        coinEntity = new CoinEntity("BTC",R.mipmap.icon_block_btc,"0","0");
        coinEntityList.add(coinEntity);
        coinEntity = new CoinEntity("EOS",R.mipmap.icon_block_eos,"0","0");
        coinEntityList.add(coinEntity);
        coinEntity = new CoinEntity("DAG",R.mipmap.icon_block_dag,"0","0");
        coinEntityList.add(coinEntity);
        adapter.setData(coinEntityList);
    }


    //获取用户信息
    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .compose((this).<BaseData<UserInfoEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        if(data.getData().getIs_bind_phone() == UserInfoEntity.UNBIND){
                            //没有绑定手机号
                            startActivity(new Intent(getActivity(),LoginHomeActivity.class));
                            return;
                        }
                        UserInfoCache.put(data.getData());
                        mUserInfoEntity = data.getData();
                        mBinding.setItem(data.getData());
                        setCoinInfo(data.getData());
                        mBinding.executePendingBindings();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

    private void doGetOrderCount() {
        RetrofitApiFactory.createApi(MineApi.class)
                .getOrderCount(0)
                .compose(RxSchedulers.<BaseData<OrderCountEntity>>compose())
                .compose((this).<BaseData<OrderCountEntity>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<OrderCountEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<OrderCountEntity> data) {
                        mBinding.setOrder(data.getData());
                        mBinding.executePendingBindings();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //请求公告列表
    private void doListData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserNotices(new String[]{"1"},null,0,10)
                .compose(RxSchedulers.<BaseData<UserNoticeEntity>>compose())
                .compose((this).<BaseData<UserNoticeEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserNoticeEntity>>() {

                    @Override
                    public void onSuccess(BaseData<UserNoticeEntity> data) {
                        Logs.i("未读消息："+data.getData().getRead_data().getAll_no_readnum());
                        if(data.getData().getRead_data().getAll_no_readnum() > 0){
                            mBinding.setShowUnReadMessage(true);
                        }else{
                            mBinding.setShowUnReadMessage(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();

                    }
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());

                    }
                });

    }
}
