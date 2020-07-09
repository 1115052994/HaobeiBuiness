package com.liemi.basemall.ui.shopcar;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Explode;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.StoreApi;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.shopcar.ShopCartAdapterEntity;
import com.liemi.basemall.data.entity.shopcar.ShopCartEntity;
import com.liemi.basemall.data.event.BackToAppEvent;
import com.liemi.basemall.data.event.ShopCartEvent;
import com.liemi.basemall.databinding.FragmentShoppingBinding;
import com.liemi.basemall.ui.MainActivity;
import com.liemi.basemall.widget.ShopCartCallback;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.NetworkUtils;
import com.netmi.baselibrary.utils.PageUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/16 15:36
 * 修改备注：
 */
public class ShoppingFragment extends BaseFragment<FragmentShoppingBinding> implements XRecyclerView.LoadingListener, ShopCartCallback {

    public static final String TAG = ShoppingFragment.class.getName();
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

    private TextView rightTitle;

    private BaseRViewAdapter<ShopCartAdapterEntity, BaseViewHolder> adapter;

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

    @Override
    protected int getContentView() {
        return R.layout.fragment_shopping;
    }

    @Override
    protected void initUI() {
        if (getActivity() instanceof MainActivity) {
            mBinding.vStatusBar.setVisibility(View.VISIBLE);
        }

        mBinding.setDoClick(this);
        mBinding.setCheckedListener(onCheckedChangeListener);
        mBinding.tvTitle.setText("购物车");
        rightTitle = getRightTitle();
        rightTitle.setText("管理");
        rightTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        rightTitle.setOnClickListener(this);
//        if (getActivity() instanceof MainActivity) {
//            getLlBack().setVisibility(View.GONE);
//        }

        xRecyclerView = mBinding.xrvShopCart;
        adapter = new ShopCartAdapter(getContext(), this);

        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    private TextView getRightTitle() {
        return mBinding.getRoot().findViewById(R.id.tv_setting);
    }

    private LinearLayout getLlBack() {
        return mBinding.getRoot().findViewById(R.id.ll_back);
    }

    @Override
    protected void initData() {
        xRecyclerView.refresh();
    }

    public void showData(PageEntity<ShopCartAdapterEntity> pageEntity) {
        if (LOADING_TYPE == Constant.PULL_REFRESH) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                xRecyclerView.setLoadingMoreEnabled(true);
            }
            adapter.setData(pageEntity.getList());
            if (mBinding.ivAll.isSelected())
                mBinding.ivAll.setSelected(false);
            else
                calcuCount();
        } else if (LOADING_TYPE == Constant.LOAD_MORE) {
            if (pageEntity.getList() != null && !pageEntity.getList().isEmpty()) {
                adapter.insert(adapter.getItemCount(), pageEntity.getList());
            }
        }
        totalCount = pageEntity.getTotal_pages();
        startPage = adapter.getItemCount();
        calcuCount();
        mBinding.tvTitle.setText(("购物车(" + totalSubCount + ")"));
    }

    /**
     * 本地删除购物车操作
     */
    public void showDataDel() {
        for (int shopCartIndex = 0; shopCartIndex < adapter.getItemCount(); shopCartIndex++) {
            ShopCartAdapterEntity shopCartAdapterEntity = adapter.getItem(shopCartIndex);
            if (shopCartAdapterEntity.isChecked()) {
                adapter.getItems().remove(shopCartIndex);
                shopCartIndex--;
            } else {
                for (int i = 0; i < shopCartAdapterEntity.getList().size(); i++) {
                    if (shopCartAdapterEntity.getList().get(i).isChecked()) {
                        shopCartAdapterEntity.getList().remove(i);
                        i--;
                    }
                }
                if (shopCartAdapterEntity.getList().isEmpty()) {
                    adapter.getItems().remove(shopCartIndex);
                    shopCartIndex--;
                }
            }
        }
        onClick(rightTitle);
        notifyDataSetChanged();
        calcuCount();
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    private int totalSubCount, choiceCount = 0;
    private double totalPrice;
    private double totalScore;

    /**
     * 计算总价，选中数量
     */
    public void calcuCount() {
        choiceCount = 0;
        totalPrice = 0;
        totalScore = 0;
        totalSubCount = 0;

        boolean isAllSelect = true;
        for (ShopCartAdapterEntity shopCartEntity : adapter.getItems()) {
            for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                if (entity.isChecked()) {
                    choiceCount++;
                    totalPrice += (Strings.toFloat(entity.getRealPrice()) * Strings.toFloat(entity.getNum()));
//                    totalScore += (Strings.toDouble(entity.getScore()) * Strings.toFloat(entity.getNum()));
                } else if (shopCartEntity.isChecked()) {
                    shopCartEntity.setChecked(false);
                    adapter.notifyDataSetChanged();
                }

                if (!entity.isChecked()) {
                    isAllSelect = false;
                }
                totalSubCount++;
            }
        }
        if (isAllSelect && adapter.getItemCount() > 0) {
            mBinding.ivAll.setImageResource(R.mipmap.baselib_ic_check);
        } else {
            mBinding.ivAll.setImageResource(R.mipmap.baselib_ic_uncheck);
        }
        mBinding.ivAll.setSelected(isAllSelect);

        mBinding.tvConfirm.setText(String.format("结算(%s)", choiceCount));
        mBinding.tvDelete.setText(String.format("删除(%s)", choiceCount));

        mBinding.tvTotalPrice.setText(FloatUtils.formatResult(String.valueOf(totalScore), FloatUtils.formatDouble(totalPrice)));
    }

    @Override
    public void onRefresh() {
        startPage = 0;
        LOADING_TYPE = Constant.PULL_REFRESH;
        xRecyclerView.setLoadingMoreEnabled(false);
        doListShopCart();
    }

    @Override
    public void onLoadMore() {
        LOADING_TYPE = Constant.LOAD_MORE;
        doListShopCart();
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

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            /*if (compoundButton.getId() == R.id.cb_all) {
                for (ShopCartAdapterEntity shopCartEntity : adapter.getItems()) {
                    shopCartEntity.setChecked(b);
                    for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                        entity.setChecked(b);
                    }
                }
                notifyDataSetChanged();
                calcuCount();
            }*/
        }
    };

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.tv_setting) {
            toggleEditLayout(mBinding.llDelete.getVisibility() == View.GONE);

        } else if (i == R.id.tv_confirm) {
            if (choiceCount > 0) {
                if (isLoading
                        || !updateCartList.isEmpty()) {
                    showError("请先等待数量修改完成！");
                    return;
                }
                ArrayList<ShopCartEntity> shopCartEntities = new ArrayList<>();
                for (ShopCartAdapterEntity shopCartEntity : adapter.getItems()) {
                    if (shopCartEntity.isChecked()) {
                        shopCartEntities.add(new ShopCartEntity(shopCartEntity));
                    } else {
                        ShopCartEntity addShopCart = new ShopCartEntity();
                        addShopCart.setShop(shopCartEntity.getShop());
                        for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                            if (entity.isChecked()) {
                                addShopCart.getList().add(entity);
                            }
                        }
                        if (!addShopCart.getList().isEmpty())
                            shopCartEntities.add(addShopCart);
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(FillOrderActivity.SHOP_CARTS, shopCartEntities);
                JumpUtil.overlay(getContext(), FillOrderActivity.class, bundle);
            } else {
                ToastUtils.showShort("请至少勾选一项商品");
            }

        } else if (i == R.id.tv_delete) {
            List<String> list = new ArrayList<>();
            for (ShopCartAdapterEntity shopCartEntity : adapter.getItems()) {
                for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                    if (entity.isChecked()) {
                        list.add(entity.getCart_id());
                    }
                }
            }
            doShopCartDel(list);
        } else if (i == R.id.ll_all) {
            if (adapter.getItemCount() <= 0) {
                return;
            }
            mBinding.ivAll.setSelected(!mBinding.ivAll.isSelected());
            if (mBinding.ivAll.isSelected()) {
                mBinding.ivAll.setImageResource(R.mipmap.baselib_ic_check);
            } else {
                mBinding.ivAll.setImageResource(R.mipmap.baselib_ic_uncheck);
            }
            for (ShopCartAdapterEntity shopCartEntity : adapter.getItems()) {
                shopCartEntity.setChecked(mBinding.ivAll.isSelected());
                for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                    entity.setChecked(mBinding.ivAll.isSelected());
                }
            }
            notifyDataSetChanged();
