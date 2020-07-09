package com.netmi.workerbusiness.im.session.viewholder;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.im.session.extension.OrderAttachment;

public class MsgViewHolderOrder extends MsgViewHolderBase {

    private OrderAttachment attachment;

    private TextView tvTitle;
    private TextView tvOrderNo;
    private ImageView ivImg;

    public MsgViewHolderOrder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_order;
    }

    @Override
    protected void inflateContentView() {

        tvTitle=findViewById(com.netease.nim.uikit.R.id.tv_title);
        tvOrderNo=findViewById(R.id.tv_order_no);
        ivImg=findViewById(R.id.iv_img);
    }

    @Override
    protected void bindContentView() {
        attachment= (OrderAttachment) message.getAttachment();
        GlideShowImageUtils.displayNetImage(context,attachment.getImgUrl(),ivImg, com.netease.nim.uikit.R.drawable.baselib_bg_default_pic);
        tvTitle.setText(attachment.getTitle());
        tvOrderNo.setText(attachment.getOrderNo());
        Log.e("weng", attachment.getTitle() + "");
        Log.e("weng", attachment.getImgUrl() + "");
        Log.e("weng", attachment.getOrderNo() + "");
    }
}
