package com.netmi.workerbusiness.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netmi.baselibrary.data.api.CommonApi;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.OssConfigureEntity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.NotifyEvent;
import com.netmi.workerbusiness.data.cache.HeadUrlCache;
import com.netmi.workerbusiness.data.cache.TelCache;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityMainBinding;
import com.netmi.workerbusiness.databinding.DialogMineTipsBinding;
import com.netmi.workerbusiness.ui.home.MainFragment;
import com.netmi.workerbusiness.ui.login.PersonalInfoActivity;
import com.netmi.workerbusiness.ui.message.MessageFragment;
import com.netmi.workerbusiness.ui.mine.MineFragment;
import com.netmi.workerbusiness.ui.mine.RenewalFeeActivity;
import com.tencent.android.tpush.XGNotifaction;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseMainActivity<ActivityMainBinding> {
    private String tel;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    @Override
    protected void initUI() {
        doGetShopInfo();
        mBinding.setCheckListener(this);
        mBinding.rbHome.setTag(MainFragment.TAG);//运营台
        mBinding.rbCollection.setTag(CodeFragment.TAG);//收款码
        mBinding.rbMessage.setTag(MessageFragment.TAG);//消息
        mBinding.rbMine.setTag(MineFragment.TAG);//我的
        mBinding.executePendingBindings();
        Log.e("wengShop_apply_status", UserInfoCache.get().getShop_apply_status());
        //入驻状态为入驻中和审核失败时 rbMine
        if (UserInfoCache.get().getShop_apply_status().equals("1") || UserInfoCache.get().getShop_apply_status().equals("3")) {//|| !UserInfoCache.get().getShop_pay_status().equals("2")
            mBinding.rbMine.setChecked(true);
        } else {
            mBinding.rbHome.setChecked(true);
        }
//        mBinding.rbMine.setChecked(true);
        registerObservers(true);
    }

    @Override
    protected void initData() {
        doInitOssConfigure();
    }


    @Override
    protected void onNotify(XGNotifaction xgNotifaction) {
        EventBus.getDefault().post(new NotifyEvent());
    }


    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(authObserver, register);
    }

    private Observer<StatusCode> authObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                switch (code) {
                    case PWD_ERROR:
                    case FORBIDDEN:
                        LoginInfoCache.setLogoutState(code.getValue());
                        break;
                    default:
                        LoginInfoCache.setLogoutState(StatusCode.KICKOUT.getValue());
                        break;
                }
                MApplication.getInstance().gotoLogin();
            }
        }
    };

    /**
     * 切换fragment时，修改statusBar
     * 返回true拦截，不执行切换fragment动作
     */
    @Override
    protected boolean setStatusBarOnFragmentChange(String tag, Fragment fragment) {
        if (TextUtils.equals(tag, MainFragment.TAG)) {
            if (!UserInfoCache.get().getShop_apply_status().equals("2")) {
                if (UserInfoCache.get().getShop_apply_status().equals("1")) {
                    showTipsDialog("您的信息正在认证中，请通过认证后再次操作，如有问题请联系管理员" + TelCache.get(),
                            "确定");
                }
                return true;
//            } else if (!UserInfoCache.get().getShop_pay_status().equals("2")) {
//                //用户缴费审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
//                if (UserInfoCache.get().getShop_pay_status().equals("0")) {
//                    showTipsDialog("您的商家认证已经通过，请缴纳服务费", "确定");
//                } else if (UserInfoCache.get().getShop_pay_status().equals("1")) {
//                    showTipsDialog("您已经缴纳服务费，请等待审核通过", "确定");
//                } else if (UserInfoCache.get().getShop_pay_status().equals("3")) {
//                    showTipsDialog("您的缴纳服务费审核失败，请重新提交", "确定");
//                }
//                return true;
            }
        } else if (TextUtils.equals(tag, CodeFragment.TAG)) {
            if (!UserInfoCache.get().getShop_apply_status().equals("2")) {
                if (UserInfoCache.get().getShop_apply_status().equals("1")) {
                    showTipsDialog("您的信息正在认证中，请通过认证后再次操作，如有问题请联系管理员" + TelCache.get(),
                            "确定");
                }
                return true;
//            } else if (!UserInfoCache.get().getShop_pay_status().equals("2")) {
//                //用户缴费审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
//                if (UserInfoCache.get().getShop_pay_status().equals("0")) {
//                    showTipsDialog("您的商家认证已经通过，请缴纳服务费", "确定");
//                } else if (UserInfoCache.get().getShop_pay_status().equals("1")) {
//                    showTipsDialog("您已经缴纳服务费，请等待审核通过", "确定");
//                } else if (UserInfoCache.get().getShop_pay_status().equals("3")) {
//                    showTipsDialog("您的缴纳服务费审核失败，请重新提交", "确定");
//                }
//                return true;
            }
        } else if (TextUtils.equals(tag, MessageFragment.TAG)) {
            if (!UserInfoCache.get().getShop_apply_status().equals("2")) {
                if (UserInfoCache.get().getShop_apply_status().equals("1")) {
                    showTipsDialog("您的信息正在认证中，请通过认证后再次操作，如有问题请联系管理员" + TelCache.get(),
                            "确定");
                }
                return true;
//            } else if (!UserInfoCache.get().getShop_pay_status().equals("2")) {
//                //用户缴费审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
//                if (UserInfoCache.get().getShop_pay_status().equals("0")) {
//                    showTipsDialog("您的商家认证已经通过，请缴纳服务费", "确定");
//                } else if (UserInfoCache.get().getShop_pay_status().equals("1")) {
//                    showTipsDialog("您已经缴纳服务费，请等待审核通过", "确定");
//                } else if (UserInfoCache.get().getShop_pay_status().equals("3")) {
//                    showTipsDialog("您的缴纳服务费审核失败，请重新提交", "确定");
//                }
//                return true;
            }
        } else if (TextUtils.equals(tag, MineFragment.TAG)) {
            fullScreen();
        }
        return false;
    }


    private void doInitOssConfigure() {
        RetrofitApiFactory.createApi(CommonApi.class)
                .getOssConfigure(0)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<OssConfigureEntity>>() {
                    @Override
                    public void onSuccess(BaseData<OssConfigureEntity> data) {
                        OssUtils.initConfigure(data.getData());
                    }
                });
    }

    private void doGetShopInfo() {
        RetrofitApiFactory.createApi(com.netmi.workerbusiness.data.api.MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        tel = data.getData().getShop_admin_tel();
                        TelCache.put(tel);
                        HeadUrlCache.put(data.getData().getLogo_url());
                        UserInfoCache.get().setShop_apply_status(String.valueOf(data.getData().getShop_apply_status()));
                        UserInfoCache.get().setShop_pay_status(String.valueOf(data.getData().getShop_pay_status()));
                        if (data.getData().getShop_user_type() == 2 || data.getData().getShop_user_type() == 3) {//用户选择商户类型 0:未选择类型 1:线上 2:线下 3:线上+线下
                            mBinding.rbCollection.setVisibility(View.VISIBLE);
                        }
//                        if (UserInfoCache.get().getShop_user_type().equals("0")) {
//                            showError("请先选择商户类型");
//                            JumpUtil.overlay(getContext(), BusinessTypeActivity.class);
//                        } else
//                            if (UserInfoCache.get().getShop_apply_status().equals("0")) {
//                            showError("请提交商家入驻资料");
//                            JumpUtil.overlay(getContext(), PersonalInfoActivity.class);
//                        }
                    }
                });
    }

    //提示确认弹窗
    private void showTipsDialog(String content, String confirmText) {
        final DialogMineTipsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_mine_tips, null, false);
        if (!TextUtils.isEmpty(confirmText)) {
            binding.tvConfirm.setText(confirmText);
        }
        if (!TextUtils.isEmpty(content)) {
            binding.tvContent.setText(content);
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.showDialog)
                .setView(binding.getRoot())
                .show();

        //此处设置位置窗体大小
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setLayout(Densitys.dp2px(getContext(), 250), LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        binding.tvConfirm.setOnClickListener((View v) -> {
            alertDialog.dismiss();
            Bundle args = new Bundle();
            if (TextUtils.equals(content, "您的商家认证已经通过，请缴纳服务费") || TextUtils.equals(content, "您的缴纳服务费审核失败，请重新提交")) {
                args.putInt(JumpUtil.TYPE, 1);
                JumpUtil.overlay(getContext(), RenewalFeeActivity.class, args);
            }
            if (TextUtils.equals(confirmText, "去认证")) {
                JumpUtil.overlay(getContext(), PersonalInfoActivity.class);
            }
        });

    }

}
