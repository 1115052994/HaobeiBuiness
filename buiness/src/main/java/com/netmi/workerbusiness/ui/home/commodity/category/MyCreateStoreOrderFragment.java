package com.netmi.workerbusiness.ui.home.commodity.category;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity.CreateGoodCommand;
import com.netmi.workerbusiness.data.entity.home.store.GoodShelfListEntity;
import com.netmi.workerbusiness.data.entity.home.store.SpecDetailEntity;
import com.netmi.workerbusiness.data.event.ShelfUpdateEvent;
import com.netmi.workerbusiness.databinding.FragmentXrecyclerviewBinding;
import com.netmi.workerbusiness.databinding.ItemShelfBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.data.Constant.SHELF_PENDING;
import static com.netmi.baselibrary.data.Constant.SHELF_STATE;
import static com.netmi.baselibrary.data.Constant.SHELF_UP;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/4
 * 修改备注：
 */
public class MyCreateStoreOrderFragment extends BaseFragment<FragmentXrecyclerviewBinding> implements XRecyclerView.LoadingListener {

    public final static String SHELF_PRICE_UPDATE = "shelf_price_update";

    /**
     * 页数
     */
    private int startPage = 0;

    /**
     * 总条数
     */
    private int totalCount;

    /**
     * 列表加载数据方式
     */
    private int LOADING_TYPE = -1;

    private BaseRViewAdapter<GoodShelfListEntity, BaseViewHolder> adapter;

    private ArrayList<SpecDetailEntity> results;

    private CreateGoodCommand createGoodCommand;


    @Override
    protected int getContentView() {
        return R.layout.fragment_xrecyclerview;
    }

