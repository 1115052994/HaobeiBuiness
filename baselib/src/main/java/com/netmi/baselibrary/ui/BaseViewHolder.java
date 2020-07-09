package com.netmi.baselibrary.ui;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.netmi.baselibrary.BR;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/4 15:36
 * 修改备注：
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public int position;

    private ViewDataBinding binding;

    protected BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(T item) {
        binding.setVariable(BR.position, position);
        binding.setVariable(getVariableId(), item);
        binding.setVariable(getListenerId(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doClick(view);
            }
        });

        //当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
        binding.executePendingBindings();
    }

    public void doClick(View view) {

    }

    public int getVariableId() {
        return BR.item;
    }

    public int getListenerId() {
        return BR.doClick;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    @Override
    public void onClick(View view) {

    }
}
