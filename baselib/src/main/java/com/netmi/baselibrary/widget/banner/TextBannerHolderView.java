package com.netmi.baselibrary.widget.banner;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.entity.AnnouncementEntity;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2019/8/12
 * 修改备注：
 */
public class TextBannerHolderView<T> extends Holder<T> {

    private T data;

    private TextView tvBanner;

    public TextBannerHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        tvBanner = itemView.findViewById(R.id.tv_banner);
    }

    @Override
    public void updateUI(T data) {
        if (data == null) return;

        String title = null;
        if (data instanceof BannerEntity) {
            title = ((AnnouncementEntity) data).getTitle();
        } else {
            title = (String) data;
        }

        if (tvBanner != null) {
            tvBanner.setText(title);
           // tvBanner.setTextColor(0xFFFFFF);
            // tvBanner.setGravity(Gravity.CENTER_VERTICAL);
        }
    }
}
