package com.netmi.workerbusiness.ui.home.commodity.offline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.liemi.basemall.contract.FileContract;
import com.liemi.basemall.presenter.CompressPresenterImpl;
import com.liemi.basemall.presenter.FilePresenterImpl;
import com.liemi.basemall.widget.PhotoAdapter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.util.ImageItemUtil;
import com.mob.tools.utils.FileUtils;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.KeyboardUtils;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.OfflineGoodApi;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity.CreateGoodCommand;
import com.netmi.workerbusiness.data.entity.home.offlinecommodity.OfflineGoodDetailEntity;
import com.netmi.workerbusiness.data.event.ShelfUpdateEvent;
import com.netmi.workerbusiness.databinding.ActivityCreateOfflineCommodityBinding;
import com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity;
import com.netmi.workerbusiness.ui.home.commodity.online.GoodDetailRichTextActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;
import static com.netmi.workerbusiness.ui.home.commodity.category.SetTagActivity.TAG_NAME;
import static com.netmi.workerbusiness.ui.home.commodity.offline.PurchaseNotesActivity.PURCHASENOTE;
import static com.netmi.workerbusiness.ui.home.commodity.online.CreateCommodityActivity.RICHTEXT_DETAIL;
import static com.netmi.workerbusiness.ui.home.commodity.online.GoodDetailRichTextActivity.RICH_TEXT_STR;

public class CreateOfflineCommodityActivity extends BaseActivity<ActivityCreateOfflineCommodityBinding> implements FileContract.FilesUpView, CompressPresenterImpl.CompressView {
    public final static int RICHTEXT_DETAIL = 7;
    public static final int PUCHASE = 9;

    //TYPE表示从哪个页面进入 1表示创建商品 2表示编辑商品
    private int type;
    private PhotoAdapter photoAdapter;
    private ArrayList<ImageItem> images;
    private List<String> uploadUrls = new ArrayList<>();
    private List<String> compressPaths;
    private FilePresenterImpl filePresenter;
    private CompressPresenterImpl compressPresenter;
    private String richtext = "";
    private String startTime = "";
    private String endTime = "";
    //    private String hour;
    private String rule = "";
    //状态： 2上架待审核7已下架
    private String status;
    // 商品状态： 2上架待审核 5已上架 7已下架
    private int goodStaus;

    private OfflineGoodDetailEntity entity;

    @Override
    protected int getContentView() {
        return R.layout.activity_create_offline_commodity;
    }

    @Override
    protected void initUI() {
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 2) {
            getTvTitle().setText("编辑商品");
            //2上架待审核 5已上架 7已下架
            goodStaus = getIntent().getExtras().getInt(JumpUtil.CODE);
            if (goodStaus == 5) { //上架状态，存入待审核列表
                mBinding.tvUpperShelf.setText("保存");
                mBinding.tvUpperShelf.setTextColor(getResources().getColor(R.color.white));
                mBinding.tvUpperShelf.setBackgroundColor(getResources().getColor(R.color.color_e60012));
                mBinding.tvSavePendingShelf.setVisibility(View.GONE);
            } else if (goodStaus == 7) { //下架状态存入已下架列表
                mBinding.tvSavePendingShelf.setText("保存");
                mBinding.tvSavePendingShelf.setTextColor(getResources().getColor(R.color.white));
                mBinding.tvSavePendingShelf.setBackgroundColor(getResources().getColor(R.color.color_e60012));
                mBinding.tvUpperShelf.setVisibility(View.GONE);
            }

        } else {
            getTvTitle().setText("创建商品");
        }
        mBinding.rvPic.setLayoutManager(new GridLayoutManager(this, 4));
        photoAdapter = new PhotoAdapter(getContext());
        photoAdapter.setMax(10);
        mBinding.rvPic.setAdapter(photoAdapter);

        filePresenter = new FilePresenterImpl().setFilesUpView(this);
        compressPresenter = new CompressPresenterImpl().setCompressView(this);


