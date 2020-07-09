package com.liemi.basemall.ui.store.good;

import android.content.DialogInterface;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.StoreApi;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.good.SpecsEntity;
import com.liemi.basemall.data.entity.good.SpecsGroupEntity;
import com.liemi.basemall.data.entity.shopcar.ShopCartEntity;
import com.liemi.basemall.data.event.ShopCartEvent;
import com.liemi.basemall.databinding.DialogFragmentGoodsBuyBinding;
import com.liemi.basemall.databinding.ItemGoodsSpecsBinding;
import com.liemi.basemall.ui.personal.order.OrderModuleFragment;
import com.liemi.basemall.ui.shopcar.FillOrderActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.annotations.NonNull;

import static com.netmi.baselibrary.data.Constant.ORDER_STATE;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/10/16 18:20
 * 修改备注：
 */
public class GoodsBuyDialogFragment extends BaseDialogFragment<DialogFragmentGoodsBuyBinding> implements DialogInterface.OnKeyListener {

    private BaseRViewAdapter<SpecsEntity, BaseViewHolder> adapter;

    private GoodsDetailedEntity goodEntity;

    private HashMap<String, SpecsEntity.ChildrenBean> choiceSpecs = new HashMap<>();

    private SpecsGroupEntity choicePrice;

    private boolean addShopCart;


    public GoodsBuyDialogFragment setGoodsEntity(GoodsDetailedEntity goodsEntity) {
        this.goodEntity = goodsEntity;
        return this;
    }

    public GoodsBuyDialogFragment setBuyType(boolean addShopCart) {
        this.addShopCart = addShopCart;
        return this;
    }

    @Override
    public int getTheme() {
        return R.style.CustomDialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        getDialog().setOnKeyListener(this);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 紧贴底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
            window.setAttributes(lp);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_goods_buy;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.tvConfirm.setText(addShopCart ? "加入购物车" : "立即购买");
    }

