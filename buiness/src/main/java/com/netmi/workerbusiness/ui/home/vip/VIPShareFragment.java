package com.netmi.workerbusiness.ui.home.vip;

import android.os.Bundle;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.VIPShareImgEntity;
import com.netmi.workerbusiness.databinding.LayouShareItemVipBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/13
 * 修改备注：
 */
public class VIPShareFragment extends BaseFragment<LayouShareItemVipBinding> {
    public static final String SHARE_TYPE = "share_type";
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static VIPShareFragment newInstance(int type) {
        VIPShareFragment f = new VIPShareFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SHARE_TYPE, type);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected int getContentView() {
        return R.layout.layou_share_item_vip;
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        doGetImgUrl();
    }

    private void doGetImgUrl() {
        showProgress("");
        Observable<BaseData<VIPShareImgEntity>> observable;

        if (getArguments().getInt(SHARE_TYPE) == 1) {  //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
            observable = RetrofitApiFactory.createApi(MineApi.class)
                    .VIPInviteFriend(null);
        } else {
            observable = RetrofitApiFactory.createApi(MineApi.class)
                    .getStoreSharePoster(null);
        }
        observable.compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<VIPShareImgEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<VIPShareImgEntity> data) {
                        String img_url = data.getData().getImg_url();
                        setImage(img_url);
                        GlideShowImageUtils.displayNetImage(getContext(), img_url, mBinding.ivBanner,
                                R.drawable.baselib_bg_null);

                    }
                });

    }

}
