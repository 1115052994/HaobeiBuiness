package com.liemi.basemall.ui.category;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.good.GoodsListEntity;
import com.liemi.basemall.databinding.ItemMallGoodBinding;
import com.liemi.basemall.ui.store.good.GoodDetailActivity;
import com.liemi.basemall.ui.store.good.SpecsTagAdapter;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.zhy.view.flowlayout.FlowLayout;

import static com.liemi.basemall.ui.store.good.BaseGoodsDetailedActivity.ITEM_ID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/12/4
 * 修改备注：
 */
public class GoodsListAdapter extends BaseRViewAdapter<GoodsListEntity, BaseViewHolder> {

    public GoodsListAdapter(Context context) {
        super(context);
    }

    public GoodsListAdapter(Context context, MyXRecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public int layoutResId(int position) {
        return R.layout.item_mall_good;
    }

    @Override
    public BaseViewHolder holderInstance(ViewDataBinding binding) {
        return new BaseViewHolder<GoodsListEntity>(binding) {

            @Override
            public void bindData(GoodsListEntity item) {

                if (item.getMeLabels().size() > 0) {
                    getBinding().idLabel.setAdapter(new SpecsTagAdapter<GoodsListEntity.MeLabelsBean>(item.getMeLabels()) {
                        @Override
                        public View getView(FlowLayout parent, int position, GoodsListEntity.MeLabelsBean s) {
                            TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_goods_label, getBinding().idLabel, false);
                            tv.setText(s.getName());
                            return tv;
                        }
                    });
                }

                super.bindData(item);
            }

            @Override
            public ItemMallGoodBinding getBinding() {
                return (ItemMallGoodBinding) super.getBinding();
            }

            @Override
            public void doClick(View view) {
                super.doClick(view);
                JumpUtil.overlay(context, GoodDetailActivity.class,
                        ITEM_ID, getItem(position).getItem_id());
            }
        };
    }
}