    @Override
    protected void initUI() {
        if (goodEntity == null) {
            ToastUtils.showShort("没有商品信息");
            onStop();
        } else {
            mBinding.setDoClick(this);
            mBinding.setItem(goodEntity);

            mBinding.tvNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        setNumber(1);
                    } else if (choicePrice != null
                            && !s.toString().equals(Strings.twoDecimal(getNum()))) {
                        if (Strings.toFloat(s.toString()) > choicePrice.getStock()) {
                            ToastUtils.showShort("购买数量不能超出库存");
                            setNumber((int) choicePrice.getStock());
                        }
                    }
                }
            });

            mBinding.rvNumber.setLayoutManager(new LinearLayoutManager(getContext()));
            mBinding.rvNumber.setAdapter(adapter = new BaseRViewAdapter<SpecsEntity, BaseViewHolder>(getContext()) {
                @Override
                public int layoutResId(int viewType) {
                    return R.layout.item_goods_specs;
                }

                @Override
                public BaseViewHolder holderInstance(ViewDataBinding binding) {
                    return new BaseViewHolder<SpecsEntity>(binding) {

                        int specsPosition;

                        @Override
                        public void bindData(SpecsEntity item) {
                            specsPosition = position;
                            final List<SpecsEntity.ChildrenBean> specsList = adapter.getItem(specsPosition).getChildren();
                            //流式布局的规格
                            getBinding().tflSpecs.setAdapter(new SpecsTagAdapter<SpecsEntity.ChildrenBean>(specsList) {
                                @Override
                                public View getView(FlowLayout parent, int position, SpecsEntity.ChildrenBean skuBean) {
                                    TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_dialog_goods_buy_sku,
                                            getBinding().tflSpecs, false);
                                    tv.setTextColor(choiceSpecs.containsKey(skuBean.getValue_id()) ?
                                            Color.parseColor("#FFFF3700") : skuBean.getOption() == 0 ?
                                            Color.parseColor("#80878787") : Color.parseColor("#FF878787"));
                                    tv.setText(skuBean.getValue_name());
                                    return tv;
                                }

                                @Override
                                public boolean setSelected(int position, SpecsEntity.ChildrenBean childrenBean) {
                                    return choiceSpecs.containsKey(childrenBean.getValue_id());
                                }

                            });

                            getBinding().tflSpecs.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    return specsList.get(position).getOption() == 0;
                                }
                            });

                            getBinding().tflSpecs.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                                @Override
                                public void onSelected(Set<Integer> selectPosSet) {
                                    Iterator<Integer> iterator = selectPosSet.iterator();
                                    if (iterator.hasNext()) {
                                        SpecsEntity.ChildrenBean choiceBean = specsList.get(iterator.next());

                                        //相同分类的规格，只保留一个
                                        Iterator<SpecsEntity.ChildrenBean> keyIterator = choiceSpecs.values().iterator();
                                        while (keyIterator.hasNext()) {
                                            SpecsEntity.ChildrenBean bean = keyIterator.next();
                                            if (TextUtils.equals(bean.getProp_id(), choiceBean.getProp_id())) {
                                                choiceSpecs.remove(bean.getValue_id());
                                                break;
                                            }
                                        }

                                        choiceSpecs.put(choiceBean.getValue_id(), choiceBean);

                                        doListGoodsSpecs();
                                    }
                                }
                            });
                            super.bindData(item);
                        }

                        @Override
                        public ItemGoodsSpecsBinding getBinding() {
                            return (ItemGoodsSpecsBinding) super.getBinding();
                        }
                    };
                }
            });

            mBinding.executePendingBindings();

            doListSpecs();

        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int i = view.getId();
        if (i == R.id.tv_minus) {
            setNumber(getNum() - 1);

        } else if (i == R.id.tv_plus) {
            if (choicePrice == null) {
                ToastUtils.showShort("请先将商品规格选择完整");
                return;
            }
            setNumber(getNum() + 1);

        } else if (i == R.id.tv_confirm) {
            if (choicePrice == null) {
                ToastUtils.showShort("请先将商品规格选择完整");
                return;
            } else if (getNum() <= 0) {
                ToastUtils.showShort("选购的商品数量必须大于0");
                return;
            }

            if (addShopCart) {
                doAddShopCart();
            } else {
                ArrayList<ShopCartEntity> shopCartEntities = new ArrayList<>();
                ShopCartEntity addShopCart = new ShopCartEntity();
                addShopCart.setShop(goodEntity.getShop());
                List<GoodsDetailedEntity> list = new ArrayList<>();
                goodEntity.setNum(String.valueOf(getNum()));
                goodEntity.setIvid(choicePrice.getIvid());
                goodEntity.setPrice(choicePrice.getPrice());
                goodEntity.setItem_type(choicePrice.getItem_type());
                goodEntity.setValue_names("规格：" + choicePrice.getValue_names());
                if (!Strings.isEmpty(choicePrice.getImg_url())) {
                    goodEntity.setImg_url(choicePrice.getImg_url());
                }
                list.add(goodEntity);
                addShopCart.setList(list);
                shopCartEntities.add(addShopCart);
                Bundle bundle = new Bundle();
                bundle.putSerializable(FillOrderActivity.SHOP_CARTS, shopCartEntities);
                JumpUtil.overlay(getContext(), FillOrderActivity.class, bundle);
                onStop();
            }

        } else if (i == R.id.view_bg) {
            onStop();
        }
    }


    private int getNum() {
        return Strings.toInt(mBinding.tvNum.getText().toString());
    }

    private void setNumber(int number) {
        mBinding.tvNum.setText(String.valueOf(number));
        mBinding.tvMinus.setEnabled(number > 1);
        mBinding.tvPlus.setEnabled(choicePrice != null && number < choicePrice.getStock());
    }

    private boolean completeChoice() {
        return choiceSpecs.size() == adapter.getItemCount();
    }


    private void doListGoodsSpecs() {

        List<SpecsEntity.ChildrenBean> specs = null;
        if (!choiceSpecs.isEmpty()) {
            specs = new ArrayList<>(choiceSpecs.values());
        }

        if (specs == null || specs.isEmpty()) {
            adapter.setData(allSpecsList);
            if (completeChoice()) {
                doGetGoodsPrice();
            }
            return;
        }


        for (SpecsEntity entity : allSpecsList) {

            //遍历所有规格
            for (SpecsEntity.ChildrenBean bean : entity.getChildren()) {

                bean.setOption(1);

                //逻辑是，判断该规格， 是否在已选规格组合中存在
                for (SpecsEntity.ChildrenBean choice : specs) {

                    if (TextUtils.equals(bean.getProp_id(), choice.getProp_id())) {
                        continue;
                    }

                    StringBuilder canChoiceIds = new StringBuilder();

                    for (SpecsGroupEntity groupEntity : allGroupList) {
                        //拿到已选规格的所有其它可选规格
                        if (groupEntity.getValue_ids().contains(choice.getValue_id())) {
                            canChoiceIds.append(groupEntity.getValue_ids());
                        }
                    }

                    if (!canChoiceIds.toString().contains(bean.getValue_id())) {
                        bean.setOption(0);
                        break;
                    }

                }
            }

        }

        adapter.setData(allSpecsList);

        //判断是否将规格选择完全了
        if (completeChoice()) {
            //筛选出完成规格选择的商品
            doGetGoodsPrice();
        }

    }


    /*获得最终的规格组合*/
    private void doGetGoodsPrice() {

        List<SpecsEntity.ChildrenBean> specs = null;
        if (!choiceSpecs.isEmpty()) {
            specs = new ArrayList<>(choiceSpecs.values());
        }

        //遍历所有组合
        for (SpecsGroupEntity groupEntity : allGroupList) {

            //已选的规格
            for (int i = 0; i < specs.size(); i++) {

                //组合如果包含该规格，则继续遍历
                if (groupEntity.getValue_ids().contains(specs.get(i).getValue_id())) {

                    //直到满足所有已选的规格，则为选中的组合
                    if (i == specs.size() - 1) {
                        groupEntity.setItem_type(goodEntity.getItem_type());
                        choicePrice = groupEntity;
                        mBinding.tvChoicePrice.setText(choicePrice.getShowPrice());
                        mBinding.tvStock.setText(("库存：" + choicePrice.getStock()));
                        if (!TextUtils.isEmpty(choicePrice.getImg_url())) {
                            GlideShowImageUtils.displayNetImage(getContext(), choicePrice.getImg_url(), mBinding.ivGoodPic);
                        }
//                        mBinding.tvChoice.setText(("已选：" + choicePrice.getValue_names()));
                        setNumber(getNum() > choicePrice.getStock() ? (int) choicePrice.getStock() : getNum());
                        return;
                    }

                }
                // 有一个规格不符合，就不是该组合
                else {
                    break;
                }
            }

        }

        choicePrice = null;

    }


    private List<SpecsGroupEntity> allGroupList = new ArrayList<>();

    private void doListSpecsGroup() {
        RetrofitApiFactory.createApi(StoreApi.class)
                .listSpecsGroup(goodEntity.getItem_id())
                .compose(RxSchedulers.<BaseData<List<SpecsGroupEntity>>>compose())
                .compose((this).<BaseData<List<SpecsGroupEntity>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<SpecsGroupEntity>>>() {

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<List<SpecsGroupEntity>> data) {
                        allGroupList = data.getData();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }

    private List<SpecsEntity> allSpecsList = new ArrayList<>();

    private void doListSpecs() {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .listGoodsSpecs(goodEntity.getItem_id())
                .compose(RxSchedulers.<BaseData<List<SpecsEntity>>>compose())
                .compose((this).<BaseData<List<SpecsEntity>>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData<List<SpecsEntity>>>() {

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                        onStop();
                    }

                    @Override
                    public void onSuccess(BaseData<List<SpecsEntity>> data) {
                        allSpecsList = data.getData();
                        adapter.setData(allSpecsList);
                        doListSpecsGroup();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

    private void doAddShopCart() {
        showProgress("");
        RetrofitApiFactory.createApi(StoreApi.class)
                .addShopCart(choicePrice.getIvid(), mBinding.tvNum.getText().toString())
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new XObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData data) {
                        ToastUtils.showShort("添加成功");
                        EventBus.getDefault().post(new ShopCartEvent());
                        onStop();
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });

    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onStop();
                return true;
        }
        return false;
    }
}
