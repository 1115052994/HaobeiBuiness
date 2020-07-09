package com.liemi.basemall.ui.store.good;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.ETHApi;
import com.liemi.basemall.data.entity.StoreEntity;
import com.liemi.basemall.data.entity.comment.CommentEntity;
import com.liemi.basemall.data.entity.eth.ETHRateEntity;
import com.liemi.basemall.data.entity.good.GoodDetailUrlEntity;
import com.liemi.basemall.data.entity.good.GoodsDetailedEntity;
import com.liemi.basemall.databinding.ActivityGoodDetailBinding;
import com.liemi.basemall.databinding.ItemGoodDetailBannerBinding;
import com.liemi.basemall.databinding.ItemGoodDetailCommentBinding;
import com.liemi.basemall.databinding.ItemGoodDetailInfoBinding;
import com.liemi.basemall.databinding.ItemGoodDetailWebviewBinding;
import com.liemi.basemall.ui.store.StoreDetailActivity;
import com.liemi.basemall.ui.store.comment.CommentAllActivity;
import com.liemi.basemall.widget.GoodDetailParamDialog;
import com.liemi.basemall.widget.GoodsBannerHolderView;
import com.liemi.basemall.widget.ImageFullWebViewClient;
import com.liemi.basemall.widget.OpenImageInterfaceJS;
import com.lzy.imagepicker.ImagePicker;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BannerEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseRViewAdapter;
import com.netmi.baselibrary.ui.BaseViewHolder;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.FastBundle;
import com.netmi.baselibrary.utils.FloatUtils;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.widget.MyXRecyclerView;
import com.netmi.baselibrary.widget.banner.MyConvenientBanner;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import static com.liemi.basemall.ui.store.StoreDetailActivity.STORE_ID;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static com.netmi.baselibrary.data.Constant.BASE_API;

public class GoodDetailActivity extends BaseGoodsDetailedActivity<ActivityGoodDetailBinding> {

    private BaseRViewAdapter<BaseEntity, BaseViewHolder> adapter;

    private List<BaseEntity> baseEntities;

    private MyConvenientBanner cbGood;

    private ETHRateEntity ethRateEntity;

    private GoodsBuyDialogFragment buyDialogFragment;

    @Override
    protected int getContentView() {
        return R.layout.activity_good_detail;
    }

    @Override
    protected LinearLayout getLlTop() {
        return mBinding.llTop;
    }

    @Override
    protected LinearLayout getLlTopWhite() {
        return mBinding.llTopWhite;
    }

    @Override
    protected MyXRecyclerView getXrvData() {
        return mBinding.xrvGood;
    }

    @Override
    protected CheckBox getCbCollect() {
        return mBinding.cbCollect;
    }

