package com.liemi.basemall.ui.store.good;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.data.entity.good.ShareImgEntity;
import com.liemi.basemall.databinding.ActivityShareGoodBinding;
import com.lzy.imagepicker.ImagePicker;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.ShareEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.widget.ShareDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.liemi.basemall.data.entity.good.GoodsDetailedEntity.GOODS_ENTITY;
import static com.netmi.baselibrary.data.Constant.SHARE_GOOD;
import static com.netmi.baselibrary.data.Constant.SUCCESS_CODE;


public class ShareGoodActivity extends BaseActivity<ActivityShareGoodBinding> {

    private String url = "";

    private GoodsDetailedEntity entity;

    @Override
    protected int getContentView() {
        return R.layout.activity_share_good;
    }

    @Override
    protected void initUI() {
        entity = (GoodsDetailedEntity) getIntent().getSerializableExtra(GOODS_ENTITY);
        if (entity == null) {
            ToastUtils.showShort("没有商品详情");
            finish();
            return;
        }
        doGetImgUrl();
        getTvTitle().setText("商品分享");
        ((ImageView) findViewById(R.id.iv_setting)).setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_black_share));
        ((ImageView) findViewById(R.id.iv_setting)).setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.iv_setting) {
            share();
        } else if (view.getId() == R.id.tv_share) {
            share();
        } else if (view.getId() == R.id.tv_save) {
            if (!TextUtils.isEmpty(url)) {
                showProgress("下载中...");

                Glide.with(this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        saveBitmapToFile(resource);
                    }
                });
            } else {
                ToastUtils.showShort("没有可保存的图片！");
            }
        } else if (view.getId() == R.id.iv_share) {
            if (!TextUtils.isEmpty(url)) {
                ArrayList<String> list = new ArrayList<>(1);
                list.add(url);
                JumpUtil.overlayImagePreview(this, list, 0);
            }
        }
    }


    private void share() {
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.setActivity(this);
        shareEntity.setLinkUrl(SHARE_GOOD + entity.getItem_id());
        shareEntity.setTitle(entity.getTitle());
        shareEntity.setContent(entity.getRemark());
        shareEntity.setImgUrl(entity.getImg_url());
        new ShareDialog(this, shareEntity).showDialog();
    }

    public void saveBitmapToFile(final Bitmap croppedImage) {
        Observable.create(new ObservableOnSubscribe<File>() {
            public void subscribe(ObservableEmitter<File> oe) throws Exception {
                Bitmap.CompressFormat outputFormat = Bitmap.CompressFormat.JPEG;
                File saveFile = createFile(ImagePicker.getInstance().getSaveImageFile(getContext()), "IMG_", ".jpg");
                OutputStream outputStream = null;

                try {
                    outputStream = getContentResolver().openOutputStream(Uri.fromFile(saveFile));
                    if (outputStream != null) {
                        croppedImage.compress(outputFormat, 90, outputStream);
                    }

                    oe.onNext(saveFile);
                } catch (IOException var14) {
                    var14.printStackTrace();
                    oe.onError(var14);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException var13) {
                            var13.printStackTrace();
                        }
                    }

                    oe.onComplete();
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<File>() {
            public void onSubscribe(Disposable d) {
            }

            public void onNext(File value) {
                ImagePicker.galleryAddPic(getContext(), value);
                String path = value.getAbsolutePath();
                ToastUtils.showShort("已保存至  " + path.substring(0, path.lastIndexOf(File.separator)) + " 文件夹");
            }

            public void onError(Throwable e) {
                ToastUtils.showShort("保存失败，请稍候再试!");
            }

            public void onComplete() {
                hideProgress();
            }
        });
    }

    private File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }

        try {
            File nomedia = new File(folder, ".nomedia");
            if (!nomedia.exists()) {
                nomedia.createNewFile();
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }


    private void doGetImgUrl() {
        showProgress("");
        RetrofitApiFactory.createApi(MineApi.class)
                .getShareImg(entity.getItem_id())
                .compose(RxSchedulers.<BaseData<ShareImgEntity>>compose())
                .compose((this).<BaseData<ShareImgEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShareImgEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShareImgEntity> data) {
                        url = data.getData().getShare_img();
                        GlideShowImageUtils.displayNetImage(getContext(), url, mBinding.ivShare, R.drawable.baselib_bg_null);
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }
                });

    }

}