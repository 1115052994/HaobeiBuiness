package com.liemi.basemall.ui.personal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.liemi.basemall.R;
import com.liemi.basemall.data.api.MineApi;
import com.liemi.basemall.databinding.ActivityMyInformationBinding;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.base.ApiException;
import com.netmi.baselibrary.data.base.BaseObserver;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.cache.UserInfoCache;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.ToastUtils;
import com.netmi.baselibrary.utils.glide.GlideShowImageUtils;
import com.netmi.baselibrary.utils.oss.OssUtils;
import com.netmi.baselibrary.widget.InputDialog;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.NonNull;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_TAKE;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/12/7 15:42
 * 修改备注：
 */
public class MyInformationActivity extends BaseActivity<ActivityMyInformationBinding> {
    //ALIPAY 请求打开相机的requestCode
//    private static final int REQUEST_OPEN_CAMERA_ALIPAY = 1001;
    //ALIPAY 请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM_ALIPAY = 1002;
    //WECHAT PAY 请求打开相机的requestCode
//    private static final int REQUEST_OPEN_CAMERA_WXPAY = 2001;
    //WECHAT PAY 请求打开相册的requestCode
    private static final int REQUEST_OPEN_ALBUM_WXPAY = 2002;
    private int mCurrentStep = 0;//当前执行的操作，0位默认，1为请求相机，2为请求读写权限
    //实名认证接口地址

    private String title = "我的资料";
    //支付宝二维码图片
    private List<String> aliQrcode = new ArrayList<>();
    //微信二维码图片
    private List<String> wxQrcode = new ArrayList<>();
    private int currentCode = -1;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_information;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(title);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
//        if (view.getId() == R.id.tv_commit_ali) {
//            showError("提交支付宝二维码");
//            return;
//        }
//        if (view.getId() == R.id.tv_commit_wechat) {
//            showError("提交微信二维码");
//            return;
//        }
        if (view.getId() == R.id.tv_confirm) {
            check();
            return;
        }
        if (view.getId() == R.id.tv_setting) {
            showError("修改");
            return;
        }

    }

    private void check() {

    }


}