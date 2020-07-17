package com.netmi.workerbusiness.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.liemi.basemall.ui.personal.digitalasset.QRCodeScanActivity;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.AfterSaleApi;
import com.netmi.workerbusiness.data.api.LoginApi;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.api.OfflineGoodApi;
import com.netmi.workerbusiness.data.api.VipParam;
import com.netmi.workerbusiness.data.cache.TelCache;
import com.netmi.workerbusiness.data.entity.home.AfterSaleDataEntity;
import com.netmi.workerbusiness.data.entity.home.BusinessOverviewEntity;
import com.netmi.workerbusiness.data.entity.home.LineOrderDataEntity;
import com.netmi.workerbusiness.data.entity.home.OfflineOrderDataEntity;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.FragmentMainBinding;
import com.netmi.workerbusiness.ui.home.aftersales.AfterSalesActivity;
import com.netmi.workerbusiness.ui.home.commodity.category.spec.SelectCategorySpecificationActivity;
import com.netmi.workerbusiness.ui.home.commodity.coupon.CouponActivity;
import com.netmi.workerbusiness.ui.home.commodity.offline.OfflineCommodityListActivity;
import com.netmi.workerbusiness.ui.home.commodity.online.LineCommodityListActivity;
import com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity;
import com.netmi.workerbusiness.ui.home.haibei.HaiBeiExchangeActivity;
import com.netmi.workerbusiness.ui.home.offline.OfflineOrderActivity;
import com.netmi.workerbusiness.ui.home.online.LineOrderActivity;
import com.netmi.workerbusiness.ui.home.vip.VIPShareActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import static com.netmi.workerbusiness.ui.home.aftersales.AfterSalesActivity.ALL;
import static com.netmi.workerbusiness.ui.home.aftersales.AfterSalesActivity.REFUND;
import static com.netmi.workerbusiness.ui.home.aftersales.AfterSalesActivity.RETURN;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CATEGORY_OR_SPECIFICATION;
import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity.FROM_TEMPLE;
import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity.JUMP_FROM;


public class MainFragment extends BaseFragment<FragmentMainBinding> {
    public static final String TAG = MainFragment.class.getName();
    //用户申请入驻审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
    private int shop_apply_status;
    //用户选择商户类型  1:线上 2:线下 3:线上+线下
    private int shop_user_type;

