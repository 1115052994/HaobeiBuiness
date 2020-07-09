package com.netmi.workerbusiness.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.AgreementEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.ui.BaseDialogFragment;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.SPs;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.databinding.DialogFragmentUserAgreeBinding;
import com.trello.rxlifecycle2.android.FragmentEvent;

import static com.netmi.workerbusiness.ui.login.LoginActivity.LOGIN_USER_AGREE;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/2/26
 * 修改备注：
 */
public class UserAgreeDialogFragment extends BaseDialogFragment<DialogFragmentUserAgreeBinding> implements DialogInterface.OnKeyListener {
    @Override
    protected int getContentView() {
        return R.layout.dialog_fragment_user_agree;
    }

    @Override
    public int getTheme() {
        return R.style.sharemall_CustomDialog;
    }

    @Override
    protected void initUI() {
        mBinding.setDoClick(this);
        String text = getResources().getString(R.string.privacy_tips_content);
        //底部提示语
        SpannableStringBuilder sbb = new SpannableStringBuilder();
        sbb.append(text);
        //设置属性
        sbb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                doInitAgreement(111);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //设置文本的颜色
                ds.setColor(Color.parseColor("#1A7DC2"));
                //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线,其实默认也是true，如果要下划线的话可以不设置
                ds.setUnderlineText(false);
            }
        }, 48, 54, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sbb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                doInitAgreement(127);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                //设置文本的颜色
                ds.setColor(Color.parseColor("#1A7DC2"));
                //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线,其实默认也是true，如果要下划线的话可以不设置
                ds.setUnderlineText(false);
            }
        }, 55, 61, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mBinding.tvContent.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        mBinding.tvContent.setText(sbb, TextView.BufferType.SPANNABLE);
        mBinding.tvContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(this);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            //WindowManager.LayoutParams.MATCH_PARENT
            lp.width = (int) (dm.widthPixels * 0.8); // 宽度持平
            window.setAttributes(lp);
        }
    }

    @Override
    protected void initData() {
        // doInitAgreement(42);//42
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.tv_exit) {
            //关闭APP
            SPs.put(getContext(), LOGIN_USER_AGREE, false);
            getActivity().onBackPressed();
        }
        if (view.getId() == R.id.tv_agree) {
            SPs.put(getContext(), LOGIN_USER_AGREE, true);
            getDialog().dismiss();
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    //隐私提示保护
    private void doInitAgreement(int type) {
        RetrofitApiFactory.createApi(com.netmi.baselibrary.data.api.LoginApi.class)
                .getAgreement(type)
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY))
                .compose(RxSchedulers.compose())
                .subscribe(new XObserver<BaseData<AgreementEntity>>(this) {
                    @Override
                    public void onSuccess(BaseData<AgreementEntity> data) {
                        if (dataExist(data)) {
                           /* BaseWebviewActivity.start(getContext(),
                                    data.getData().getTitle(),
                                    data.getData().getUrl(), null);*/
                            Intent intent = new Intent(getContext(), BaseWebviewActivity.class);
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_TITLE, data.getData().getTitle());
                            if (data.getData().getLink_type().equals("2")) {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_URL);
                            } else {
                                intent.putExtra(BaseWebviewActivity.WEBVIEW_TYPE, BaseWebviewActivity.WEBVIEW_TYPE_CONTENT);
                            }
                            intent.putExtra(BaseWebviewActivity.WEBVIEW_CONTENT, data.getData().getContent());
                            startActivity(intent);
                        }
                    }
                });
    }
}
