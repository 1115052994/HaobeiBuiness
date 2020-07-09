package com.netmi.baselibrary.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.netmi.baselibrary.widget.MyXRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类描述：RecyclerView 适配基类
 * 创建人：Simple
 * 创建时间：2017/9/6 17:48
 * 修改备注：
 */
public abstract class BaseRViewAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<T> items = new ArrayList<T>();
    protected Context context;
    protected MyXRecyclerView fRecyclerView;

    protected BaseRViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * 局部更新，需要传入XRecyclerView
     */
    protected BaseRViewAdapter(Context context, MyXRecyclerView recyclerView) {
        this(context);
        this.fRecyclerView = recyclerView;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId(viewType), parent, false);
        return holderInstance(binding);
    }


    /**
     * @param viewType According to position return item layout id
     */
    public abstract int layoutResId(int viewType);

    /**
     * ViewHolder实例化
     */
    public abstract VH holderInstance(ViewDataBinding binding);

    public T getItem(int position) {
        return position < items.size() ? items.get(position) : null;
    }

    /**
     * 如果存在XRecyclerView 调用正确的更新
     */
    private boolean xrIsEmpty() {
        boolean isEmpty = fRecyclerView == null;
        if (isEmpty) {
            notifyDataSetChanged();
        }
        return isEmpty;
    }

    public MyXRecyclerView getFRecyclerView() {
        return fRecyclerView;
    }

    public void setData(List<T> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void insert(T item) {

        items.add(getItemCount(), item);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, getItemCount());
        }
    }

    public void insert(List<T> items) {

        this.items.addAll(getItemCount(), items);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, getItemCount());
        }
    }

    public void insert(int position, T item) {

        items.add(position, item);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, position);
        }
    }

    public void insert(int position, List<T> items) {

        this.items.addAll(position, items);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemInserted(items, position);
        }
    }

    public void remove(int position) {
        if(position > -1 && position < getItemCount()) {
            T remove = items.remove(position);
            if (remove != null) {
                if (!xrIsEmpty()) {
                    getFRecyclerView().notifyItemRemoved(items, position);
                }
            }
        }
    }

    public void remove(T item) {

        for (int i = 0; i < items.size(); i++) {
            T temp = items.get(i);
            if (temp.equals(item)) {
                remove(i);
                i--;
            }
        }
    }

    public void replace(int position, T item) {
        Collections.replaceAll(items, getItem(position), item);
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemChanged(position);
        }
    }

    public void notifyPosition(int position) {
        if (!xrIsEmpty()) {
            getFRecyclerView().notifyItemChanged(position);
        }
    }

    public void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<T> getItems() {
        return items;
    }

    /**
     * 绑定ItemView事件
     */
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        if (holder.itemView != null && !(holder.itemView instanceof AdapterView)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.doClick(view);
                }
            });
        }
        holder.position = position;
        holder.bindData(getItem(position));
    }
}