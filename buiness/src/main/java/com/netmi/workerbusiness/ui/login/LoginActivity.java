package com.netmi.workerbusiness.ui.login;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.liemi.basemall.data.api.LoginApi;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.LoginInfoEntity;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.router.BaseRouter;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.AppManager;
import com.netmi.baselibrary.utils.AppUtils;
import com.netmi.baselibrary.utils.Densitys;
import com.netmi.baselibrary.utils.InputListenView;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.baselibrary.utils.Strings;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.mine.ShopInfoEntity;
import com.netmi.workerbusiness.databinding.ActivityLoginBinding;
import com.netmi.workerbusiness.databinding.DialogMineTipsBinding;
import com.netmi.workerbusiness.ui.MainActivity;
import com.netmi.workerbusiness.utils.HTMLFormat;
import com.netmi.workerbusiness.utils.PushUtils;
import com.tencent.bugly.beta.Beta;
import com.trello.rxlifecycle2.android.ActivityEvent;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;


@Route(path = BaseRouter.ModLogin_ThirdLoginActivity)
public class LoginActivity extends BaseIMLoginActivity<ActivityLoginBinding> {
    public static final String LOGIN_DISPLAY = "loginDisplay";
    public static final String LOGIN_USER_AGREE = "loginUserAgree";
    private UserAgreeDialogFragment userAgreeDialogFragment;


