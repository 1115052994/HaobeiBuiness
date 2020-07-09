package com.liemi.basemall.widget;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.liemi.basemall.R;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/8/17 14:17
 * 修改备注：
 */
public class GoodsBannerHolderView<T> extends Holder<T> {

    private ImageView ivBanner;

    public GoodsBannerHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        ivBanner = itemView.findViewById(R.id.iv_banner);
    }

    @Override
    public void updateUI(T data) {
        String imgUrl;
        if(data instanceof BannerEntity){
            imgUrl = ((BannerEntity) data).getImg_url();
        }else{
            imgUrl = (String) data;
        }
        if (ivBanner != null) {
            GlideShowImageUtils.displayNetImage(MApplication.getAppContext(), imgUrl, ivBanner, R.drawable.baselib_bg_default_pic);
        }
    }
}
