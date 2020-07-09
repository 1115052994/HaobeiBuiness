package com.liemi.basemall.ui.home;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.liemi.basemall.R;
import com.liemi.basemall.data.MallConstant;
import com.liemi.basemall.data.api.FloorApi;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.UserNoticeEntity;
import com.liemi.basemall.data.entity.floor.FloorPageEntity;
import com.liemi.basemall.data.entity.floor.NewFloorEntity;
import com.liemi.basemall.data.event.BackToAppEvent;
import com.liemi.basemall.databinding.FragmentMallHomeBinding;
import com.liemi.basemall.ui.personal.MessageActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.StatusBarUtil;
import com.netmi.baselibrary.utils.Strings;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_SEARCH_BAR;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/22 10:49
 * 修改备注：
 */
public class HomePageFragment extends BaseXRecyclerFragment<FragmentMallHomeBinding, NewFloorEntity> {

    public static final String TAG = HomePageFragment.class.getName();

    private int llTopColor;

    private GradientDrawable tvSearchBg;

    private int systemType = -1;

    private TransitionSet mSet;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mall_home;
    }

    @Override
    protected void initUI() {

        mBinding.setDoClick(this);

        StatusBarUtil.setImgTransparent(getActivity());
        tvSearchBg = new GradientDrawable();
        tvSearchBg.setShape(GradientDrawable.RECTANGLE);
        tvSearchBg.setGradientType(GradientDrawable.RECTANGLE);
        tvSearchBg.setCornerRadius(Densitys.dp2px(getContext(), 18));


        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter = new FloorAdapter(getContext(), xRecyclerView).setLifecycleFragment(this));

        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int item = layoutManager.findFirstVisibleItemPosition();
                View itemView = layoutManager.findViewByPosition(item);
                if (itemView != null) {
                    int itemHeight = itemView.getHeight();
                    if (item == 1) {
                        if (mBinding.llTop.getHeight() <= 1) return;
                        float alpha = Math.abs(layoutManager.findViewByPosition(item).getTop()) / mBinding.llTop.getHeight();
                        alpha = alpha > 1 ? 1 : alpha;
                        float whiteAlpha = 1 - alpha;

                        llTopColor = Color.parseColor(getColor((int) (alpha * 255), "ffffff"));
                        mBinding.llTop.setBackgroundColor(llTopColor);

                        if (alpha > 0.5f) {
                            if (systemType == 0) {
                                systemType = StatusBarUtil.StatusBarLightMode(getActivity());
                            }
                            tvSearchBg.setColor(Color.parseColor(getColor((int) (alpha * 255), "f8f8f8")));
                            mBinding.tvSearch.setTextColor(Color.parseColor(getColor((int) (alpha * 255), "999999")));
                            mBinding.llTop.setBackgroundColor(Color.parseColor(getColor(
                                    (int) (alpha * 255), "ffffff")));
                            expand();
                        } else {
                            reduce();
                            if (systemType != 0) {
//                                StatusBarUtil.setImgTransparent(getActivity());
                                StatusBarUtil.StatusBarDarkMode(getActivity(), systemType);
                                systemType = 0;
                            }
                            tvSearchBg.setColor(Color.parseColor(getColor((int) ((whiteAlpha) * 77), "ffffff")));
                            mBinding.tvSearch.setTextColor(Color.parseColor(getColor((int) ((whiteAlpha) * 255), "ffffff")));
                            mBinding.llTop.setBackgroundResource(R.drawable.gradient_home_title);
                        }
                        mBinding.llSearch.setBackground(tvSearchBg);
                        mBinding.ivMessage.setAlpha(whiteAlpha);
                        mBinding.ivMessageBlack.setAlpha(alpha);
                        mBinding.ivSearch.setAlpha(whiteAlpha);
                        mBinding.ivSearchBlack.setAlpha(alpha);
                    }
                }
            }

            private String getColor(int i, String color) {
                String colorAlpha = Integer.toHexString(i);
                if (colorAlpha.length() == 1)
                    colorAlpha = "0" + colorAlpha;
                return "#" + colorAlpha + color;
            }

        });

    }

    private void expand() {
        //设置伸展状态时的布局
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mBinding.llSearch.getLayoutParams();
        LayoutParams.width = getActivity().getResources().getDisplayMetrics().widthPixels;
        LayoutParams.setMargins(dip2px(15), 0, 0, 0);
        mBinding.llSearch.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mBinding.llSearch);
    }

    private void reduce() {
        //设置收缩状态时的布局
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) mBinding.llSearch.getLayoutParams();
        LayoutParams.width = getActivity().getResources().getDisplayMetrics().widthPixels-40;
        LayoutParams.setMargins(dip2px(60), 0, 0, 0);
        mBinding.llSearch.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(mBinding.llSearch);
    }

    void beginDelayedTransition(ViewGroup view) {
        mSet = new AutoTransition();
        mSet.setDuration(600);
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
            doNoticeData();
        }else{
            mBinding.setShowUnReadMessage(false);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (MallConstant.systemType > 0 && systemType <= 0) {
                StatusBarUtil.StatusBarDarkMode(getActivity(), MallConstant.systemType);
                MallConstant.systemType = 0;
            }
        }
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    public void showData(FloorPageEntity<NewFloorEntity> pageEntity) {
        if (LOADING_TYPE == Constant.PULL_REFRESH && pageEntity != null) {
            if (!Strings.isEmpty(pageEntity.getContent().getList())) {
                //搜索栏另外处理
                for (NewFloorEntity floorEntity : pageEntity.getContent().getList()) {
                    if (floorEntity.getType() == FLOOR_SEARCH_BAR) {
                        mBinding.llTop.setVisibility(View.VISIBLE);
                        pageEntity.getContent().getList().remove(floorEntity);
                        pageEntity.setTotal_pages(pageEntity.getTotal_pages() - 1);
                        break;
                    }
                }
            }
        }
        super.showData(pageEntity);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.iv_message || i == R.id.iv_message_black) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                JumpUtil.overlay(getContext(), MessageActivity.class);
            }

        } else if (i == R.id.tv_search) {
            JumpUtil.overlay(getActivity(), SearchActivity.class);
        }else if (i == R.id.ll_seach_arrow){
            EventBus.getDefault().post(new BackToAppEvent());
            getActivity().finish();
        }
    }


    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(FloorApi.class)
                .doListFloors(1, null)
                .compose(RxSchedulers.<BaseData<FloorPageEntity<NewFloorEntity>>>compose())
                .compose((this).<BaseData<FloorPageEntity<NewFloorEntity>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<FloorPageEntity<NewFloorEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<FloorPageEntity<NewFloorEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


    private void doNoticeData(){
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserNotices(new String[]{"1"}, null, 0, 10)
                .compose(RxSchedulers.<BaseData<UserNoticeEntity>>compose())
                .compose((this).<BaseData<UserNoticeEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserNoticeEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserNoticeEntity> data) {
                        if (data.getData().getRead_data().getAll_no_readnum() > 0) {
                            mBinding.setShowUnReadMessage(true);
                        } else {
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
