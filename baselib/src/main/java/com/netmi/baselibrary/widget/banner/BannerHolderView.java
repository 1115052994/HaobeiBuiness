package com.netmi.baselibrary.widget.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/17 14:17
 * 修改备注：
 */
public class BannerHolderView<T> extends Holder<T> {

    private T data;

    private ImageView ivBanner;

    public BannerHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        ivBanner = itemView.findViewById(R.id.iv_banner);
    }

    @Override
    public void updateUI(T data) {
        if (data == null) return;

        String imageUrl = null;
        if (data instanceof BannerEntity) {
            imageUrl = ((BannerEntity) data).getImg_url();
        } else {
            imageUrl = (String) data;
        }

        if (ivBanner != null) {
            GlideShowImageUtils.displayNetImage(ivBanner.getContext(), imageUrl, ivBanner, R.drawable.baselib_bg_default_pic,8);

        }
    }
}