    @Override
    protected int getContentView() {
        return R.layout.fragment_main;
    }

    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        super.onClick(view);
        int id = view.getId();
        /**
         * 经营概况
         * */
        //全部
        if (id == R.id.tv_check_more) {
            args.putInt(JumpUtil.TYPE, shop_user_type);
            JumpUtil.overlay(getContext(), BusinessOverviewActivity.class, args);
            /**
             *扫描
             * */
        } else if (id == R.id.tv_setting) {//扫描二维码
            JumpUtil.startForResult(this, QRCodeScanActivity.class, 1002, null);
            /**
             * 线上订单部分
             * */
        } else if (id == R.id.tv_line_check_more) {  //全部
            args.putInt(JumpUtil.TYPE, LineOrderActivity.ALL);
            JumpUtil.overlay(getContext(), LineOrderActivity.class, args);
        } else if (id == R.id.tv_line_payment) {     //待支付
            args.putInt(JumpUtil.TYPE, LineOrderActivity.PAYMENT);
            JumpUtil.overlay(getContext(), LineOrderActivity.class, args);
        } else if (id == R.id.tv_line_ship) {         //待发货
            args.putInt(JumpUtil.TYPE, LineOrderActivity.SHIP);
            JumpUtil.overlay(getContext(), LineOrderActivity.class, args);
        } else if (id == R.id.tv_line_receipt) {      //待收货
            args.putInt(JumpUtil.TYPE, LineOrderActivity.RECEIPT);
            JumpUtil.overlay(getContext(), LineOrderActivity.class, args);
        } else if (id == R.id.tv_line_evaluate) {     //待评价
            args.putInt(JumpUtil.TYPE, LineOrderActivity.EVALUATE);
            JumpUtil.overlay(getContext(), LineOrderActivity.class, args);
            /**
             * 线下订单部分
             * */
        } else if (id == R.id.tv_outline_check_more) {  //全部
            args.putInt(JumpUtil.TYPE, OfflineOrderActivity.ALL);
            JumpUtil.overlay(getContext(), OfflineOrderActivity.class, args);
        } else if (id == R.id.tv_pending_write_off) {  //待核销
            args.putInt(JumpUtil.TYPE, OfflineOrderActivity.WRITE_OFF);
            JumpUtil.overlay(getContext(), OfflineOrderActivity.class, args);
        } else if (id == R.id.tv_comment) {  //待评价
            args.putInt(JumpUtil.TYPE, OfflineOrderActivity.COMMENT);
            JumpUtil.overlay(getContext(), OfflineOrderActivity.class, args);
        } else if (id == R.id.tv_finished) {  //已完成
            args.putInt(JumpUtil.TYPE, OfflineOrderActivity.FINISH);
            JumpUtil.overlay(getContext(), OfflineOrderActivity.class, args);
            /**
             *售后管理
             * */
        } else if (id == R.id.tv_after_sales || id == R.id.tv_finish) {    //已完成  默认为全部
            args.putInt(JumpUtil.TYPE, ALL);
            JumpUtil.overlay(getContext(), AfterSalesActivity.class, args);
        } else if (id == R.id.tv_return) {    //退货
            args.putInt(JumpUtil.TYPE, RETURN);
            JumpUtil.overlay(getContext(), AfterSalesActivity.class, args);
        }
//        else if (id == R.id.tv_refund) {    //退款
//            args.putInt(JumpUtil.TYPE, REFUND);
//            JumpUtil.overlay(getContext(), AfterSalesActivity.class, args);
//            /**
//             *商品管理
//             * */
//        }
        else if (id == R.id.tv_line_commodity_list||id == R.id.tv_line_commodity_list2) {
            if(shop_user_type==1){
                //线上商品列表
                JumpUtil.overlay(getContext(), LineCommodityListActivity.class);
            }else if(shop_user_type==2){
                //线下商品列表
                JumpUtil.overlay(getContext(), OfflineCommodityListActivity.class);
            }
        } else if (id == R.id.tv_postage_editor) {  //邮费模板
            Bundle bundle = new Bundle();
            bundle.putString(JUMP_FROM, FROM_TEMPLE);
            JumpUtil.overlay(getContext(), PostageEditorActivity.class, bundle);
        } else if (id == R.id.tv_specification) {  //规格编辑
            args.putInt(JumpUtil.TYPE, 2);
            args.putString(CATEGORY_OR_SPECIFICATION, "");
            JumpUtil.overlay(getContext(), SelectCategorySpecificationActivity.class, args);
            /**
             *其他
             * */
        } else if (id == R.id.tv_coupon || id == R.id.tv_coupon2) {    //优惠券
            JumpUtil.overlay(getContext(), CouponActivity.class);
        } else if (id == R.id.tv_financial_statements || id == R.id.tv_financial_statements2) {  //财务报表
            args.putInt(JumpUtil.TYPE, shop_user_type);
            JumpUtil.overlay(getContext(), FinancialStatementsActivity.class, args);
        } else if (id == R.id.tv_team_manage || id == R.id.tv_team_manage2) {  //团队管理
            JumpUtil.overlay(getContext(), TeamActivity.class);
        } else if (id == R.id.tv_haibei_home || id == R.id.tv_haibei_home2) {  //海贝之家
//            JumpUtil.overlay(getContext(), HaibeiHomeActivity.class);
            //该页面同步用户端图表数据
//            JumpUtil.overlay(getContext(), HaibeiSpareActivity.class);
            JumpUtil.overlay(getContext(), HaibeiSpareTabActivity.class);
        } else if (id == R.id.tv_merchant_loan || id == R.id.tv_merchant_loan2) { //商家贷
            JumpUtil.overlay(getContext(), MerchantLoanActivity.class);
        } else if (id == R.id.tv_live) { //商家直播
            showError("敬请期待");
        } else if (id == R.id.tv_invite_friend || id == R.id.tv_invite_friend2) { //邀请好友
            //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
            JumpUtil.overlay(getContext(), VIPShareActivity.class,
                    VipParam.shareType, "1", VipParam.title, getString(R.string.sharemall_vip_invite_friend));
//            showError("敬请期待");
        } else if (id == R.id.view4) {
            //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
            JumpUtil.overlay(getContext(), VIPShareActivity.class,
                    VipParam.shareType, "1", VipParam.title, getString(R.string.sharemall_vip_invite_friend));
        }else if(id == R.id.tv_haibei_convert){
            //海贝兑换
            JumpUtil.overlay(getContext(), HaiBeiExchangeActivity.class);
        }
    }


    @Override
    protected void initUI() {
        initImmersionBar();

        ((TextView) mBinding.getRoot().findViewById(R.id.tv_title)).setText("运营台");
        mBinding.setDoClick(this);
        mBinding.refreshView.setOnRefreshListener(this::onRefresh);
        doGetShopInfo();

        //获取角标数据
        getAfterSaleDate();
        getLineOrderDate();
        getOfflineOrder();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initImmersionBar();
        }
    }

    public void initImmersionBar() {
        ImmersionBar.with(this)
                .reset()
                .statusBarDarkFont(false)
                .init();
    }


    private void setUI() {
        //1:线上 2:线下 3:线上+线下
        if (shop_user_type == 1) {
            mBinding.tvSetting.setVisibility(View.GONE);
            mBinding.llLineOrder.setVisibility(View.GONE);
        } else if (shop_user_type == 2) {
            mBinding.llAfterSale.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {

    }

    public void onRefresh() {
        doGetBusiness();
        //获取角标数据
        getAfterSaleDate();
        getLineOrderDate();
        getOfflineOrder();
//        doGetShopInfo();
        mBinding.refreshView.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == 10002 && data != null) {
            String scanResult = data.getStringExtra("scan_result");
            RetrofitApiFactory.createApi(OfflineGoodApi.class)
                    .check(scanResult)
                    .compose(RxSchedulers.compose())
                    .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                    .subscribe(new XObserver<BaseData>() {
                        @Override
                        public void onSuccess(BaseData data) {
                            showError("核销成功");
                        }

                        @Override
                        public void onFail(BaseData data) {
                            showError(data.getErrmsg());
                        }
                    });
        }
    }

    private void doGetBusiness() {
        RetrofitApiFactory.createApi(LoginApi.class)
                .businessOverview("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<BusinessOverviewEntity>>() {
                    @Override
                    public void onSuccess(BaseData<BusinessOverviewEntity> data) {
                        mBinding.setModel(data.getData());
                    }
                });
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        shop_apply_status = data.getData().getShop_apply_status();
                        shop_user_type = data.getData().getShop_user_type();
                        setUI();
                        if (shop_apply_status != 1) {
                            doGetBusiness();
                        }
                    }
                });


        //只要一个头像的接口
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        mBinding.setModel2(data.getData());
                    }

                    @Override
                    public void onFail(BaseData<ShopInfoEntity> data) {
                        if (data.getErrcode() == 20001) {
                            showError(data.getErrmsg());
                        }
                    }
                });
    }


    private void getAfterSaleDate() {
        RetrofitApiFactory.createApi(AfterSaleApi.class)
                .getAfterSaleData("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<AfterSaleDataEntity>>() {
                    @Override
                    public void onSuccess(BaseData<AfterSaleDataEntity> data) {
                        mBinding.setAfterSaleData(data.getData());
                    }
                });
    }

    private void getLineOrderDate() {
        RetrofitApiFactory.createApi(AfterSaleApi.class)
                .getLineOrderData("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<LineOrderDataEntity>>() {
                    @Override
                    public void onSuccess(BaseData<LineOrderDataEntity> data) {
                        mBinding.setLineOrderData(data.getData());
                    }
                });
    }

    private void getOfflineOrder() {
        RetrofitApiFactory.createApi(AfterSaleApi.class)
                .getOfflineOrderData("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<OfflineOrderDataEntity>>() {
                    @Override
                    public void onSuccess(BaseData<OfflineOrderDataEntity> data) {
                        mBinding.setOfflineOrderData(data.getData());
                    }
                });
    }
}