    //登录方式  0为验证码登录  1位密码登录
    private int login_way = 1;
    //密码隐藏  显示图片
    Drawable gone, visi, pass;
    private AlertDialog alertDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initUI() {

        //检查版本更新
        Beta.checkUpgrade(false, false);
        if (!TextUtils.equals((String) SPs.get(getContext(), LOGIN_DISPLAY, ""), AppUtils.getAppVersionName()) ||
                !SPs.getBoolean(getContext(), LOGIN_USER_AGREE, false)) {
            SPs.put(getContext(), LOGIN_DISPLAY, AppUtils.getAppVersionName());
            Log.e("weng", AppUtils.getAppVersionName());
            if (userAgreeDialogFragment == null) {
                userAgreeDialogFragment = new UserAgreeDialogFragment();
                userAgreeDialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
            } else {
                userAgreeDialogFragment.onStart();
            }
        }

        mBinding.etAccount.setText(LoginInfoCache.get().getLogin());
        new InputListenView(mBinding.tvConfirm, mBinding.etAccount) {
            @Override
            public boolean customJudge() {
//                return mBinding.etCode.getText().toString().length() > 3;
                return true;
            }
        };

        mBinding.etPassword.setOnDrawableRightListener(this::onDrawableRightClick);
        mBinding.rlParent.setFocusable(true);
        mBinding.rlParent.setFocusableInTouchMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(LoginInfoCache.get().getLogin())) {
            mBinding.etAccount.setText(LoginInfoCache.get().getLogin());
            mBinding.etAccount.setSelection(mBinding.etAccount.getText().length());
        }
    }

    @Override
    protected void initData() {
        mBinding.setIsCheck(true);
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        int id = view.getId();
        if (id == R.id.tv_confirm) {
            if (login_way == 0) {  //验证码登录
                if (TextUtils.isEmpty(mBinding.etAccount.getText())) {
                    showError("请输入手机号");
                } else if (TextUtils.isEmpty(mBinding.etCode.getText())) {
                    showError("请输入验证码");
                } else if (!mBinding.getIsCheck()) {
                    showError("请勾选协议");
                } else {
                    doLogin(mBinding.etAccount.getText().toString(), mBinding.etCode.getText().toString(), "", "login_code");
                }
            } else if (login_way == 1) { //密码登录
                if (TextUtils.isEmpty(mBinding.etAccount.getText())) {
                    showError("请输入手机号");
                } else if (TextUtils.isEmpty(mBinding.etPassword.getText())) {
                    showError("请输入密码");
                } else if (!mBinding.getIsCheck()) {
                    showError("请勾选协议");
                } else {
                    doLogin(mBinding.etAccount.getText().toString(), "", MD5.GetMD5Code(mBinding.etPassword.getText().toString()), "login_phone");
                }
            }
        } else if (id == R.id.tvGetCode) {
            if (TextUtils.isEmpty(mBinding.etAccount.getText())) {
                showError("请输入手机号");
            } else if (!Strings.isPhone(mBinding.etAccount.getText())) {
                showError("请输入正确的手机号");
            } else {
                doSendSMS(mBinding.etAccount.getText().toString(), "login");
            }
        } else if (id == R.id.change_login_way) {  //去注册
            //验证码页面
            JumpUtil.overlay(getContext(), VerificationActivity.class);
//            JumpUtil.overlay(getContext(), RegisterActivity.class);

        } else if (id == R.id.tv_forget_password) {
            JumpUtil.overlay(getContext(), ForgetPasswordActivity.class);
        } else if (id == R.id.tv_phone_register) {
            JumpUtil.overlay(getContext(), RegisterActivity.class);
        } else if (id == R.id.tv_service) {//点击服务条款
            doAgreement(111);
        } else if (id == R.id.tv_secret) {//点击隐私协议
            doAgreement(127);
        } else if (id == R.id.cb_service) {
            if (mBinding.getIsCheck()) {
                mBinding.setIsCheck(false);
            } else {
                mBinding.setIsCheck(true);
            }
        }
    }

    private void doSendSMS(String phone, String type) {

        showProgress("");
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getSmsCode(phone, type)
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new XObserver<BaseData>() {
                    public void onSuccess(BaseData baseData) {
                        sendSMSOk();
                        showError(baseData.getErrmsg());
                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });

    }

    private boolean isSend = true;

    public void sendSMSOk() {
        isSend = false;
        new CountDownTimer(1000 * 60, 1000) {  // 倒计时 60 秒后可以重新获取验证码
            @Override
            public void onTick(long millisUntilFinished) {
                if (isSend) {
                    cancel();
                } else if (mBinding.tvGetCode != null) {
                    String s = (millisUntilFinished / 1000) + getString(R.string.pickerview_seconds);
                    mBinding.tvGetCode.setText(s);
                }
            }

            @Override
            public void onFinish() {
                if (isSend || mBinding.tvGetCode == null)
                    return;
                isSend = true;
                mBinding.tvGetCode.setEnabled(isSend);
                mBinding.tvGetCode.setText(R.string.obtain_check_code);
            }
        }.start();
        mBinding.tvGetCode.setEnabled(isSend);
    }


    //执行登录操作
    protected void doLogin(final String phone, String code, String password, final String scenario) {
        showProgress("");
        RetrofitApiFactory.createApi(LoginApi.class)
                .doLoginPhoneCode(phone, code, password, scenario, null)
                .compose(this.<BaseData<UserInfoEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<UserInfoEntity>>compose())
                .subscribe(new XObserver<BaseData<UserInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<UserInfoEntity> data) {
                        showError("登录成功");
                        if (scenario.equals(Constant.LOGIN_CODE) || scenario.equals(Constant.LOGIN_PHONE)) {
                            LoginInfoEntity loginInfoEntity = new LoginInfoEntity(phone);
                            loginInfoEntity.setOpen(true);
                            LoginInfoCache.put(loginInfoEntity);
                        }
                        //保存Token
                        AccessTokenCache.put(data.getData().getToken());
                        //保存用户信息
                        UserInfoCache.put(data.getData());

                        //绑定个推
                        PushUtils.bindPush();
                        String shop_user_type = data.getData().getShop_user_type();
                        String shop_apply_status = data.getData().getShop_apply_status();
                        if (shop_user_type.equals("0") || shop_apply_status.equals("0")) {
                            //选择商户类型
                            JumpUtil.overlay(getContext(), BusinessTypeActivity.class);
                        } else if (shop_user_type.equals("1")) {
                            //跳首页
                            toMain();
                        } else if (shop_user_type.equals("2")) {
                            getShop();
                        }

                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }

                    @Override
                    public void onFail(BaseData<UserInfoEntity> data) {
                        showMessageHintDialog(data.getErrmsg());
                    }
                });
    }

    //登录成功结束页面
    protected void loginSuccessFinish() {
//        将之前的页面结束掉
        if (UserInfoCache.get().getShop_user_type().equals("0")) {
            showError("请先选择商户类型");
            JumpUtil.overlay(getContext(), BusinessTypeActivity.class);
        } else if (UserInfoCache.get().getShop_apply_status().equals("0")) {
            showError("请提交商家入驻资料");
            JumpUtil.overlay(getContext(), BusinessTypeActivity.class);
        } else {
//            loginYunXin(UserInfoCache.get().getToken());
            JumpUtil.overlay(getContext(), MainActivity.class);
            AppManager.getInstance().finishAllActivity(MainActivity.class);
        }
    }

    //服务协议
    private void doAgreement(int type) {
        showProgress("");
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getAgreement(type)
                .compose(this.<BaseData<AgreementEntity>>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxSchedulers.<BaseData<AgreementEntity>>compose())
                .subscribe(new XObserver<BaseData<AgreementEntity>>() {
                    @Override
                    public void onSuccess(BaseData<AgreementEntity> agreementEntityBaseData) {
                        if (agreementEntityBaseData.getErrcode() == Constant.SUCCESS_CODE) {
                            Bundle bundle = new Bundle();
                            if (type == 111) {
                                bundle.putString(WEBVIEW_TITLE, "服务条款");
                            } else {
                                bundle.putString(WEBVIEW_TITLE, "隐私协议");
                            }
                            bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                            bundle.putString(WEBVIEW_CONTENT, HTMLFormat.getNewData(agreementEntityBaseData.getData().getContent()));
                            JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                        } else {
                            showError(agreementEntityBaseData.getErrmsg());
                        }
                    }

                    @Override
                    public void onComplete() {
                        hideProgress();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        hideProgress();
                        showError(ex.getMessage());
                    }
                });
    }


    //密码显示隐藏
    public void onDrawableRightClick() {
        int inputType = mBinding.etPassword.getInputType();
        if (gone == null) {
            gone = getdra(R.mipmap.password_gone_three);
        }
        if (visi == null) {
            visi = getdra(R.mipmap.password_visible_three);
        }
//        if (pass==null){
//            pass = getdra(R.mipmap.user_password);
//        }
        if (inputType == 0x81) {
            mBinding.etPassword.setCompoundDrawables(pass, null, visi, null);
            mBinding.etPassword.setInputType(0x90);
//            mBinding.etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);

        } else {
            mBinding.etPassword.setCompoundDrawables(pass, null, gone, null);
            mBinding.etPassword.setInputType(0x81);
//            mBinding.etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        }
    }

    public Drawable getdra(@DrawableRes int id) {
        Drawable rightVi = getResources().getDrawable(id);
        rightVi.setBounds(0, 0, rightVi.getMinimumWidth(), rightVi.getMinimumHeight());
        return rightVi;
    }

    public void getShop() {
        RetrofitApiFactory.createApi(MineApi.class)
                .shopInfo("")
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ShopInfoEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ShopInfoEntity> data) {
                        ShopInfoEntity data1 = data.getData();
                        if (data1.getId() == null) {//新用户
                            if (data1.getIs_bind_bank().equals("0")) {//绑定银行卡
                                //跳转银行卡绑定
                                JumpUtil.overlay(getContext(), BindingThreeActivity.class, "name", data1.getReal_name(), "idcard", data1.getIdcard(), "phone", data1.getPhone());
                            } else {
                                //跳首页
                                toMain();
                            }
                        } else {
                            //跳首页
                            toMain();
                        }

                    }

                    @Override
                    public void onFail(BaseData data) {
                        super.onFail(data);
                        if (data.getErrcode() == 20001) {
                            JumpUtil.overlay(getContext(), BusinessTypeActivity.class);
                        }
                    }
                });
    }

    //提示确认弹窗
    private void showTipsDialog(String content, String confirmText, String contentTitle) {
        final DialogMineTipsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_mine_tips, null, false);
        if (!TextUtils.isEmpty(confirmText)) {
            binding.tvConfirm.setText(confirmText);
        }
        if (!TextUtils.isEmpty(contentTitle)) {
            binding.tvHintDialogTips.setText(contentTitle);
        }
        if (!TextUtils.isEmpty(content)) {
            binding.tvContent.setText(content);
        }
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(getContext(), R.style.showDialog)
                    .setView(binding.getRoot())
                    .show();
            //此处设置位置窗体大小
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setLayout(Densitys.dp2px(getContext(), 350), LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        } else {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
        binding.tvConfirm.setOnClickListener((View v) -> {
            alertDialog.dismiss();
            Bundle args = new Bundle();
            if (content.equals(getString(R.string.haibei_dialog_contrac))) {//合同
                JumpUtil.overlay(getContext(), ContractOfflineListActivity.class);
            }
        });
    }

    public void toMain() {
        JumpUtil.overlay(getContext(), MainActivity.class);
        AppManager.getInstance().finishAllActivity(MainActivity.class);
    }

}
