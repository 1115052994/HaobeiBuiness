package com.liemi.basemall.ui.shopcar;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.databinding.ItemShopCartGoodBinding;
import com.liemi.basemall.ui.store.good.GoodDetailActivity;
import com.liemi.basemall.widget.ChildCheckListener;
import com.liemi.basemall.widget.ShopCartCallback;
import com.netmi.baselibrary.BR;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;

import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;

/**
 * Created by Bingo on 2018/11/27.
 */

public class ShopGoodAdapter  extends BaseRViewAdapter<GoodsDetailedEntity, BaseViewHolder> {

    private ShopCartCallback shopCartCallback;
    private ChildCheckListener checkListener;

    protected ShopGoodAdapter(Context context, @NonNull ShopCartCallback shopCartCallback,@NonNull ChildCheckListener checkListener) {
        super(context);
        this.shopCartCallback=shopCartCallback;
        this.checkListener=checkListener;
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.item_shop_cart_good;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder(binding) {

            @Override
            public ItemShopCartGoodBinding getBinding() {
                return (ItemShopCartGoodBinding) super.getBinding();
            }

            private void setNum(float num) {
                getBinding().tvMinus.setEnabled(num > 1);
                getBinding().tvPlus.setEnabled(num < Strings.toLong(getItem(position).getStock()));
                getBinding().tvCalculate.setText(Strings.twoDecimal(num));
//                        getBinding().etCalculate.setSelection(getBinding().etCalculate.getText().length());
            }


            @Override
            public void bindData(Object item) {
                final ItemShopCartGoodBinding goodBinding = getBinding();
                goodBinding.setIsEdit(true);
                goodBinding.setCheckedListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        getItem(position).setChecked(b);
                        checkListener.childCheck();
                        shopCartCallback.calcuCount();
                    }
                });
                setNum(Strings.toFloat(getItem(position).getNum()));
                final TextView etCalculate = goodBinding.tvCalculate;
                goodBinding.setDoClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = v.getId();
                        if (i == R.id.tv_minus) {
                            setNum(Strings.toFloat(etCalculate.getText().toString()) - 1);

                        } else if (i == R.id.tv_plus) {
                            setNum(Strings.toFloat(etCalculate.getText().toString()) + 1);

                        } else if (i == R.id.iv_good || i == R.id.ll_good_detail) {
                            JumpUtil.overlay(context, GoodDetailActivity.class,
                                    ITEM_ID, getItem(position).getItem_id());

                        } else {
                        }
                    }
                });

                etCalculate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s.toString())) {
                            setNum(1f);
                        } else if (!s.toString().equals(Strings.twoDecimal(getItem(position).getNum()))) {
                            if (Strings.toFloat(s.toString()) > Strings.toDouble(getItem(position).getStock())) {
                                ToastUtils.showShort("购买数量不能超出库存");
                                setNum(Strings.toFloat(getItem(position).getStock()));
                            } else {
                                shopCartCallback.doUpdateCartNum(getItem(position), Strings.toFloat(s.toString()));
                            }
                        }
                    }
                });

                goodBinding.setVariable(BR.position, position);
                goodBinding.setVariable(getVariableId(), item);

                //当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
                goodBinding.executePendingBindings();
            }

        };
    }
}