//            calcuCount();
        }


    }

    private void toggleEditLayout(boolean isDelete) {
        if (rightTitle.getText().equals("管理")) {
            rightTitle.setText("取消");
        } else {
            rightTitle.setText("管理");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionManager.beginDelayedTransition(mBinding.llFooter, new Explode());
        }
        mBinding.rlBuy.setVisibility(isDelete ? View.GONE : View.VISIBLE);
        mBinding.llDelete.setVisibility(isDelete ? View.VISIBLE : View.GONE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void shopCartUpdate(ShopCartEvent event) {
        xRecyclerView.refresh();
    }

    private void doListShopCart() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listShopCart(PageUtil.toPage(startPage), Constant.ALL_PAGES)
                .compose(RxSchedulers.<BaseData<PageEntity<ShopCartAdapterEntity>>>compose())
                .compose((this).<BaseData<PageEntity<ShopCartAdapterEntity>>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData<PageEntity<ShopCartAdapterEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<ShopCartAdapterEntity>> data) {
                        showData(data.getData());
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


    private void doShopCartDel(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCartDel(list)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        showDataDel();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    private LinkedHashMap<GoodsDetailedEntity, Float> updateCartList = new LinkedHashMap<>(16);
    private boolean isLoading = false;

    public void doUpdateCartNum(final GoodsDetailedEntity goodEntity, final float num) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(com.netmi.baselibrary.R.string.tip_network_error);
            updateCartList.clear();
            calcuCount();
            return;
        } else if (isLoading) {
            updateCartList.put(goodEntity, num);
            return;
        }
        isLoading = true;
        RetrofitApiFactory.createApi(StoreApi.class)
                .shopCartUpdate(goodEntity.getCart_id(), String.valueOf(num))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        isLoading = false;
                        showError(ex.getMessage());
                        updateCartList.clear();
                        calcuCount();
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        goodEntity.setNum(Strings.twoDecimal(num));
                        if (updateCartList.containsKey(goodEntity)) {
                            updateCartList.remove(goodEntity);
                        }

                        if (updateCartList.entrySet().iterator().hasNext()) {
                            Map.Entry<GoodsDetailedEntity, Float> shopCart = updateCartList.entrySet().iterator().next();
                            doUpdateCartNum(shopCart.getKey(), shopCart.getValue());
                        }
                    }

                    @Override
                    public void onComplete() {
                        calcuCount();
                        isLoading = false;
                        hideProgress();
                    }
                });
    }
}
