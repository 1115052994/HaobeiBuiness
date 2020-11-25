package com.netmi.workerbusiness.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.api.WalletApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.FragmentCodeBinding;
import com.netmi.workerbusiness.ui.utils.SetImg;
import com.sobot.chat.utils.ScreenUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/4/21
 * 修改备注：
 */
public class CodeFragment extends BaseFragment<FragmentCodeBinding> {
    public static final String TAG = CodeFragment.class.getName();

    RelativeLayout estab;
    private String logo_url,image_code;

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
        mBinding.tvSetting.setText("立牌打印");
        mBinding.tvSetting.setVisibility(View.VISIBLE);
        mBinding.setDoClick(this::doClick);
        estab = LinearLayout.inflate(getActivity(), R.layout.establish_brand_printing, null).findViewById(R.id.establish_relayout);

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



    public void doClick(View view){
        if(view.getId() == R.id.tv_setting){
            //establish_brand_printing  立牌

            //获取屏幕宽和高
            int screenWidth = ScreenUtils.getScreenWidth(getActivity());

            if(!logo_url.isEmpty()&&!image_code.isEmpty()){
                SetImg.saveBitmap(getViewBitmap(estab, screenWidth, ScreenUtils.dip2px(getActivity(),683)),getActivity());
            }else {
                showError("图片加载失败 请重试");
            }

        }

    }



    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        logo_url = data.getData().getLogo_url();
                        mBinding.setLogoUrl(data.getData().getLogo_url());
                        GlideShowImageUtils.displayCircleNetImage(getContext(), logo_url, estab.findViewById(R.id.iv_head), R.drawable.baselib_bg_default_circle_pic);
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
                        ((TextView)estab.findViewById(R.id.tv_id)).setText("ID:" + data.getData().getShare_code());
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
                        image_code = data.getData();
                        GlideShowImageUtils.displayNetImage(getActivity(), image_code, estab.findViewById(R.id.iv_code), com.netmi.baselibrary.R.drawable.baselib_bg_default_circle_pic);
                    }
                });
    }

    //如果出现图片只截取了上面的一部分   那么你就需要计算控件自适应的高度了
    public static Bitmap getViewBitmap(ViewGroup v, int width, int height) {
        int h = 0;
        //测量使得view指定大小
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        v.measure(measuredWidth, measuredHeight);
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        //获取当前控件的所以子控件的高
        for (int i =0; i < v.getChildCount(); i++) {
            h += v.getChildAt(i).getHeight();
        }
        Bitmap bmp = Bitmap.createBitmap(v.getWidth(), h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bmp;
    }

}
