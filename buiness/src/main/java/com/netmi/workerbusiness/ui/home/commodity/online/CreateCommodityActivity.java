package com.netmi.workerbusiness.ui.home.commodity.online;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.StoreApi;
import com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity.CreateGoodCommand;
import com.netmi.workerbusiness.data.entity.home.store.SpecDetailEntity;
import com.netmi.workerbusiness.databinding.ActivityCreatCommodityBinding;
import com.netmi.workerbusiness.ui.home.commodity.category.SelectCategoryActivity;
import com.netmi.workerbusiness.ui.home.commodity.category.SetTagActivity;
import com.netmi.workerbusiness.ui.home.commodity.category.spec.SelectCategorySpecificationActivity;
import com.netmi.workerbusiness.ui.home.commodity.coupon.ServiceDesciptionActivity;
import com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_SELECT;
import static com.netmi.workerbusiness.ui.home.commodity.category.SelectCategoryActivity.ID_LIST;
import static com.netmi.workerbusiness.ui.home.commodity.category.SelectCategoryActivity.NAME_LIST;
import static com.netmi.workerbusiness.ui.home.commodity.category.SetTagActivity.TAG_NAME;
import static com.netmi.workerbusiness.ui.home.commodity.coupon.ServiceDesciptionActivity.SERVICE_DES;
import static com.netmi.workerbusiness.ui.home.commodity.online.GoodDetailRichTextActivity.RICH_TEXT_STR;
import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity.FROM_CREATE;
import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity.JUMP_FROM;
import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity.POST_TEMPLE_ID;
import static com.netmi.workerbusiness.ui.home.commodity.postage.PostageEditorActivity.POST_TEMPLE_NAME;

public class CreateCommodityActivity extends BaseActivity<ActivityCreatCommodityBinding> implements FileContract.FilesUpView, CompressPresenterImpl.CompressView {

    public final static String CATEGORY_OR_SPECIFICATION = "categroy_or_specification";
    public final static String TAG_ONE_OR_TWO = "tag_one_or_two";
    public final static String CREATE_GOOD_FROM = "create_good_from";
    public final static String STORE_RESULT = "store_result";
    public final static String SHELF_LIST = "shelf_list";
    public final static String CREATE_GOOD_DETAI = "create_good_detail";
    public final static String SHOP_ID = "shop_id";
    public final static String GOOD_ID = "good_id";
    public final static int CATEGORY = 1;
    public final static int SPECIFICATION = 2;
    public final static int GROUP = 3;
    public final static int POSTAGE_TEMPLE = 4;
    public final static int TAG_ONE = 5;
    public final static int TAG_TWO = 6;
    public final static int RICHTEXT_DETAIL = 7;
    public final static int SERVICE_DESCRIPTION = 8;

    /**
     * TYPE表示从哪个页面进入 1表示创建商品 2表示编辑商品
     */
    private int type;

    private PhotoAdapter photoAdapter;
    private List<String> uploadUrls;
    private List<String> compressPaths;
    private ArrayList<ImageItem> images;
    private FilePresenterImpl filePresenter;
    private CompressPresenterImpl compressPresenter;

    private CreateGoodCommand createGoodCommand = new CreateGoodCommand();
    private List<String> tagList = new ArrayList<>();
    private String lable;
    private int upper_shelf = -1;
    private List<String> nature_list = new ArrayList<>();  //服务描述


    @Override
    protected int getContentView() {
        return R.layout.activity_creat_commodity;
    }

