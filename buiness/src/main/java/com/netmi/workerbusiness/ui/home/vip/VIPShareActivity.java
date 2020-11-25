package com.netmi.workerbusiness.ui.home.vip;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.SlidingTextTabLayout;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.FileDownloadContract;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.api.VipParam;
import com.netmi.workerbusiness.data.entity.VIPShareImgEntity;
import com.netmi.workerbusiness.databinding.ActivityVipshareBinding;
import com.netmi.workerbusiness.presenter.FileDownloadPresenterImpl;
import com.netmi.workerbusiness.share.ShareUtilsTop;
import com.netmi.workerbusiness.utils.ControlShareUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class VIPShareActivity extends BaseActivity<ActivityVipshareBinding> implements PlatformActionListener, FileDownloadContract.View {
    private String url;
    private String type;       //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
    private ShareUtilsTop shareUtilsTop;
    private String inviteCode;
    private List<String> imageList;
    private List<String> image = new ArrayList<>();
    private ArrayList<Fragment> fragmentList;

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_vipshare;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra("title"));
        String[] titles = new String[]{getString(R.string.sharemall_vip_invite_friend),
                getString(R.string.sharemall_vip_invite_shop)};
        fragmentList = new ArrayList<>();
        fragmentList.add(VIPShareFragment.newInstance(1));
        fragmentList.add(VIPShareFragment.newInstance(2));
        mBinding.vp.setOffscreenPageLimit(2);
        mBinding.vp.setAdapter(new SlidingTextTabLayout.InnerPagerAdapter(getSupportFragmentManager(), fragmentList, titles));
        mBinding.stlTitle.setViewPager(mBinding.vp);
        mBinding.vp.setCurrentItem(0);
        mBinding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fragmentList != null && !fragmentList.isEmpty()) {
                    url = ((VIPShareFragment) fragmentList.get(position)).getImage();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        basePresenter = new FileDownloadPresenterImpl(this, getContext());
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        type = getIntent().getStringExtra(VipParam.shareType);
        //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
        shareUtilsTop = new ShareUtilsTop();
        doGetImgUrl();
    }

    private void shareToPlatform(String platformName) {
        if(platformName.equals(Wechat.NAME)){
            shareUtilsTop.weiXin(VIPShareActivity.this,url,"客商e宝","客商e宝",url,0);

        }else if(platformName.equals(WechatMoments.NAME)){
            shareUtilsTop.weixinCircle(VIPShareActivity.this,url,"客商e宝","客商e宝",url,0);
        } else {
            Toast.makeText(getContext(), "请先初始化数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//        doResult(1, "");
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
    }

    private void doResult(int resultCode, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        doResult(-1, "分享失败了T.T");
    }

    @Override
    public void onCancel(Platform platform, int i) {
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_save_pic) {
            if (!TextUtils.isEmpty(url)) {
                new RxPermissions(getActivity()).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    imageList = new ArrayList<>();
                                    imageList.add(url);
                                    ((FileDownloadPresenterImpl) basePresenter).doDownloadFiles(imageList);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                } else {
                                    // 用户拒绝了该权限，并且选中『不再询问』
                                }
                            }
                        });
            } else {
                ToastUtils.showShort("没有可保存的图片");
            }

        } else if (i == R.id.tv_share_moment) {
            showProgress("");
            ControlShareUtil.controlWechatMomentsShare(false);
            shareToPlatform(WechatMoments.NAME);
//            showError("敬请期待");
        } else if (i == R.id.tv_share_wechat) {
            showProgress("");
            ControlShareUtil.controlWechatShare(false);
            shareToPlatform(Wechat.NAME);
//            showError("敬请期待");
        } else if (i == R.id.tv_copy) {
            KeyboardUtils.putTextIntoClip(getContext(), inviteCode);
        }
    }

    private void doGetImgUrl() {
        showProgress("");
        Observable<BaseData<VIPShareImgEntity>> observable;

        if ("1".equals(type)) {  //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
            observable = RetrofitApiFactory.createApi(MineApi.class)
                    .VIPInviteFriend(null);
        } else {
            observable = RetrofitApiFactory.createApi(MineApi.class)
                    .getStoreSharePoster(null);
        }
        observable.compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<VIPShareImgEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<VIPShareImgEntity> data) {
                        if ("1".equals(type)) { //1:邀请好友海报接口 2:获取店铺分享海报 3:分享收益海报
                            mBinding.rlInviteCode.setVisibility(View.VISIBLE);
                            inviteCode = data.getData().getInvite_code();
                            mBinding.tvInviteCode.setText(inviteCode);
                        }
                        url = data.getData().getImg_url();
                        /*GlideShowImageUtils.displayNetImage(getContext(), url, mBinding.ivShare,
                                R.drawable.baselib_bg_null);*/

                    }
                });

    }

    @Override
    public void fileDownloadResult(List<String> imgUrls) {
        if (imgUrls != null) {
            ToastUtils.showShort("图片已保存至相册");
        } else {
            ToastUtils.showShort("没有要保存的图片");
        }
    }

    @Override
    public void fileDownloadProgress(int percentage) {

    }

    @Override
    public void fileDownloadFail(String errMsg) {
        ToastUtils.showShort(errMsg);
    }
}
