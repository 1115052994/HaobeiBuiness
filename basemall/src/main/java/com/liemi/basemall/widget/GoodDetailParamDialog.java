package com.liemi.basemall.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liemi.basemall.R;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;


/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/14 18:37
 * 修改备注：
 */
public class GoodDetailParamDialog extends Dialog {

    private GoodsDetailedEntity goodEntity;

    public GoodDetailParamDialog(Context context, GoodsDetailedEntity goodEntity) {
        super(context, R.style.showDialog);
        this.goodEntity = goodEntity;
        initUI();
    }

    /**
     * 对话框布局初始化
     */
    private void initUI() {

        setContentView(R.layout.dialog_good_detail_param);

        RecyclerView rvParam = findViewById(R.id.rv_param);
        rvParam.setLayoutManager(new LinearLayoutManager(getContext()));
        BaseRViewAdapter<GoodsDetailedEntity.MeNaturesBean, BaseViewHolder> adapter;
        rvParam.setAdapter(adapter = new BaseRViewAdapter<GoodsDetailedEntity.MeNaturesBean, BaseViewHolder>(getContext()) {
            @Override
            public int layoutResId(int position) {
                return R.layout.item_dialog_good_param;
            }

            @Override
            public BaseViewHolder holderInstance(ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                };
            }

        });
        adapter.setData(goodEntity.getMeNatures());

        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 显示
     */
    public void showDialog() {
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setAttributes(lp);
            dialogWindow.setWindowAnimations(R.style.dialog_bottom_anim_style);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
        show();
    }


}