    @Override
    protected void initUI() {
        type = getIntent().getExtras().getInt(JumpUtil.TYPE);
        if (type == 2) {
            getTvTitle().setText("编辑商品");
            mBinding.tvSavePendingShelf.setText("保存");
            mBinding.tvSavePendingShelf.setTextColor(getResources().getColor(R.color.white));
            mBinding.tvSavePendingShelf.setBackgroundColor(getResources().getColor(R.color.color_e60012));
            mBinding.tvUpperShelf.setVisibility(View.GONE);
        } else {
            getTvTitle().setText("创建商品");
        }
        mBinding.rvPic.setLayoutManager(new GridLayoutManager(this, 4));

        photoAdapter = new PhotoAdapter(getContext());
        photoAdapter.setMax(10);
        mBinding.rvPic.setAdapter(photoAdapter);
        uploadUrls = new ArrayList<>();
        filePresenter = new FilePresenterImpl().setFilesUpView(this);
        compressPresenter = new CompressPresenterImpl().setCompressView(this);
        tagList.add("");
        tagList.add("");
        if (getIntent().getStringExtra(CREATE_GOOD_FROM) != null) {
            if (TextUtils.equals(getIntent().getStringExtra(CREATE_GOOD_FROM), SHELF_LIST)) {
                createGoodCommand = (CreateGoodCommand) getIntent().getSerializableExtra(CREATE_GOOD_DETAI);
                mBinding.tvGoodName.setText(createGoodCommand.getTitle());
                mBinding.etSale.setText(createGoodCommand.getDeal_num_false());
                mBinding.etOldPrice.setText(createGoodCommand.getOld_price());
                mBinding.etNowPrice.setText(createGoodCommand.getPrice());
                mBinding.etSort.setText(createGoodCommand.getSort());
                photoAdapter.setData(createGoodCommand.getItemImgs());
                if (createGoodCommand.getItemImgs().size() > 0) {
                    images = new ArrayList<>();
                    List<String> imgUrls = new ArrayList<>();
                    for (int i = 0; i < createGoodCommand.getItemImgs().size(); i++) {
                        ImageItem item = new ImageItem();
                        item.path = createGoodCommand.getItemImgs().get(i);
                        imgUrls.add(item.path);
                        images.add(item);
                    }
                    photoAdapter.setData(imgUrls);
                }
                mBinding.tvCategroy.setText(createGoodCommand.getCategoryStr());
//                mBinding.tvGroup.setText(createGoodCommand.getGroup_name());
                if(createGoodCommand.getTemplate_list()!=null){
                    mBinding.tvPostageTemplate.setText(createGoodCommand.getTemplate_list().getTemplate_name());
                }

                createGoodCommand.setItem_id(getIntent().getStringExtra(GOOD_ID));
                String label = createGoodCommand.getMeLabel();
                mBinding.tvTagOne.setText(label);
//                List<String> labels = createGoodCommand.getLabel_list();
//                if (labels.size() == 1) {
//                    mBinding.tvTagOne.setText(labels.get(0));
//                    tagList.add(labels.get(0));
//                } else if (labels.size() == 2) {
//                    mBinding.tvTagOne.setText(labels.get(0));
//                    mBinding.tvTagTwo.setText(labels.get(1));
//                    tagList.add(labels.get(0));
//                    tagList.add(labels.get(1));
//                }
            }
        } else {
            ToastUtils.showShort("未找到商品参数");
            finish();
        }
        mBinding.tvGoodName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                createGoodCommand.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.ll_select_categroy) {//选择类目
            Bundle bundleCategory = new Bundle();
            bundleCategory.putString(CATEGORY_OR_SPECIFICATION, "category");
            if (createGoodCommand != null && createGoodCommand.getCate_list() != null && createGoodCommand.getCate_list().size() > 0) {
                bundleCategory.putStringArrayList(ID_LIST, (ArrayList<String>) createGoodCommand.getCate_list());
            }
            JumpUtil.startForResult(getActivity(), SelectCategoryActivity.class, CATEGORY, bundleCategory);
            //            case R.id.ll_group:
//                //选择分组
//                Bundle bundleGroup = new Bundle();
//                bundleGroup.putString(SHOP_ID, getIntent().getStringExtra(SHOP_ID));
//                if (createGoodCommand != null && createGoodCommand.getGroup() != null && createGoodCommand.getGroup().size() > 0) {
//                    bundleGroup.putStringArrayList(ID_LIST, (ArrayList<String>) createGoodCommand.getGroup());
//                }
//                JumpUtil.startForResult(getActivity(), SelectGroupActivity.class, GROUP, bundleGroup);
//                break;
        } else if (id == R.id.ll_postage_template) {//选择运费模板
            Bundle bundle = new Bundle();
            bundle.putString(JUMP_FROM, FROM_CREATE);
            JumpUtil.startForResult(getActivity(), PostageEditorActivity.class, POSTAGE_TEMPLE, bundle);
        } else if (id == R.id.ll_tag_one) {//标签一
            Bundle bundleTagOne = new Bundle();
            bundleTagOne.putString(TAG_ONE_OR_TWO, "1");
            JumpUtil.startForResult(getActivity(), SetTagActivity.class, TAG_ONE, bundleTagOne);
            //            case R.id.ll_tag_two:
//                //标签二
//                Bundle bundleTagTwo = new Bundle();
//                bundleTagTwo.putString(TAG_ONE_OR_TWO, "2");
//                JumpUtil.startForResult(getActivity(), SetTagActivity.class, TAG_TWO, bundleTagTwo);
//                break;
        } else if (id == R.id.ll_good_detail) {//商品详情
            Bundle bundleRichtext = new Bundle();
            if (createGoodCommand != null && createGoodCommand.getRich_text() != null) {
                bundleRichtext.putString(RICH_TEXT_STR, createGoodCommand.getRich_text());
            }
            JumpUtil.startForResult(getActivity(), GoodDetailRichTextActivity.class, RICHTEXT_DETAIL, bundleRichtext);
        } else if (id == R.id.ll_specifications) {//商品规格
            Bundle bundleSpecification = new Bundle();
            bundleSpecification.putInt(JumpUtil.TYPE, 1);
            bundleSpecification.putString(JumpUtil.ID, createGoodCommand.getItem_id());
            bundleSpecification.putString(CATEGORY_OR_SPECIFICATION, "specification");
            if (createGoodCommand.getItem_value_list() != null && createGoodCommand.getItem_value_list().size() > 0) {
                ArrayList<SpecDetailEntity> specDetailEntities = new ArrayList<>();
                for (CreateGoodCommand.SpecBean specBean : createGoodCommand.getItem_value_list()) {
                    SpecDetailEntity specDetailEntity = new SpecDetailEntity();
                    specDetailEntity.setDiscount(specBean.getDiscount());
                    specDetailEntity.setPrice(specBean.getPrice());
                    specDetailEntity.setStock(specBean.getStock());
                    specDetailEntity.setValue_ids(specBean.getValue_ids());
                    specDetailEntity.setValue_names(specBean.getValue_names());
                    specDetailEntities.add(specDetailEntity);
                }

                bundleSpecification.putParcelableArrayList(JumpUtil.VALUE, specDetailEntities);
            }
            JumpUtil.startForResult(getActivity(), SelectCategorySpecificationActivity.class, SPECIFICATION, bundleSpecification);
        } else if (id == R.id.ll_service_description) {//服务描述
            Bundle service = new Bundle();
            if (createGoodCommand != null && createGoodCommand.getMeNatures() != null) {
                service.putStringArrayList(SERVICE_DES, (ArrayList<String>) createGoodCommand.getNature_list());
            }
            JumpUtil.startForResult(getActivity(), ServiceDesciptionActivity.class, SERVICE_DESCRIPTION, service);
        } else if (id == R.id.tv_save_pending_shelf) {//存入待上架
            if (checkContent()) {
                upper_shelf = 7;
                compressPresenter.compressFiles(images);
            }
        } else if (id == R.id.tv_upper_shelf) {//立即上架
            upper_shelf = 2;
            if (checkContent()) {
                compressPresenter.compressFiles(images);
            }
        }
    }

    public void doGetUpOrSaveShelfImmediately(CreateGoodCommand createGoodCommand) {
        RetrofitApiFactory.createApi(StoreApi.class)
                .getCreateGood(createGoodCommand)
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
//                            EventBus.getDefault().post(new ShelfUpdateEvent());
                            finish();
                        } else {
                            ToastUtils.showShort(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    public void doGetUpdateShelf(CreateGoodCommand createGoodCommand) {
        RetrofitApiFactory.createApi(StoreApi.class)
                .getUpdateGood(createGoodCommand)
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
                            ToastUtils.showShort(data.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }

    public boolean checkContent() {
        if (TextUtils.isEmpty(createGoodCommand.getTitle())) {
            ToastUtils.showShort("请输入商品名称");
            return false;
        } else if (photoAdapter.getItemCount() == 0) {
            ToastUtils.showShort("请上传商品图片");
            return false;
        } else if (photoAdapter.getItemCount() < 1) {
            ToastUtils.showShort("商品图片至少上传一张");
            return false;
        } else if (createGoodCommand.getCate_list().size() == 0) {
            ToastUtils.showShort("请选择类目");
            return false;
        }
//        else if (createGoodCommand.getGroup().size() == 0) {
//            ToastUtils.showShort("请选择分组");
//            return false;
//        }
        else if (TextUtils.isEmpty(createGoodCommand.getTemplate_id())) {
            ToastUtils.showShort("请选择模板");
            return false;
        } else if (TextUtils.isEmpty(createGoodCommand.getRich_text())) {
            ToastUtils.showShort("请编辑商品详情");
            return false;
        } else if (createGoodCommand.getItem_value_list().size() == 0) {
            ToastUtils.showShort("请设置商品规格");
            return false;
        } else if (TextUtils.isEmpty(mBinding.etSale.getText())) {
            ToastUtils.showShort("请填写虚拟销量");
            return false;
        } else if (TextUtils.isEmpty(mBinding.etOldPrice.getText())) {
            ToastUtils.showShort("请填写原价");
            return false;
        } else if (TextUtils.isEmpty(mBinding.etNowPrice.getText())) {
            ToastUtils.showShort("请填写现价");
            return false;
        } else {
            createGoodCommand.setDeal_num_false(mBinding.etSale.getText().toString());
            createGoodCommand.setPrice(mBinding.etNowPrice.getText().toString());
            createGoodCommand.setOld_price(mBinding.etOldPrice.getText().toString());
            return true;
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
                if (requestCode == CATEGORY) {
                    //从category选择返回时进行展示的处理
                    List<String> nameList = data.getStringArrayListExtra(NAME_LIST);
                    //所需类目需要上传的数组参数
                    List<String> idList = data.getStringArrayListExtra(ID_LIST);
                    Log.e("ID_LIST", "idList" + idList);
                    StringBuffer stringBuffer = new StringBuffer();
                    for (String id : idList) {
                        int position = idList.indexOf(id);
                        if (idList.size() == 1) {
                            stringBuffer.append(nameList.get(position));
                        } else if (idList.size() == 2) {
                            if (position == 0) {
                                stringBuffer.append(nameList.get(position) + "、");
                            } else if (position == 1) {
                                stringBuffer.append(nameList.get(position));
                            }
                        } else if (nameList.size() > 2) {
                            if (position == 0) {
                                stringBuffer.append(nameList.get(position) + "、");
                            } else if (position == 1) {
                                stringBuffer.append(nameList.get(position) + "、...");
                            }
                        }
                    }
                    mBinding.tvCategroy.setText(stringBuffer.toString());
                    createGoodCommand.getCate_list().clear();
                    createGoodCommand.setCate_list(idList);
                } else if (requestCode == SPECIFICATION) {
                    //从specification 商品规格 选择后返回进行展示的处理
                    List<SpecDetailEntity> specDetailEntities = data.getParcelableArrayListExtra(JumpUtil.VALUE);
                    List<CreateGoodCommand.SpecBean> specBeanList = new ArrayList<>();
                    for (SpecDetailEntity entity : specDetailEntities) {
                        CreateGoodCommand.SpecBean specBean = new CreateGoodCommand.SpecBean();
                        specBean.setDiscount(entity.getDiscount());
                        specBean.setPrice(entity.getPrice());
                        specBean.setStock(entity.getStock());
                        specBean.setValue_ids(entity.getValue_ids());
                        specBean.setValue_names(entity.getValue_names());
                        specBeanList.add(specBean);
                    }
                    createGoodCommand.setItem_value_list(specBeanList);

                } else if (requestCode == GROUP) {
                    //从selectgroup中选择后返回进行展示的处理
                    List<String> nameList = data.getStringArrayListExtra(NAME_LIST);
                    List<String> idList = data.getStringArrayListExtra(ID_LIST);
                    StringBuffer stringBuffer = new StringBuffer();
                    for (String str : nameList) {
                        if (nameList.size() == 1) {
                            stringBuffer.append(str);
                        } else if (nameList.size() == 2) {
                            if (nameList.indexOf(str) == 0) {
                                stringBuffer.append(str + "、");
                            } else {
                                stringBuffer.append(str);
                            }
                        } else {
                            if (nameList.indexOf(str) == 2) {
                                stringBuffer.append("...");
                                break;
                            } else {
                                stringBuffer.append(str + "、");
                            }
                        }
                    }
                    mBinding.tvGroup.setText(stringBuffer.toString());
                    createGoodCommand.setGroup(idList);
                } else if (requestCode == POSTAGE_TEMPLE) {
                    //从postage_temple选择后返回进行展示的处理
                    mBinding.tvPostageTemplate.setText(data.getStringExtra(POST_TEMPLE_NAME));
                    String post_temple_id = data.getStringExtra(POST_TEMPLE_ID);
                    createGoodCommand.setTemplate_id(post_temple_id);
                } else if (requestCode == TAG_ONE) {
                    //从settag中输入标签内容返回后进行展示（标签一）
                    mBinding.tvTagOne.setText(data.getStringExtra(TAG_NAME));
                    lable = data.getStringExtra(TAG_NAME);
//                    tagList.add(0, data.getStringExtra(TAG_NAME));
                }
//                else if (requestCode == TAG_TWO) {
//                    //从settag中输入标签内容返回后进行展示（标签二）
//                    mBinding.tvTagTwo.setText(data.getStringExtra(TAG_NAME));
//                    tagList.add(1, data.getStringExtra(TAG_NAME));
//                }
                else if (requestCode == RICHTEXT_DETAIL) {
                    //获取富文本内容
                    String richtext = data.getStringExtra(RICH_TEXT_STR);
                    createGoodCommand.setRich_text(richtext);
                } else if (requestCode == SERVICE_DESCRIPTION) {
                    //服务描述
                    nature_list = data.getStringArrayListExtra(SERVICE_DES);
                    createGoodCommand.setNature_list(nature_list);
                }
            } else {
                ToastUtils.showShort("操作失败，请重试");
            }
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
                createGoodCommand.setMulti_urls(uploadUrls);
                createGoodCommand.setTitle(mBinding.tvGoodName.getText().toString());
                createGoodCommand.setSort(mBinding.etSort.getText().toString());
                List<String> tagListReal = new ArrayList<>();
                for (String str : tagList) {
                    if (!TextUtils.isEmpty(str)) {
                        tagListReal.add(str);
                    }
                }
//                createGoodCommand.setLabel_list(tagListReal);
                createGoodCommand.setStatus(upper_shelf);
                createGoodCommand.setLabel(lable);
                if (TextUtils.equals(getIntent().getStringExtra(CREATE_GOOD_FROM), SHELF_LIST)) {
                    doGetUpdateShelf(createGoodCommand);
                } else if (TextUtils.equals(getIntent().getStringExtra(CREATE_GOOD_FROM), STORE_RESULT)) {
                    doGetUpOrSaveShelfImmediately(createGoodCommand);
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
                filePresenter.filesUp(CreateCommodityActivity.this.compressPaths);
            }
        });
    }

    @Override
    public void compressFailure(String msg) {
        ToastUtils.showShort(msg);
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
