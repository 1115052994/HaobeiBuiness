package com.netmi.baselibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.netmi.baselibrary.utils.Densitys;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/3/1 12:17
 * 修改备注：
 */
public class FRecyclerView extends MyXRecyclerView {

    public FRecyclerView(Context context) {
        this(context, null);
    }

    public FRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View mFootView = getDefaultFootView();
        if (mFootView != null) {
            mFootView.setPadding(0, Densitys.dp2px(context, 8), 0, Densitys.dp2px(context, 8));
        }
        setFootViewText("正在加载...", "--我是有底线的--");
    }

}
