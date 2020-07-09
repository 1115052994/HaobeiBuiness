package com.liemi.basemall.ui.home;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.CategoryApi;
import com.liemi.basemall.data.api.StoreApi;
import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.entity.floor.NewFloorEntity;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.liemi.basemall.databinding.ItemHomeBannerBinding;
import com.liemi.basemall.databinding.ItemHomeFloorBinding;
import com.liemi.basemall.databinding.ItemHomeNoticeBinding;
import com.liemi.basemall.databinding.ItemNewFloorBinding;
import com.liemi.basemall.databinding.ItemNewFloorImgBinding;
import com.liemi.basemall.ui.category.CategoryGoodsActivity;
import com.liemi.basemall.ui.category.GoodsListAdapter;
import com.liemi.basemall.ui.store.StoreDetailActivity;
import com.liemi.basemall.ui.store.good.GoodDetailActivity;
import com.liemi.basemall.widget.GoodsBannerHolderView;
import com.liemi.basemall.widget.MyTextBannerView;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.data.event.SwitchTabEvent;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.MLoadingDialog;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.superluo.textbannerlibrary.ITextBannerItemClickListener;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_BANNER;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_GOODS_HOT;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_GOODS_NEWS;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_NAVIGATION;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_NOTICE;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_STORE;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_GOOD;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_GOODS_CATEGORY;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_GOODS_HOT;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_GOODS_NEWS;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_GOODS_RECOMMEND;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_STORE;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_WEB_CONTENT;
import static com.liemi.basemall.data.entity.floor.NewFloorEntity.FLOOR_TYPE_WEB_URL;
import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_HOT_GOODS;
import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_ID;
import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_NAME;
import static com.liemi.basemall.ui.category.CategoryGoodsActivity.MC_NEW_GOODS;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;
import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_URL;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/12/4
 * 修改备注：
 */
public class FloorAdapter extends BaseRViewAdapter<NewFloorEntity, BaseViewHolder> {

    private RxFragment lifecycleFragment;

    private String shopId;

    public FloorAdapter(Context context) {
        super(context);
    }

    public FloorAdapter(Context context, MyXRecyclerView recyclerView) {
        super(context, recyclerView);
    }

    public FloorAdapter setLifecycleFragment(RxFragment lifecycleFragment) {
        this.lifecycleFragment = lifecycleFragment;
        return this;
    }

