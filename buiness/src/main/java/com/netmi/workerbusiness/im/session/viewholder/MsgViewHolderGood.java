package com.netmi.workerbusiness.im.session.viewholder;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.im.session.extension.GoodAttachment;

public class MsgViewHolderGood extends MsgViewHolderBase {

    private GoodAttachment attachment;

    private TextView tvTitle;
    private TextView tvPrice;
    private ImageView ivImg;

    public MsgViewHolderGood(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_good;
    }

    @Override
    protected void inflateContentView() {
        tvTitle = findViewById(R.id.tv_title);
        tvPrice = findViewById(R.id.tv_price);
        ivImg = findViewById(R.id.iv_img);
    }

    @Override
    protected void bindContentView() {
        attachment = (GoodAttachment) message.getAttachment();
        GlideShowImageUtils.displayNetImage(context, attachment.getImgUrl(), ivImg, com.netease.nim.uikit.R.drawable.baselib_bg_default_pic);
        tvTitle.setText(attachment.getTitle());
        tvPrice.setText("¥" + attachment.getPrice());
    }
}
