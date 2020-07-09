package com.netmi.workerbusiness.ui.home;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.data.entity.home.TeamEntity;
import com.netmi.workerbusiness.data.entity.home.offlineorder.OfflineOrderListEntity;
import com.netmi.workerbusiness.data.event.LocationEvent;
import com.netmi.workerbusiness.data.event.TeamEvent;
import com.netmi.workerbusiness.databinding.FragmentListBinding;
import com.netmi.workerbusiness.databinding.ItemOfflineOrderViewBinding;
import com.netmi.workerbusiness.ui.home.offline.OfflineEvaluationActivity;
import com.netmi.workerbusiness.ui.home.offline.OfflineOrderDetailActivity;
import com.netmi.workerbusiness.ui.home.offline.OfflineOrderViewFragment;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;

import static android.content.Intent.getIntent;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/14
 * 修改备注：
 */
public class TeamFragment extends BaseXRecyclerFragment<FragmentListBinding, TeamEntity> {

    private static final String TAG = OfflineOrderViewFragment.class.getName();
    public static final String TYPE = "type";  //type 1是个人 2是店铺
    public static final String FANSUID = "fansuid";  //粉丝的uid
    private int type;
    private String uid;

    //打开下级的最大层级
    private int maxCount = 2;

    //实例化
    public static TeamFragment newInstance(int type, String uid) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putString(FANSUID, uid);
        TeamFragment fragment = new TeamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.fragment_list;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        type = getArguments().getInt(TYPE);
        uid = getArguments().getString(FANSUID);
        initRecyclerView();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<TeamEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_team;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<TeamEntity>(binding) {

                    @Override
                    public void bindData(TeamEntity item) {
                        super.bindData(item);//不能删

                    }

                    @Override
                    public void doClick(View view) {
                        int count = 0;
                        for (Activity activity : AppManager.getActivityStack()) {
                            if (activity.getClass().equals(TeamFragment.this.getClass())) {
                                count++;
                            }
                        }
                        if (count < maxCount) {
                            EventBus.getDefault().post(new TeamEvent(getItem(position).getUid()));
                            TeamActivity.start(getContext(), getItem(position).getUid());
                        }
                    }
                };
            }
        };

        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setLoadingListener(this);
    }

    @Override
    protected void doListData() {
        //显示我的成员信息
        RetrofitApiFactory.createApi(CouponApi.class)
                .doTeam(PageUtil.toPage(startPage), 10, uid, String.valueOf(type))
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new XObserver<BaseData<PageEntity<TeamEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<TeamEntity>> baseData) {
                        if (LOADING_TYPE == Constant.PULL_REFRESH) {
//                            otherData = baseData.getData();
//                            mBinding.setModel(otherData);
                            mBinding.executePendingBindings();
                        }
                        showData(baseData.getData());
                        hideProgress();
                    }
                });
    }

}
