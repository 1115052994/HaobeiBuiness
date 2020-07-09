package com.liemi.basemall.ui.shopcar;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.api.StoreApi;
import com.liemi.basemall.data.entity.AddressEntity;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.order.FillOrderEntity;
import com.liemi.basemall.data.entity.order.InvoiceEntity;
import com.liemi.basemall.data.entity.order.OrderPayEntity;
import com.liemi.basemall.data.entity.shopcar.ShopCartEntity;
import com.liemi.basemall.data.event.ShopCartEvent;
import com.liemi.basemall.databinding.ActivityFillOrderBinding;
import com.liemi.basemall.databinding.ItemFillOrderGoodBinding;
import com.liemi.basemall.databinding.ItemShopCartBinding;
import com.liemi.basemall.ui.personal.address.AddressAddActivity;
import com.liemi.basemall.ui.personal.address.AddressManageActivity;
import com.liemi.basemall.ui.store.StoreDetailActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.liemi.basemall.ui.personal.address.AddressManageActivity.ADDRESS_ENTITY;
import static com.liemi.basemall.ui.personal.address.AddressManageActivity.CHOICE_ADDRESS;
import static com.liemi.basemall.ui.personal.address.AddressManageActivity.CHOICE_ADDRESS_MAID;
import static com.liemi.basemall.ui.shopcar.OrderPayOnlineActivity.ORDER_PAY_ENTITY;
import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/24 14:48
 * 修改备注：
 */
public class FillOrderActivity extends BaseActivity<ActivityFillOrderBinding> {

    private final static int REQUEST_INVOICE = 0x1123;

    private final static int REQUEST_ADDRESS = 0x1125;

    public static final String SHOP_CARTS = "shop_carts";

    public static final String TOTAL_PRICE = "total_price";

    private RecyclerView recyclerView;

    private BaseRViewAdapter<BaseEntity, BaseViewHolder> adapter;

    private InvoiceEntity invoiceEntity;

    List<BaseEntity> baseEntities = new ArrayList<>();

    ArrayList<ShopCartEntity> shopCartEntities = new ArrayList<>();

    private double totalPrice = 0;

    private double totalScore, totalEarnScore;

