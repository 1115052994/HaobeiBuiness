package com.liemi.basemall.ui.category;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.CategoryApi;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.UserNoticeEntity;
import com.liemi.basemall.data.entity.category.GoodsOneCateEntity;
import com.liemi.basemall.data.entity.category.GoodsTwoCateEntity;
import com.liemi.basemall.data.event.BackToAppEvent;
import com.liemi.basemall.data.event.UnReadMsgEvent;
import com.liemi.basemall.databinding.FragmentMallCategoryBinding;
import com.liemi.basemall.ui.home.SearchActivity;
import com.liemi.basemall.ui.personal.MessageActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_ID;
import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_NAME;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/8 11:05
 * 修改备注：
 */
public class CategoryFragment extends BaseFragment<FragmentMallCategoryBinding> {

    public static final String TAG = CategoryFragment.class.getName();

    public static final String CATEGORY_ID = "categoryId";

    private BaseRViewAdapter<GoodsOneCateEntity, BaseViewHolder> cateAdapter;

    private BaseRViewAdapter<GoodsTwoCateEntity, BaseViewHolder> childAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_mall_category;
    }

    @Override
    protected void initUI() {
        EventBus.getDefault().register(this);

        mBinding.setDoClick(this);
        mBinding.rvGoodsCate.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvGoodsCate.setAdapter(cateAdapter = new BaseRViewAdapter<GoodsOneCateEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_category_one;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        GoodsOneCateEntity entity = cateAdapter.getItem(position);
                        for (GoodsOneCateEntity cateEntity : cateAdapter.getItems()) {
                            if (cateEntity == entity)
                                cateEntity.setCheck(true);
                            else
                                cateEntity.setCheck(false);
                        }
                        cateAdapter.notifyDataSetChanged();
                        setChildAdapter(entity);
                    }
                };
            }
        });

        mBinding.rvGoods.setPullRefreshEnabled(false);
        mBinding.rvGoods.setLoadingMoreEnabled(false);
        mBinding.rvGoods.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mBinding.rvGoods.setAdapter(childAdapter = new BaseRViewAdapter<GoodsTwoCateEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_category_two;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        Bundle bundle = new Bundle();
                        bundle.putString(MC_ID, childAdapter.getItem(position).getMcid());
                        bundle.putString(MC_NAME, childAdapter.getItem(position).getName());
                        JumpUtil.overlay(getActivity(), CategoryGoodsActivity.class, bundle);
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        doListCategory();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 未读消息标识
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void setRedCircle(UnReadMsgEvent event) {
        if (event != null)
            mBinding.includeTop.tvRedCircle.setVisibility(event.unReadNum > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!loading && cateAdapter.getItemCount() == 0) {
            doListCategory();
        }
        if(AccessTokenCache.get() != null && !TextUtils.isEmpty(AccessTokenCache.get().getToken())) {
            doNoticeData();
        }else{
            mBinding.setShowUnReadMessage(false);
        }
    }

    public void showData(List<GoodsOneCateEntity> pageEntity) {
        if (pageEntity != null
                && !pageEntity.isEmpty()) {
            GoodsOneCateEntity first = pageEntity.get(0);
            first.setCheck(true);
            cateAdapter.setData(pageEntity);
            setChildAdapter(first);
        }
    }

    private void setChildAdapter(GoodsOneCateEntity categoryEntity) {
        if (categoryEntity == null) {
            return;
        }
        childAdapter.setData(categoryEntity.getSecond_category());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.tv_search) {
            JumpUtil.startSceneTransition(getActivity(), SearchActivity.class, null,
                    new Pair<>(view, getString(R.string.transition_name)));

        } else if (i == R.id.iv_message) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                JumpUtil.overlay(getContext(), MessageActivity.class);
            }
        }
    }

    private boolean loading = false;

    private void doListCategory() {
        loading = true;
        RetrofitApiFactory.createApi(CategoryApi.class)
                .listTotalCategory("")
                .compose(RxSchedulers.<BaseData<List<GoodsOneCateEntity>>>compose())
                .compose((this).<BaseData<List<GoodsOneCateEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<List<GoodsOneCateEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        loading = false;
                    }

                    @Override
                    public void onSuccess(BaseData<List<GoodsOneCateEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                        loading = false;
                    }
                });
    }

    private void doNoticeData() {
        RetrofitApiFactory.createApi(MineApi.class)
                .doUserNotices(new String[]{"1"},null,0,10)
                .compose(RxSchedulers.<BaseData<UserNoticeEntity>>compose())
                .compose((this).<BaseData<UserNoticeEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<UserNoticeEntity>>() {

                    @Override
                    public void onSuccess(BaseData<UserNoticeEntity> data) {
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