    @Override
    protected void initUI() {
        super.initUI();
        xRecyclerView.setAdapter(adapter = new BaseRViewAdapter<BaseEntity, BaseViewHolder>(this) {
            @Override
            public int layoutResId(int viewType) {
                if (viewType == 1) {
                    return R.layout.item_good_detail_banner;
                } else if (viewType == 2) {
                    return R.layout.item_good_detail_info;
                } else if (viewType == 3) {
                    return R.layout.item_good_detail_comment;
                } else if (viewType == 4) {
                    return R.layout.item_good_detail_store;
                } else if (viewType == 5) {
                    return R.layout.item_good_detail_webview;
                }
                return R.layout.item_good_detail_web;
            }

            @Override
            public int getItemViewType(int position) {
                if (adapter.getItem(position) instanceof PageEntity) {
                    return 1;
                } else if (adapter.getItem(position) instanceof GoodsDetailedEntity) {
                    return 2;
                } else if (adapter.getItem(position) instanceof CommentEntity) {
                    return 3;
                } else if (adapter.getItem(position) instanceof StoreEntity) {
                    return 4;
                } else if (adapter.getItem(position) instanceof GoodDetailUrlEntity) {
                    return 5;
                }
                return super.getItemViewType(position);
            }

            @Override
            public BaseViewHolder holderInstance(final ViewDataBinding binding) {
                return new BaseViewHolder(binding) {
                    @Override
                    public void bindData(Object item) {
                        super.bindData(item);
                        if (getBinding() instanceof ItemGoodDetailBannerBinding) {
                            final ItemGoodDetailBannerBinding bannerBinding = (ItemGoodDetailBannerBinding) getBinding();

                            PageEntity<BannerEntity> pageEntity = (PageEntity<BannerEntity>) adapter.getItem(position);
                            final List<String> showImgs = new ArrayList<>();
                            for (BannerEntity entity : pageEntity.getList()) {
                                showImgs.add(entity.getImg_url());
                            }
                            cbGood = bannerBinding.cbHome;
                            cbGood.setPages(new CBViewHolderCreator() {
                                @Override
                                public Holder createHolder(View itemView) {
                                    return new GoodsBannerHolderView<String>(itemView);
                                }

                                @Override
                                public int getLayoutId() {
                                    return R.layout.item_banner;
                                }
                            }, showImgs)
                                    .setOnItemClickListener(new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            JumpUtil.overlayImagePreview(getActivity(), showImgs, position);
                                        }
                                    })
                                    .setOnPageChangeListener(new OnPageChangeListener() {
                                        @Override
                                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                        }

                                        @Override
                                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        }

                                        @Override
                                        public void onPageSelected(int index) {
                                            bannerBinding.tvIndicator.setText(((index + 1) + "/" + showImgs.size()));
                                        }
                                    }).startTurning(5000);

                            bannerBinding.tvIndicator.setText(("1/" + showImgs.size()));

                        } else if (getBinding() instanceof ItemGoodDetailInfoBinding) {
                            final ItemGoodDetailInfoBinding binding = (ItemGoodDetailInfoBinding) getBinding();
                            if (Strings.toFloat(goodEntity.getPostage()) > 0) {
                                binding.tvPostage.setText("运费:" + goodEntity.getPostage());
                            } else {
                                binding.tvPostage.setText("免运费");
                            }

                            if (ethRateEntity == null || Strings.isEmpty(ethRateEntity.getEth_cny())) {
                                binding.llDigital.setVisibility(View.GONE);
                            } else {
                                /**
                                 * 2018/12/18  修改ETH金额不再从自读能获取，而是自己计算
                                 */
                                binding.llDigital.setVisibility(View.GONE);
                                //获取汇率
                                float ethCny = FloatUtils.string2Float(ethRateEntity.getEth_cny());
                                GoodsDetailedEntity detailedEntity = (GoodsDetailedEntity) getItem(position);
                                //计算商品价格
                                String price = String.valueOf(FloatUtils.string2Float(detailedEntity.getPrice()) /ethCny);
                                binding.tvNeedEth.setText(FloatUtils.eightDecimal(price, false));
                                binding.tvEthRate.setText("≈" + FloatUtils.twoDecimal(ethRateEntity.getEth_cny(),false) + " CNY");
                            }

                            if (goodEntity.getMeLabels().size() > 0) {
                                binding.tflLabel.setAdapter(new SpecsTagAdapter<GoodsDetailedEntity.MeLabelsBean>(goodEntity.getMeLabels()) {
                                    @Override
                                    public View getView(FlowLayout parent, int position, GoodsDetailedEntity.MeLabelsBean s) {
                                        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_goods_label, binding.tflLabel, false);
                                        tv.setText(s.getName());
                                        return tv;
                                    }

                                });
                            }

                        } else if (getBinding() instanceof ItemGoodDetailWebviewBinding) {
                            WebView webView = ((ItemGoodDetailWebviewBinding) getBinding()).wvGood;
                            webView.setWebViewClient(new WebViewClient());
                            webView.setWebChromeClient(new WebChromeClient());
                            ImageFullWebViewClient.setWebSettings(webView.getSettings());
                            webView.setHorizontalScrollBarEnabled(false);
                            webView.setVerticalScrollBarEnabled(false);
                            webView.addJavascriptInterface(new OpenImageInterfaceJS(getActivity()), "App");
                            Log.e("detail_url",BASE_API + ((GoodDetailUrlEntity) item).getDetailUri());
                            webView.loadUrl(BASE_API + ((GoodDetailUrlEntity) item).getDetailUri());
                        } else if (getBinding() instanceof ItemGoodDetailCommentBinding) {
                            ItemGoodDetailCommentBinding binding = (ItemGoodDetailCommentBinding) getBinding();
                            RecyclerView rvPic = binding.rvImg;
                            CommentEntity commentBean = (CommentEntity) getItem(position);
                            binding.rbStarServer.setStar(commentBean.getLevel(), false);
                            if (!Strings.isEmpty(commentBean.getMeCommetImgs())) {
                                rvPic.setLayoutManager(new GridLayoutManager(context, 3));
                                final BaseRViewAdapter<String, BaseViewHolder> imgAdapter = new BaseRViewAdapter<String, BaseViewHolder>(context) {

                                    @Override
                                    public int layoutResId(int position) {
                                        return R.layout.item_multi_pic_show;
                                    }

                                    @Override
                                    public BaseViewHolder holderInstance(ViewDataBinding binding) {
                                        return new BaseViewHolder(binding) {
                                            @Override
                                            public void doClick(View view) {
                                                super.doClick(view);
                                                JumpUtil.overlayImagePreview(getActivity(), getItems(), position);
                                            }
                                        };
                                    }
                                };
                                rvPic.setAdapter(imgAdapter);
                                if (!Strings.isEmpty(commentBean.getMeCommetImgs())) {
                                    imgAdapter.setData(commentBean.getMeCommetImgs());
                                }
                            }
                        }
                    }