    @Override
    protected int getContentView() {
        return R.layout.activity_fill_order;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText("确认订单");

        recyclerView = mBinding.rvFillOrder;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.item_fill_order_address;
                } /*else if (viewType == 2) {
                    return R.layout.item_shop_cart;
                }
                return R.layout.item_fill_order_score;*/
                return R.layout.item_shop_cart;
            }

            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof AddressEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof ShopCartEntity) {
                    return 2;
                }
                return super.getItemViewType(position);
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        if (getBinding() instanceof ItemShopCartBinding) {
                            ItemShopCartBinding cartBinding = (ItemShopCartBinding) getBinding();
                            cartBinding.llTotal.setVisibility(View.GONE);
                            ShopCartEntity cartEntity = (ShopCartEntity) item;
                            float totalPrice = 0;
                            float totalScore = 0;
                            for (GoodsDetailedEntity entity : cartEntity.getList()) {
                                totalPrice += (Strings.toFloat(entity.getRealPrice()) * Strings.toFloat(entity.getNum()));
//                                totalScore += (Strings.toFloat(entity.getScore()) * Strings.toFloat(entity.getNum()));
                            }
                            cartBinding.tvTotalGood.setText(String.format(getString(R.string.format_total_good_price_tip), cartEntity.getList().size()));
                            cartBinding.mtvTotalPrice.setText(FloatUtils.formatResult(String.valueOf(totalScore), String.valueOf(totalPrice)));
                            RecyclerView rvGoods = cartBinding.rvGoods;
                            rvGoods.setLayoutManager(new LinearLayoutManager(context));
                            BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder> goodAdapter = getGoodAdapter();
                            rvGoods.setAdapter(goodAdapter);
                            goodAdapter.setData(((ShopCartEntity) item).getList());
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.ll_address) {
                            Bundle addressBundle = new Bundle();
                            addressBundle.putInt(CHOICE_ADDRESS, 1);
                            AddressEntity addressEntity = (AddressEntity) adapter.getItem(0);
                            addressBundle.putString(CHOICE_ADDRESS_MAID, addressEntity != null ? addressEntity.getMaid() : "");
                            JumpUtil.startForResult(getActivity(), AddressManageActivity.class, REQUEST_ADDRESS, addressBundle);
                        } else if (i == R.id.ll_no_address) {

                            JumpUtil.startForResult(getActivity(), AddressAddActivity.class, REQUEST_ADDRESS, null);
                        } else if (i == R.id.tv_store_name) {
                            if (getItem(position) instanceof ShopCartEntity) {
                                ShopCartEntity cartEntity = (ShopCartEntity) getItem(position);
                                if (cartEntity.getShop() != null) {
                                    MApplication.getInstance().appManager.finishActivity(StoreDetailActivity.class);
                                    JumpUtil.overlay(context, StoreDetailActivity.class, new FastBundle().putString(STORE_ID, cartEntity.getShop().getId()));
                                } else {
                                    ToastUtils.showShort("缺少商家相关信息");
                                }
                            }
                        }
                    }
                };
            }

        });


    }

    private BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder> getGoodAdapter() {
        return new BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_fill_order_good;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {

                };
            }
        };
    }

    @Override
    protected void initData() {

        shopCartEntities = (ArrayList<ShopCartEntity>) getIntent().getSerializableExtra(SHOP_CARTS);

        //计算总价
        for (ShopCartEntity shopCartEntity : shopCartEntities) {
            for (GoodsDetailedEntity entity : shopCartEntity.getList()) {

                //选取店铺下商品最高的邮费
                if (Strings.toDouble(entity.getPostage()) > Strings.toDouble(shopCartEntity.getPostage())) {
                    shopCartEntity.setPostage(entity.getPostage());
                }

                totalPrice += (Strings.toDouble(entity.getRealPrice()) * Strings.toDouble(entity.getNum()));

//                totalScore += (Strings.toDouble(entity.getScore()) * Strings.toFloat(entity.getNum()));

                totalEarnScore += (Strings.toDouble(entity.getEarn_score()) * Strings.toDouble(entity.getNum()));
            }

            //加上邮费
            totalPrice += Strings.toDouble(shopCartEntity.getPostage());
        }

        //填充数据
        baseEntities.add(new AddressEntity());
        baseEntities.addAll(shopCartEntities);
        adapter.setData(baseEntities);


        mBinding.tvTotalPrice.setText(FloatUtils.formatResult(String.valueOf(totalScore), String.valueOf(totalPrice)));

        doListAddress();
    }

    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_payment) {
            AddressEntity addressEntity = (AddressEntity) adapter.getItem(0);
            if (!TextUtils.isEmpty(addressEntity.getMaid())) {
                FillOrderEntity orderCommand = new FillOrderEntity();
                orderCommand.setAddress_id(addressEntity.getMaid());

                NumberFormat nf = NumberFormat.getInstance();
                // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
                nf.setGroupingUsed(false);
                orderCommand.setAmount(nf.format(totalPrice));

                orderCommand.setPay_score(totalScore);
                //商品列表
                List<FillOrderEntity.Good> list = new ArrayList<FillOrderEntity.Good>();
                for (ShopCartEntity shopCartEntity : shopCartEntities) {
                    for (GoodsDetailedEntity entity : shopCartEntity.getList()) {
                        FillOrderEntity.Good good = new FillOrderEntity.Good();
                        if (!TextUtils.isEmpty(entity.getCart_id())) {
                            good.setCart_id(entity.getCart_id());
                        }
                        good.setIvid(entity.getIvid());
                        good.setNum((int) Strings.toFloat(entity.getNum()));
                        good.setItem_type(entity.getItem_type());
                        good.setRemark(shopCartEntity.getRemark());
                        list.add(good);
                    }
                }
                orderCommand.setItem_data(list);

                doOrderCreate(orderCommand);
            } else {
                ToastUtils.showShort("请先设置收货地址！");
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            //收货地址
            if (requestCode == REQUEST_ADDRESS) {
                AddressEntity addressEntity = (AddressEntity) data.getSerializableExtra(ADDRESS_ENTITY);
                if (addressEntity == null) {
                    addressEntity = new AddressEntity();
                }
                adapter.replace(0, addressEntity);
            }
        }
    }

    private void doOrderCreate(final FillOrderEntity orderCommand) {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .createOrder(orderCommand)
                .compose(RxSchedulers.<BaseData<OrderPayEntity>>compose())
                .compose((this).<BaseData<OrderPayEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<OrderPayEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<OrderPayEntity> data) {
                        EventBus.getDefault().post(new ShopCartEvent());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ORDER_PAY_ENTITY, data.getData());
                        JumpUtil.overlay(getContext(), OrderPayOnlineActivity.class, bundle);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


    private void doListAddress() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .doAddressList(0, 1)
                .compose(RxSchedulers.<BaseData<PageEntity<AddressEntity>>>compose())
                .compose((this).<BaseData<PageEntity<AddressEntity>>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<AddressEntity>>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<PageEntity<AddressEntity>> data) {
                        if (data.getData() != null && !data.getData().getList().isEmpty())
                            adapter.replace(0, data.getData().getList().get(0));
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }


}