    public static MyCreateStoreOrderFragment newInstance(String[] state) {
        MyCreateStoreOrderFragment f = new MyCreateStoreOrderFragment();
        Bundle bundle = new Bundle();
        if (state != null) {
            bundle.putStringArray(SHELF_STATE, state);
        }
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initUI() {
        EventBus.getDefault().register(this);
        xRecyclerView = mBinding.xrvData;
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<GoodShelfListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_shelf;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                    @Override
                    public ItemShelfBinding getBinding() {
                        return (ItemShelfBinding)super.getBinding();
                    }

                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        if (TextUtils.equals(getArguments().getStringArray(SHELF_STATE)[0], SHELF_UP)) {
                            getBinding().tvPriceShelf.setVisibility(View.VISIBLE);
                            getBinding().tvEdit.setVisibility(View.GONE);
                            getBinding().llDownShelf.setVisibility(View.VISIBLE);
                            getBinding().llUpShelf.setVisibility(View.GONE);
                            getBinding().tvExtension.setVisibility(View.VISIBLE);
                            getBinding().tvDeleteShelf.setVisibility(View.GONE);
                            getBinding().tvEmptyView.setVisibility(View.GONE);
                        } else if (TextUtils.equals(getArguments().getStringArray(SHELF_STATE)[0], SHELF_PENDING)){
                            getBinding().tvPriceShelf.setVisibility(View.GONE);
                            getBinding().tvEdit.setVisibility(View.GONE);
                            getBinding().llDownShelf.setVisibility(View.VISIBLE);
                            getBinding().llUpShelf.setVisibility(View.GONE);
                            getBinding().tvExtension.setVisibility(View.GONE);
                            getBinding().tvDeleteShelf.setVisibility(View.VISIBLE);
                            getBinding().tvEmptyView.setVisibility(View.INVISIBLE);
                        }else {
                            getBinding().tvPriceShelf.setVisibility(View.GONE);
                            getBinding().tvEdit.setVisibility(View.VISIBLE);
                            getBinding().llUpShelf.setVisibility(View.VISIBLE);
                            getBinding().llDownShelf.setVisibility(View.GONE);
                            getBinding().tvExtension.setVisibility(View.GONE);
                            getBinding().tvDeleteShelf.setVisibility(View.VISIBLE);
                            getBinding().tvEmptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int id = view.getId();
                        if (id == R.id.tv_edit) {
                            doShelfEdit(getItem(position).getItem_id());
                        } else if (id == R.id.tv_up_shelf) {
                            doUpShelf(getItem(position).getItem_id(), position);
                        } else if (id == R.id.tv_down_shelf) {
                            doDownShelf(getItem(position).getItem_id(), position);
                        } else if (id == R.id.tv_delete_shelf) {
                            doDeleteShelf(getItem(position).getItem_id(), position);
                        } else if (id == R.id.tv_extension) {
                            doExtension(position);
                        } else if (id == R.id.tv_price_shelf) {
                            doPrice(getItem(position).getItem_id());
                        }
                    }
                };
            }
        });

    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    public void showData(PageEntity<GoodShelfListEntity> pageEntity) {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(true);
            }
            adapter.setData(pageEntity.getList());
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                adapter.insert(adapter.getItemCount(), pageEntity.getList());
            }
        }
        totalCount = pageEntity.getTotal_pages();
        startPage = adapter.getItemCount();
    }

    @Override
    public void onRefresh() {
        startPage = 0;
        LOADING_TYPE = Constant.PULL_REFRESH;
        xRecyclerView.setLoadingMoreEnabled(false);
        doListShelf();
    }

    @Override
    public void onLoadMore() {
        LOADING_TYPE = Constant.LOAD_MORE;
        doListShelf();
    }

    @Override
    public void hideProgress() {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            xRecyclerView.refreshComplete();
        } else {
            xRecyclerView.loadMoreComplete();
        }
        if (startPage >= totalCount) {
            xRecyclerView.setNoMore(true);
        }
    }

    private String getSearchKey() {
//        if (getActivity() instanceof MyCreateStoreOrderActivity) {
//            return ((MyCreateStoreOrderActivity) getActivity()).getSearchKey();
//        }
        return null;
    }

    private void doListShelf() {
//        RetrofitApiFactory.createApi(StoreApi.class)
//                .getShelfGoodList(getArguments().getStringArray(SHELF_STATE), PageUtil.toPage(startPage), Constant.PAGE_ROWS, getSearchKey())
//                .compose(RxSchedulers.<BaseData<PageEntity<GoodShelfListEntity>>>compose())
//                .compose((this).<BaseData<PageEntity<GoodShelfListEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(new BaseObserver<BaseData<PageEntity<GoodShelfListEntity>>>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData<PageEntity<GoodShelfListEntity>> data) {
//                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
//                            showData(data.getData());
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
    }

    //编辑
    public void doShelfEdit(final String item_id) {
//        RetrofitApiFactory.createApi(StoreApi.class)
//                .getEditGoodDetail(item_id)
//                .compose(RxSchedulers.<BaseData<CreateGoodCommand>>compose())
//                .compose((this).<BaseData<CreateGoodCommand>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(new BaseObserver<BaseData<CreateGoodCommand>>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData<CreateGoodCommand> data) {
//                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
//                            //跳转到创建商品页面并赋值
//                            createGoodCommand = data.getData();
//                            Bundle bundle = new Bundle();
//                            bundle.putString(CREATE_GOOD_FROM, SHELF_LIST);
//                            bundle.putSerializable(CREATE_GOOD_DETAI,createGoodCommand);
//                            bundle.putString(GOOD_ID, item_id);
//                            JumpUtil.overlay(getContext(), CreateGoodActivity.class, bundle);
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
    }

    //上架
    public void doUpShelf(String item_id, final int position) {
//        RetrofitApiFactory.createApi(StoreApi.class)
//                .getUpShelf(item_id)
//                .compose(RxSchedulers.<BaseData>compose())
//                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(new BaseObserver<BaseData>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData data) {
//                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
//                            ToastUtils.showShort("上架成功");
//                            adapter.remove(position);
//                            //通知上架列表刷新
//                            EventBus.getDefault().post(new ShelfUpdateEvent());
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
    }

    //下架
    public void doDownShelf(String item_id, final int position) {
//        RetrofitApiFactory.createApi(StoreApi.class)
//                .getLowerShelf(item_id)
//                .compose(RxSchedulers.<BaseData>compose())
//                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(new BaseObserver<BaseData>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData data) {
//                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
//                            ToastUtils.showShort("下架成功");
//                            adapter.remove(position);
//                            //通知上架列表刷新
//                            EventBus.getDefault().post(new ShelfUpdateEvent());
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
    }

    //删除
    public void doDeleteShelf(String item_id, final int position) {
//        RetrofitApiFactory.createApi(StoreApi.class).
//                getDeleteShelf(item_id)
//                .compose(RxSchedulers.<BaseData>compose())
//                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(new BaseObserver<BaseData>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData data) {
//                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
//                            ToastUtils.showShort("删除成功");
//                            adapter.remove(position);
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });
    }

    //推广
    public void doExtension(int position) {
//        GoodEntity goodEntity = new GoodEntity();
//        GoodShelfListEntity goodShelfListEntity = adapter.getItem(position);
//        goodEntity.setItem_id(goodShelfListEntity.getItem_id());
//        goodEntity.setImg_url(goodShelfListEntity.getImg_url());
//        goodEntity.setTitle(goodShelfListEntity.getTitle());
//        goodEntity.setStock(goodShelfListEntity.getStock());
//        goodEntity.setDeal_num(goodShelfListEntity.getDeal_num());
//        StoreEntity storeEntity = new StoreEntity();
//        StoreEntity.BonusBean bonusBean = new StoreEntity.BonusBean();
//        bonusBean.setType_2_level_0(goodShelfListEntity.getBonus().getType_2_level_0());
//        bonusBean.setType_2_level_1(goodShelfListEntity.getBonus().getType_2_level_1());
//        bonusBean.setType_2_level_2(goodShelfListEntity.getBonus().getType_2_level_2());
//        bonusBean.setType_2_level_3(goodShelfListEntity.getBonus().getType_2_level_3());
//        bonusBean.setType_3_level_0(goodShelfListEntity.getBonus().getType_3_level_0());
//        bonusBean.setType_3_level_1(goodShelfListEntity.getBonus().getType_3_level_1());
//        bonusBean.setType_3_level_2(goodShelfListEntity.getBonus().getType_3_level_2());
//        bonusBean.setType_3_level_3(goodShelfListEntity.getBonus().getType_3_level_3());
//        storeEntity.setBonus(bonusBean);
//        goodEntity.setShop(storeEntity);
//        GoodEntity.BonusTypeLevelBean bonusTypeLevelBean = new GoodEntity.BonusTypeLevelBean();
//        bonusTypeLevelBean.setType_2_level_0(goodShelfListEntity.getBonus_type_level().getType_2_level_0());
//        bonusTypeLevelBean.setType_2_level_1(goodShelfListEntity.getBonus_type_level().getType_2_level_1());
//        bonusTypeLevelBean.setType_2_level_2(goodShelfListEntity.getBonus_type_level().getType_2_level_2());
//        bonusTypeLevelBean.setType_2_level_3(goodShelfListEntity.getBonus_type_level().getType_2_level_3());
//        bonusTypeLevelBean.setType_3_level_0(goodShelfListEntity.getBonus_type_level().getType_3_level_0());
//        bonusTypeLevelBean.setType_3_level_1(goodShelfListEntity.getBonus_type_level().getType_3_level_1());
//        bonusTypeLevelBean.setType_3_level_2(goodShelfListEntity.getBonus_type_level().getType_3_level_2());
//        bonusTypeLevelBean.setType_3_level_3(goodShelfListEntity.getBonus_type_level().getType_3_level_3());
//        goodEntity.setBonus_type_level(bonusTypeLevelBean);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ShareGoodActivity.ENTITY, goodEntity);
//        JumpUtil.overlay(getContext(), ShareGoodActivity.class, bundle);
    }

    //改价
    public void doPrice(final String item_id){
//        RetrofitApiFactory.createApi(StoreApi.class)
//                .getEditGoodDetail(item_id)
//                .compose(RxSchedulers.<BaseData<CreateGoodCommand>>compose())
//                .compose((this).<BaseData<CreateGoodCommand>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribe(new BaseObserver<BaseData<CreateGoodCommand>>() {
//                    @Override
//                    protected void onError(ApiException ex) {
//                        showError(ex.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(@NonNull BaseData<CreateGoodCommand> data) {
//                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
//                            createGoodCommand = data.getData();
//                            Bundle bundleSpecification = new Bundle();
//                            bundleSpecification.putString(CATEGORY_OR_SPECIFICATION, "specification");
//                            if (createGoodCommand.getSpec() != null && createGoodCommand.getSpec().size() > 0){
//                                ArrayList<SpecDetailEntity> specDetailEntities = new ArrayList<>();
//                                for (CreateGoodCommand.SpecBean specBean : createGoodCommand.getSpec()){
//                                    SpecDetailEntity specDetailEntity = new SpecDetailEntity();
//                                    specDetailEntity.setDiscount(specBean.getDiscount());
//                                    specDetailEntity.setPrice(specBean.getPrice());
//                                    specDetailEntity.setStock(specBean.getStock());
//                                    specDetailEntity.setValue_ids(specBean.getValue_ids());
//                                    specDetailEntity.setValue_names(specBean.getValue_names());
//                                    specDetailEntities.add(specDetailEntity);
//                                }
//                                bundleSpecification.putSerializable(CREATE_GOOD_DETAI,createGoodCommand);
//                                bundleSpecification.putString(GOOD_ID, item_id);
//                                bundleSpecification.putParcelableArrayList(SHELF_PRICE_UPDATE,specDetailEntities);
//                            }
//                            JumpUtil.overlay(getContext(), CategorySpecificationCompleteActivity.class,bundleSpecification);
//                        } else {
//                            showError(data.getErrmsg());
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        hideProgress();
//                    }
//                });

    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void shelfListUpdate(ShelfUpdateEvent event) {
        if (xRecyclerView != null) {
            xRecyclerView.refresh();
        }
    }
}