    public FloorAdapter setShopId(String shopId) {
        this.shopId = shopId;
        return this;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public void setData(List<NewFloorEntity> items) {
        super.setData(items);
        for (NewFloorEntity entity : getItems()) {
            if (entity.getType() == FLOOR_GOODS_HOT || entity.getType() == FLOOR_GOODS_NEWS) {
                doListGoodsData(entity);
            } else if (entity.getType() == FLOOR_STORE) {
                doListStore(entity);
            }
        }
    }

    @Override
    public int layoutResId(int viewType) {
        switch (viewType) {
            case FLOOR_BANNER:
                return R.layout.item_home_banner;
            case FLOOR_NOTICE:
                return R.layout.item_home_notice;
            case FLOOR_GOODS_HOT:
            case FLOOR_GOODS_NEWS:
            case FLOOR_STORE:
                return R.layout.item_home_floor;
            default:
                return R.layout.item_new_floor;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<NewFloorEntity>(binding) {

            @Override
            public void bindData(final NewFloorEntity item) {
                if (getBinding() instanceof ItemHomeBannerBinding) {
                    ItemHomeBannerBinding binding = (ItemHomeBannerBinding) getBinding();
                    if (item.getFloor_data() != null
                            && !item.getFloor_data().isEmpty()) {
                        final List<String> list = new ArrayList<>();

                        for (NewFloorEntity.FloorDataBean entity : item.getFloor_data()) {
                            list.add(entity.getImg_url());
                        }

                        binding.cbBanner
                                .setPages(new CBViewHolderCreator() {
                                    @Override
                                    public Holder createHolder(View itemView) {
                                        return new GoodsBannerHolderView<String>(itemView);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.item_banner;
                                    }
                                }, list)
                                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                                .setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int subPosition) {
                                        floorDataClick(getContext(), item.getFloor_data().get(subPosition));
                                    }
                                }).startTurning(5000);

                    }
                } else if (getBinding() instanceof ItemHomeNoticeBinding) {
                    ItemHomeNoticeBinding binding = (ItemHomeNoticeBinding) getBinding();
                    List<MyTextBannerView.TextBannerEntity> list = new ArrayList<>();
                    for (NewFloorEntity.FloorDataBean entity : item.getFloor_data()) {
                        list.add(new MyTextBannerView.TextBannerEntity(entity.getTitle(), entity.getTime()));
                    }
                    binding.tvBanner.setItemOnClickListener(new ITextBannerItemClickListener() {
                        @Override
                        public void onItemClick(String data, int position) {
                            floorDataClick(getContext(), item.getFloor_data().get(position));
                        }
                    });
                    binding.tvBanner.stopViewAnimator();
                    binding.tvBanner.setData(list);
                    binding.tvBanner.startViewAnimator();
                } else if (getBinding() instanceof ItemNewFloorBinding) {
                    ItemNewFloorBinding binding = (ItemNewFloorBinding) getBinding();
                    final GridLayoutManager gridLayoutManager;
                    if (item.getType() == FLOOR_NAVIGATION) {
                        //分类导航栏，一行显示4个或者5个，根据总数量判断
                        gridLayoutManager = new GridLayoutManager(getContext(), 5);
                        binding.rvFloor.setBackgroundColor(Color.WHITE);
                        binding.rvFloor.setPadding(Densitys.dp2px(getContext(), 15), Densitys.dp2px(getContext(), 10), 0, Densitys.dp2px(getContext(), 10));
                    } else {
                        gridLayoutManager = new GridLayoutManager(getContext(), Strings.isEmpty(item.getFloor_data()) ? 1 : item.getFloor_data().size());
                        binding.rvFloor.setPadding(0, Densitys.dp2px(getContext(), item.getTop()), 0, Densitys.dp2px(getContext(), item.getBottom()));
                        binding.rvFloor.setBackgroundColor(Color.TRANSPARENT);
                    }
                    binding.rvFloor.setLayoutManager(gridLayoutManager);
                    BaseRViewAdapter<NewFloorEntity.FloorDataBean, BaseViewHolder> picAdapter = new BaseRViewAdapter<NewFloorEntity.FloorDataBean, BaseViewHolder>(getContext()) {

                        @Override
                        public int layoutResId(int viewType) {
                            if (item.getType() == FLOOR_NAVIGATION) {
                                return R.layout.item_home_nav;
                            } else {
                                return R.layout.item_new_floor_img;
                            }
                        }

                        @Override
                        public BaseViewHolder holderInstance(ViewDataBinding binding) {
                            return new BaseViewHolder<NewFloorEntity.FloorDataBean>(binding) {

                                @Override
                                public void doClick(View view) {
                                    super.doClick(view);
                                    floorDataClick(getContext(), item.getFloor_data().get(position));
                                }

                                @Override
                                public ItemNewFloorImgBinding getBinding() {
                                    return (ItemNewFloorImgBinding) super.getBinding();
                                }

                            };
                        }
                    };

                    binding.rvFloor.setAdapter(picAdapter);
                    if (!Strings.isEmpty(item.getFloor_data())) {
                        picAdapter.setData(item.getFloor_data());
                    }
                } else if (getBinding() instanceof ItemHomeFloorBinding) {
                    ItemHomeFloorBinding binding = (ItemHomeFloorBinding) getBinding();
                    binding.getRoot().setPadding(0, Densitys.dp2px(getContext(), item.getTop()), 0, Densitys.dp2px(getContext(), item.getBottom()));
                    switch (item.getType()) {
                        case FLOOR_GOODS_HOT:
                            binding.tvTitle.setText("热门商品");
                            break;
                        case FLOOR_GOODS_NEWS:
                            binding.tvTitle.setText("新品推荐");
                            break;
                        case FLOOR_STORE:
                            binding.tvTitle.setText("推荐店铺");
                            break;
                        default:
                            break;
                    }

                    //店铺列表
                    if (item.getType() == FLOOR_STORE) {
                        binding.rvFloor.setLayoutManager(new LinearLayoutManager(getContext()));
                        BaseRViewAdapter<StoreEntity, BaseViewHolder> storeAdapter = new BaseRViewAdapter<StoreEntity, BaseViewHolder>(getContext()) {
                            @Override
                            public int layoutResId(int position) {
                                return R.layout.item_home_store;
                            }

                            @Override
                            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                return new BaseViewHolder<StoreEntity>(binding) {

                                    @Override
                                    public void bindData(StoreEntity item) {
                                        super.bindData(item);
                                    }

                                    @Override
                                    public void doClick(View view) {
                                        super.doClick(view);
                                        JumpUtil.startSceneTransition((Activity) getContext(), StoreDetailActivity.class,
                                                new FastBundle().putString(STORE_ID, getItem(position).getId()),
                                                new Pair<>(getBinding().getRoot().findViewById(R.id.iv_store_pic), getContext().getString(R.string.transition_store)),
                                                new Pair<>(getBinding().getRoot().findViewById(R.id.tv_store_name), getContext().getString(R.string.transition_store_name)));
                                    }
                                };
                            }
                        };
                        binding.rvFloor.setAdapter(storeAdapter);
                        if (!Strings.isEmpty(item.getShops_list())) {
                            storeAdapter.setData(item.getShops_list());
                        }
                    }
                    //商品列表
                    else {
                        binding.rvFloor.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        GoodsListAdapter goodsAdapter = new GoodsListAdapter(getContext());
                        binding.rvFloor.setAdapter(goodsAdapter);
                        if (!Strings.isEmpty(item.getGoods_list())) {
                            goodsAdapter.setData(item.getGoods_list());
                        }
                    }


                }
                super.bindData(item);
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                if (view.getId() == R.id.ll_title) {
                    //根据楼层的类型，设置房间的点击效果，例如FLOOR_GOODS_HOT 对应的点击效果 FLOOR_TYPE_GOODS_HOT
                    ItemHomeFloorBinding binding = (ItemHomeFloorBinding) getBinding();
                    NewFloorEntity.FloorDataBean dataBean;
                    if (binding.tvTitle.getText().toString().equals("推荐店铺")) {
                        dataBean = new NewFloorEntity.FloorDataBean(String.valueOf(NewFloorEntity.FLOOR_TYPE_All_STORE));
                    } else {
                        dataBean = new NewFloorEntity.FloorDataBean(String.valueOf(getItem(position).getType() - 2));
                    }
                    floorDataClick(context, dataBean);
                }
            }
        };
    }


    //统一处理点击事件
    public static void floorDataClick(Context context, NewFloorEntity.FloorDataBean entity) {
        //商品：1，-- 分类对应列表：2，-- 店铺：3，-- 热门商品列表：4，-- 新品推荐列表：5，-- 推荐店铺列表：6，-- 富文本：7，-- 外链：8
        switch (entity.getType()) {
            case FLOOR_TYPE_GOOD:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    JumpUtil.overlay(context, GoodDetailActivity.class,
                            new FastBundle().putString(ITEM_ID, entity.getParam()));
                } else {
                    ToastUtils.showShort("未配置商品");
                }
                break;
            case FLOOR_TYPE_GOODS_CATEGORY:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    JumpUtil.overlay(context, CategoryGoodsActivity.class,
                            new FastBundle()
                                    .put(MC_ID, entity.getParam())
                                    .putString(MC_NAME, entity.getTitle()));
                } else {
                    ToastUtils.showShort("未配置商品列表");
                }
                break;
            case FLOOR_TYPE_STORE:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    JumpUtil.overlay(context, StoreDetailActivity.class,
                            new FastBundle().putString(STORE_ID, entity.getParam()));
                } else {
                    ToastUtils.showShort("未配置店铺");
                }
                break;
            case FLOOR_TYPE_GOODS_NEWS:
            case FLOOR_TYPE_GOODS_HOT:
                JumpUtil.overlay(context, CategoryGoodsActivity.class,
                        new FastBundle()
                                .put(entity.getType() == FLOOR_TYPE_GOODS_HOT ? MC_HOT_GOODS : MC_NEW_GOODS, "1")
                                .putString(MC_NAME, entity.getType() == FLOOR_TYPE_GOODS_HOT ? "热门商品" : "新品推荐"));
                break;
            case FLOOR_TYPE_GOODS_RECOMMEND:
                EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_store));
                break;
            case FLOOR_TYPE_WEB_CONTENT:
            case FLOOR_TYPE_WEB_URL:
                if (!TextUtils.isEmpty(entity.getParam())) {
                    Bundle bundle = new Bundle();
                    if(entity.getType() == FLOOR_TYPE_WEB_CONTENT) {
                        bundle.putString(WEBVIEW_TITLE, TextUtils.isEmpty(entity.getTitle()) ? "详情" : entity.getTitle());
                    }
                    bundle.putInt(WEBVIEW_TYPE, entity.getType() == FLOOR_TYPE_WEB_CONTENT ? BaseWebviewActivity.WEBVIEW_TYPE_CONTENT : WEBVIEW_TYPE_URL);
                    bundle.putString(WEBVIEW_CONTENT, entity.getType() == FLOOR_TYPE_WEB_CONTENT ? entity.getParam() : entity.getParam());
                    JumpUtil.overlay(context, BaseWebviewActivity.class, bundle);
                } else {
                    ToastUtils.showShort("未配置页面");
                }
                break;
            case NewFloorEntity.FLOOR_TYPE_All_STORE:   //查看全部店铺
                EventBus.getDefault().post(new SwitchTabEvent(R.id.rb_store));
                break;
            default:
                break;
        }
    }

    public void hideProgress() {
        MLoadingDialog.dismiss();
    }

    public void showError(String message) {
        hideProgress();
        ToastUtils.showShort(message);
    }

    private void doListGoodsData(final NewFloorEntity entity) {
        if (lifecycleFragment == null) {
            ToastUtils.showShort("请先初始化页面");
        }

        RetrofitApiFactory.createApi(CategoryApi.class)
                .listGoods(0, entity.getNums() > 0 ? entity.getNums() : Constant.PAGE_ROWS, null, null, null,
                        null, null, null, null, shopId,
                        entity.getType() == FLOOR_GOODS_HOT ? "1" : "0", entity.getType() == FLOOR_GOODS_NEWS ? "1" : "0", null, null)
                .compose(RxSchedulers.<BaseData<PageEntity<GoodsListEntity>>>compose())
                .compose(((RxFragment) lifecycleFragment).<BaseData<PageEntity<GoodsListEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<PageEntity<GoodsListEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<GoodsListEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            entity.setGoods_list(data.getData().getList());
                            notifyPosition(getItems().indexOf(entity));
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


    private void doListStore(final NewFloorEntity entity) {
        if (lifecycleFragment == null) {
            ToastUtils.showShort("请先初始化页面");
        }
        RetrofitApiFactory.createApi(StoreApi.class)
                .listStore(0, entity.getNums() > 0 ? entity.getNums() : 2 * Constant.PAGE_ROWS, "2", null)
                .compose(RxSchedulers.<BaseData<PageEntity<StoreEntity>>>compose())
                .compose((lifecycleFragment).<BaseData<PageEntity<StoreEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<PageEntity<StoreEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<StoreEntity>> data) {
                        if (data.getData() != null && !Strings.isEmpty(data.getData().getList())) {
                            entity.setShops_list(data.getData().getList());
                            notifyPosition(getItems().indexOf(entity));
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


}
