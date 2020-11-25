package com.netmi.workerbusiness.ui.mine;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.widget.SendMailboxDialog;

import java.util.List;

public class ContractExamineAdapter extends BaseAdapter {
    private LayoutInflater mInflater=null;
    private List<String> item=null;
    private int mResource;
    private Activity context;

    public ContractExamineAdapter(Activity context, int mResource, List<String> item ) {
        this.context = context;
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
        //查看
        v.findViewById(R.id.cb_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //发送
        v.findViewById(R.id.cb_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMailboxDialog.show(context);
            }
        });
        return v;
    }
    public void setData(List<String> item){
        this.item= item;
        notifyDataSetChanged();
    }
}
