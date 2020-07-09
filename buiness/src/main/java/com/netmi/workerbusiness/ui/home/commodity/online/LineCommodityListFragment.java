package com.netmi.workerbusiness.ui.home.commodity.online;


import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseXRecyclerFragment;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CommodityApi;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.linecommodity.LineCommodityListEntity;
import com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity.CreateGoodCommand;
import com.netmi.workerbusiness.data.entity.home.store.SpecDetailEntity;
import com.netmi.workerbusiness.data.event.LineCommoditySearchEvent;
import com.netmi.workerbusiness.data.event.ShelfUpdateEvent;
import com.netmi.workerbusiness.databinding.FragmentListBinding;
import com.netmi.workerbusiness.databinding.ItemLineCommodityBinding;
import com.netmi.workerbusiness.ui.home.commodity.category.spec.SelectCategorySpecificationActivity;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;

import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CATEGORY_OR_SPECIFICATION;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CREATE_GOOD_DETAI;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.CREATE_GOOD_FROM;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.GOOD_ID;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.SHELF_LIST;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.SPECIFICATION;


/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/9/2
 * 修改备注：
 */
public class LineCommodityListFragment extends BaseXRecyclerFragment<FragmentListBinding, LineCommodityListEntity> {

    private static final String TAG = LineCommodityListFragment.class.getName();
    /**
     * 商品状态：5:已上架  2:待审核  7:已下架
     */
    public static final String TYPE = "type";
    private int type;
    private String key_word = "";
    private CreateGoodCommand createGoodCommand;

    //实例化
    public static LineCommodityListFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        LineCommodityListFragment fragment = new LineCommodityListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshList(LineCommoditySearchEvent event) {
        key_word = event.getContent();
        xRecyclerView.refresh();
    }


    @Override
    protected void doListData() {
        RetrofitApiFactory.createApi(CommodityApi.class)
                .onlineCommodity(PageUtil.toPage(startPage, 10), Constant.PAGE_ROWS, key_word, type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<PageEntity<LineCommodityListEntity>>>(this) {
                    @Override
                    public void onSuccess(BaseData<PageEntity<LineCommodityListEntity>> data) {
                        showData(data.getData());
                    }
                });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initUI() {
        type = getArguments().getInt(TYPE);
        initRecyclerView();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }


    private void initRecyclerView() {
        adapter = new BaseRViewAdapter<LineCommodityListEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int viewType) {
                return R.layout.item_line_commodity;
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder<LineCommodityListEntity>(binding) {
                    @Override
                    public void bindData(LineCommodityListEntity item) {
                        super.bindData(item);//不能删
                        ItemLineCommodityBinding itemBinding = (ItemLineCommodityBinding) binding;
                        //  商品状态： 5:已上架  2:待审核  7:已下架
                        if (type == 5) {
                            itemBinding.tvDel.setVisibility(View.INVISIBLE);
                            itemBinding.tvDel.setClickable(false);
                            itemBinding.tvShelf.setText("下架");
                            itemBinding.tvShelf.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(
                                    R.mipmap.ic_down_shelf), null, null, null);
                            itemBinding.tvEdit.setVisibility(View.VISIBLE);
                            itemBinding.tvEdit.setClickable(true);
                        } else if (type == 2) {
                            itemBinding.tvDel.setVisibility(View.VISIBLE);
                            itemBinding.tvDel.setClickable(true);
                            itemBinding.tvShelf.setText("下架");
                            itemBinding.tvShelf.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(
                                    R.mipmap.ic_down_shelf), null, null, null);
                            itemBinding.tvEdit.setVisibility(View.INVISIBLE);
                            itemBinding.tvEdit.setClickable(false);
                        } else if (type == 7) {
                            itemBinding.tvEdit.setVisibility(View.VISIBLE);
                            itemBinding.tvEdit.setClickable(true);
                            itemBinding.tvShelf.setText("上架");
                            itemBinding.tvShelf.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(
                                    R.mipmap.ic_onshelf), null, null, null);
                            itemBinding.tvDel.setVisibility(View.VISIBLE);
                            itemBinding.tvDel.setClickable(true);
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        LineCommodityListEntity entity = items.get(position);
                        int id = view.getId();
                        if (id == R.id.tv_edit) { //编辑
                            doShelfEdit(entity.getItem_id());
                        } else if (id == R.id.tv_shelf) {
                            if (type == 7) {
                                //上架
                                doUpShelf(entity.getItem_id(), position);
                            } else {
                                //下架
                                doDownShelf(entity.getItem_id(), position);
                            }
                        } else if (id == R.id.tv_del) {//删除
                            doDeleteShelf(entity.getItem_id(), position);
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


    //编辑  (获取商品详情,真正的编辑操作在商品详情页面中实现)
    public void doShelfEdit(final String item_id) {
        RetrofitApiFactory.createApi(StoreApi.class)
                .getGoodDetail(item_id)
                .compose(RxSchedulers.<BaseData<CreateGoodCommand>>compose())
                .compose((this).<BaseData<CreateGoodCommand>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData<CreateGoodCommand>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData<CreateGoodCommand> data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            createGoodCommand = data.getData();
                            //跳转到创建商品页面并赋值
                            Bundle bundle = new Bundle();
                            //TYPE表示从哪个页面进入 1表示创建商品 2表示编辑商品
                            bundle.putInt(JumpUtil.TYPE, 2);
                            bundle.putString(CREATE_GOOD_FROM, SHELF_LIST);
                            bundle.putSerializable(CREATE_GOOD_DETAI, createGoodCommand);
                            bundle.putString(GOOD_ID, item_id);
                            JumpUtil.overlay(getContext(), CreateCommodityActivity.class, bundle);
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //上架
    public void doUpShelf(String item_id, final int position) {
        RetrofitApiFactory.createApi(StoreApi.class)
                .onShelf(item_id)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("上架成功");
                            adapter.remove(position);
                            //通知上架列表刷新
                            EventBus.getDefault().post(new ShelfUpdateEvent());
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //下架
    public void doDownShelf(String item_id, final int position) {
        RetrofitApiFactory.createApi(StoreApi.class)
                .downShelf(item_id)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("下架成功");
                            adapter.remove(position);
                            //通知上架列表刷新
                            EventBus.getDefault().post(new ShelfUpdateEvent());
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //删除
    public void doDeleteShelf(String item_id, final int position) {
        RetrofitApiFactory.createApi(StoreApi.class).
                deleteGood(Integer.valueOf(item_id))
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcodeJugde() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("删除成功");
                            adapter.remove(position);
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


}