                    @Override
                    public void doClick(View view) {
                        super.doClick(view);
                        int i = view.getId();
                        if (i == R.id.ll_comment_all) {
                            if (MApplication.getInstance().checkUserIsLogin()) {
                                JumpUtil.overlay(getContext(), CommentAllActivity.class, new FastBundle().putString(ITEM_ID, goodEntity.getItem_id()));
                            }

                        } else if (i == R.id.tv_good_param) {
                            if (goodEntity != null
                                    && goodEntity.getMeNatures() != null
                                    && !goodEntity.getMeNatures().isEmpty()) {
                                new GoodDetailParamDialog(getContext(), goodEntity).showDialog();
                            } else {
                                ToastUtils.showShort("暂无服务描述");
                            }

                        } else if (i == R.id.tv_rule_choice) {
                            if (MApplication.getInstance().checkUserIsLogin()) {
                                if(buyDialogFragment==null){
                                    buyDialogFragment=new GoodsBuyDialogFragment().setGoodsEntity(goodEntity).setBuyType(false);
                                    buyDialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
                                }else{
                                    buyDialogFragment.setBuyType(false);
                                    buyDialogFragment.onStart();
                                }
                            }
                        } else if (i == R.id.tv_enter) {
                            if (goodEntity != null
                                    && goodEntity.getShop() != null) {
                                JumpUtil.startSceneTransition(getActivity(), StoreDetailActivity.class,
                                        new FastBundle().putString(STORE_ID, goodEntity.getShop().getId()),
                                        new Pair<>(binding.getRoot().findViewById(R.id.iv_store_pic), getString(R.string.transition_store)),
                                        new Pair<>(binding.getRoot().findViewById(R.id.tv_store_name), getString(R.string.transition_store_name)));
                            } else {
                                ToastUtils.showShort("请等待数据加载");
                            }

                        } else {
                        }
                    }
                };
            }
        });


        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
    }

    @Override
    protected void initData() {
        super.initData();
        doGetETHRate();
        baseEntities = new ArrayList<>();
        xRecyclerView.refresh();
    }

    @Override
    public void showData(GoodsDetailedEntity goodEntity) {
        this.goodEntity = goodEntity;
        mBinding.cbCollect.setChecked(goodEntity.getIs_collection() == 1);
        mBinding.setCheckedListener(this);
        if (goodEntity.getIs_collection()==1){
            getCbCollect().setText("已收藏");
            getCbCollect().setTextColor(0xFFB52902);
        }else {
            getCbCollect().setText("收藏");
            getCbCollect().setTextColor(0xFF878787);
        }
        mBinding.setItem(goodEntity);

        PageEntity<BannerEntity> pageEntity = new PageEntity<>();
        for (String imgsBean : goodEntity.getItemImgs()) {
            pageEntity.getList().add(new BannerEntity(imgsBean));
        }
        baseEntities.clear();
        baseEntities.add(pageEntity);
        baseEntities.add(goodEntity);
        if (goodEntity.getMeCommet() != null) {
            goodEntity.getMeCommet().setTotal_level(goodEntity.getTotal_level() + "%");
            baseEntities.add(goodEntity.getMeCommet());
        } else {
            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setSum_commet(0);
            commentEntity.setTotal_level("暂无");
            baseEntities.add(commentEntity);
        }
        if (!TextUtils.isEmpty(goodEntity.getShop_id())
                && goodEntity.getShop() != null) {
            baseEntities.add(goodEntity.getShop());
        }
        baseEntities.add(new BaseEntity());
        baseEntities.add(new GoodDetailUrlEntity(goodEntity.getRich_text(), goodEntity.getParam()));
        adapter.setData(baseEntities);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && cbGood != null
                    && requestCode == REQUEST_CODE_PREVIEW) {
                cbGood.setCurrentItem(data.getIntExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0), false);
            }
        }
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int i = view.getId();
        if (i == R.id.tv_buy) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                if(buyDialogFragment==null){
                    buyDialogFragment=new GoodsBuyDialogFragment().setGoodsEntity(goodEntity).setBuyType(false);
                    buyDialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
                }else{
                    buyDialogFragment.setBuyType(false);
                    buyDialogFragment.onStart();
                }
            }

        } else if (i == R.id.tv_add_shop_cart) {
            if (MApplication.getInstance().checkUserIsLogin()) {
                if(buyDialogFragment==null){
                    buyDialogFragment=new GoodsBuyDialogFragment().setGoodsEntity(goodEntity).setBuyType(true);
                    buyDialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
                }else{
                    buyDialogFragment.setBuyType(true);
                    buyDialogFragment.onStart();
                }
            }
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        View decorView=buyDialogFragment.getDialog().getWindow()==null?null:buyDialogFragment.getDialog().getWindow().getDecorView();
        if (buyDialogFragment!=null && decorView!=null && decorView.getVisibility()==View.VISIBLE){
            return buyDialogFragment.onKeyDown(keyCode,event);
        }

        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    protected void onDestroy() {
        if (buyDialogFragment!=null) {
            buyDialogFragment = null;
        }
        super.onDestroy();
    }

    private void doGetETHRate() {
        showProgress("");
        RetrofitApiFactory.createApi(ETHApi.class)
                .getETHRate(null)
                .compose(RxSchedulers.<BaseData<ETHRateEntity>>compose())
                .compose((this).<BaseData<ETHRateEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ETHRateEntity>>() {
                    @Override
                    protected void onError(ApiException ex) {
                        ethRateEntity = null;
                        adapter.notifyDataSetChanged();
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onSuccess(BaseData<ETHRateEntity> data) {
                        ethRateEntity = data.getData();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFail(BaseData<ETHRateEntity> data) {
                        showError(data.getErrmsg());
                        ethRateEntity = null;
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }
                });
    }
}
