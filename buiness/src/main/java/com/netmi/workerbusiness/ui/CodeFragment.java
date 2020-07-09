package com.netmi.workerbusiness.ui;

import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.FragmentCodeBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/21
 * 修改备注：
 */
public class CodeFragment extends BaseFragment<FragmentCodeBinding> {
    public static final String TAG = CodeFragment.class.getName();

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.fragment_code;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        initImmersionBar();
        ((TextView) mBinding.getRoot().findViewById(R.id.tv_title)).setText("收款码");
        mBinding.refreshView.setOnRefreshListener(this::onRefresh);
    }

    public void onRefresh() {
        doGetShopInfo();
        doGetUserInfo();
        mBinding.refreshView.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
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
                .statusBarDarkFont(true)
                .init();
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        doGetShopInfo();
        doGetUserInfo();
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        mBinding.setLogoUrl(data.getData().getLogo_url());
                        getCode(data.getData().getId());
                    }
                });
    }

    private void doGetUserInfo() {
        RetrofitApiFactory.createApi(com.liemi.basemall.data.api.MineApi.class)
                .getUserInfo(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        mBinding.setId("ID:" + data.getData().getShare_code());
                    }
                });
    }

    private void getCode(String shop_id) {
        RetrofitApiFactory.createApi(WalletApi.class)
                .getCode(shop_id)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<String>>() {
                    @Override
                    public void onSuccess(BaseData<String> data) {
                        mBinding.setCode(data.getData());
                    }
                });
    }
}