        if (type == 2) {
            entity = (OfflineGoodDetailEntity) getIntent().getExtras().getSerializable(JumpUtil.VALUE);
            mBinding.tvGoodName.setText(entity.getTitle());
            photoAdapter.setData(entity.getImg_url());
            mBinding.etOldPrice.setText(entity.getOld_price());
            mBinding.etNowPrice.setText(entity.getPrice());
            mBinding.etStock.setText(entity.getStock());
            mBinding.etSale.setText(entity.getDeal_num_false());
            mBinding.etSort.setText(entity.getSort());
            rule = entity.getPurchase_note();
            richtext = entity.getRich_text();
            startTime = entity.getStart_date();
            endTime = entity.getEnd_date();
            if (entity.getImg_url().size() > 0) {
                images = new ArrayList<>();
                List<String> imgUrls = new ArrayList<>();
                for (int i = 0; i < entity.getImg_url().size(); i++) {
                    ImageItem item = new ImageItem();
                    item.path = entity.getImg_url().get(i);
                    imgUrls.add(item.path);
                    images.add(item);
                }
                photoAdapter.setData(imgUrls);
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.ll_good_detail) {//商品详情
            Bundle bundleRichtext = new Bundle();
            if (!richtext.equals("")) {
                bundleRichtext.putString(RICH_TEXT_STR, richtext);
            }
            JumpUtil.startForResult(getActivity(), GoodDetailRichTextActivity.class, RICHTEXT_DETAIL, bundleRichtext);
        } else if (id == R.id.ll_purchase_notes) {//购买须知
            Bundle puchaseNote = new Bundle();
            if (type == 2) {
                entity.setPurchase_note(rule);
                entity.setStart_date(startTime);
                entity.setEnd_date(endTime);
                puchaseNote.putSerializable(JumpUtil.VALUE, entity);
                puchaseNote.putInt(JumpUtil.TYPE, type);
            } else if (type == 1) {
                if (!rule.equals("") && !startTime.equals("") && !endTime.equals("")) {
                    puchaseNote.putInt(JumpUtil.TYPE, type);
                    puchaseNote.putString(JumpUtil.CODE, rule);
                    puchaseNote.putString(JumpUtil.VALUE, startTime);
                    puchaseNote.putString(JumpUtil.FLAG, endTime);
                    puchaseNote.putInt(JumpUtil.ID, check);
                }
            }
            JumpUtil.startForResult(getActivity(), PurchaseNotesActivity.class, PUCHASE, puchaseNote);
        } else if (id == R.id.tv_save_pending_shelf) {//存入已下架
            if (checkContent()) {
                status = "7";
                Log.e("weng", images.toString());
                compressPresenter.compressFiles(images);
            }
        } else if (id == R.id.tv_upper_shelf) {//上架待审核
            status = "2";
            if (checkContent()) {
                Log.e("weng", images.toString());
                compressPresenter.compressFiles(images);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                }
                Log.e("weng", images.toString());
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    photoAdapter.setData(ImageItemUtil.ImageItem2String(images));
                }
            }
        } else if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == RICHTEXT_DETAIL) {
                    //获取富文本内容
                    richtext = data.getStringExtra(RICH_TEXT_STR);
                } else if (requestCode == PUCHASE) {
                    //购买须知返回数据
                    Bundle args = data.getExtras().getBundle(PURCHASENOTE);
                    startTime = args.getString(JumpUtil.VALUE);
                    endTime = args.getString(JumpUtil.TYPE);
//                    hour = data.getExtras().getString(JumpUtil.ID);
                    rule = args.getString(JumpUtil.FLAG);
                    check = args.getInt(JumpUtil.ID);
                }
            }
        }
    }

    private int check; //0否1是；

    public boolean checkContent() {
        if (mBinding.tvGoodName.getText().toString().isEmpty()) {
            ToastUtils.showShort("请输入商品名称");
            return false;
        } else if (photoAdapter.getItemCount() == 0) {
            ToastUtils.showShort("请上传商品图片");
            return false;
        } else if (photoAdapter.getItemCount() < 1) {
            ToastUtils.showShort("商品图片至少上传一张");
            return false;
        } else if (mBinding.etOldPrice.getText().toString().isEmpty()) {
            ToastUtils.showShort("请填写原价");
            return false;
        } else if (mBinding.etNowPrice.getText().toString().isEmpty()) {
            ToastUtils.showShort("请填写现价");
            return false;
        } else if (mBinding.etStock.getText().toString().isEmpty()) {
            ToastUtils.showShort("填写库存");
            return false;
        } else if (TextUtils.isEmpty(richtext)) {
            ToastUtils.showShort("请编辑商品详情");
            return false;
//        } else if (TextUtils.isEmpty(rule)) {
//            ToastUtils.showShort("请填写购买须知");
//            return false;
        } else if (TextUtils.isEmpty(mBinding.etSale.getText())) {
            ToastUtils.showShort("请填写虚拟销量");
            return false;
        } else if (TextUtils.isEmpty(mBinding.etSort.getText())) {
            ToastUtils.showShort("请填写排序");
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void filesUpSuccess(List<String> imageUrl) {
        uploadUrls.addAll(imageUrl);
        if (compressPaths != null && compressPaths.size() > 0) {
            for (String compressPath : compressPaths) {
                FileUtils.deleteFile(compressPath);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //创建商品
                if (type == 1) {
                    //创建商品
                    doCreate(mBinding.tvGoodName.getText().toString(), uploadUrls, mBinding.etNowPrice.getText().toString(), mBinding.etOldPrice.getText().toString()
                            , mBinding.etStock.getText().toString(), mBinding.etSale.getText().toString(), mBinding.etSort.getText().toString()
                            , status, richtext, rule, startTime, endTime);
                } else if (type == 2) {
                    //编辑商品
                    doGetUpdateShelf((String) getIntent().getExtras().get(JumpUtil.ID), mBinding.tvGoodName.getText().toString(), uploadUrls, mBinding.etNowPrice.getText().toString(), mBinding.etOldPrice.getText().toString()
                            , mBinding.etStock.getText().toString(), mBinding.etSale.getText().toString(), mBinding.etSort.getText().toString()
                            , status, richtext, rule, startTime, endTime);
                }
            }
        });
    }

    @Override
    public void filesUpFailure(String msg) {
        if (compressPaths != null && compressPaths.size() > 0) {
            for (String compressPath : compressPaths) {
                FileUtils.deleteFile(compressPath);
            }
        }
        ToastUtils.showShort(msg);
    }


    @Override
    public void compressSuccess(List<String> compressPaths) {
        this.compressPaths = compressPaths;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                filePresenter.filesUp(CreateOfflineCommodityActivity.this.compressPaths);
            }
        });
    }

    @Override
    public void compressFailure(String msg) {
        ToastUtils.showShort(msg);
    }

    //新建商品
    public void doCreate(String title, List<String> img_url, String price, String old_price, String stock, String deal_num_false, String sort
            , String status, String rich_text, String purchase_note, String start_date, String end_date) {
        RetrofitApiFactory.createApi(OfflineGoodApi.class)
                .getCreateGood(title, img_url, price, old_price, stock, deal_num_false, sort, status, rich_text, purchase_note, start_date, end_date)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("创建成功");
                            finish();
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    //编辑商品
    public void doGetUpdateShelf(String id, String title, List<String> img_url, String price, String old_price, String stock, String deal_num_false, String sort
            , String status, String rich_text, String purchase_note, String start_date, String end_date) {
        RetrofitApiFactory.createApi(OfflineGoodApi.class)
                .getUpdateGood(id, title, img_url, price, old_price, stock, deal_num_false, sort, status, rich_text, purchase_note, start_date, end_date)
                .compose(RxSchedulers.<BaseData>compose())
                .compose((this).<BaseData>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseObserver<BaseData>() {
                    @Override
                    protected void onError(ApiException ex) {
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull BaseData data) {
                        if (data.getErrcode() == Constant.SUCCESS_CODE) {
                            ToastUtils.showShort("编辑成功");
//                            EventBus.getDefault().post(new ShelfUpdateEvent());
                            finish();
                        } else {
                            showError(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (filePresenter != null) {
            filePresenter.destroy();
        }
        if (compressPresenter != null) {
            compressPresenter.destroy();
        }
    }


}
