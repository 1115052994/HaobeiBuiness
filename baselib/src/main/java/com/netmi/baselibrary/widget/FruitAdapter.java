package com.netmi.baselibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netmi.baselibrary.R;

import java.util.List;

public class FruitAdapter extends BaseAdapter {
    private LayoutInflater mInflater=null;
    private List<String> item=null;
    private int mResource;

    public FruitAdapter(Context context, int mResource, List<String> item ) {
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item = item;
        this.mResource = mResource;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        //创建一个视图并返回
        View v=null;
        if(convertView==null)
        {   //用context给的布局填充器创建视图
            v=mInflater.inflate(mResource,viewGroup,false);
        }else{
            v=convertView;
        }
        //为视图填充相应的数据(可以封装成一个bindView(int position, View view)方法)
        ((TextView)v.findViewById(R.id.top_text_name1)).setText(item.get(i));
        return v;
    }
    public void setData(List<String> item){
        this.item= item;
        notifyDataSetChanged();
    }
}
