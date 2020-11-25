package com.netmi.baselibrary.data;

import android.os.Environment;

import com.netmi.baselibrary.utils.AppUtils;

import java.io.File;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/4 18:05
 * 修改备注：
 */
public class Constant {

    /**
     * 服务器Api地址
     */
    public static final String BASE_API = AppUtils.getBaseApi();

    public static final String BASE_HTML = AppUtils.getBaseHtml();

    //正式服   新   http://hb-api.ksebao.com/   老http://hb-api.haobeitech.com/
    //测试服   新   http://hb-api-test.ksebao.com/  老http://hb-api-test.haobeitech.com/
    public static final String BASE_API_RELEASE = "http://hb-api.ksebao.com/";
    //测试
    public static final String BASE_API_DEBUG = "http://hb-api-test.ksebao.com/";


    public static final String BASE_HTML_DEBUG = BASE_API_DEBUG;

    public static final String BASE_HTML_RELEASE = BASE_API_RELEASE;

    /**
     * 分享商品
     */
    public static final String SHARE_GOOD = BASE_HTML + "netmi-shop-h5/dist/#/goods?goods_id=";

    /**
     * Http 请求成功
     */
    public static final int SUCCESS_CODE = 0;

    /**
     * token错误
     */
    public static final int TOKEN_FAIL = 10000;

    /**
     * 已自动登出，请重新登陆
     */
    public static final int TOKEN_OUT = 1200;

    /**
     * 项目文件缓存文件夹
     */
    private final static String FILE_PATH = "hi_cloth";

    /**
     * 是否关闭推送
     */
    public static final String IS_PUSH = "is_push";

    /**
     * 平台类型（1:IOS,2:安卓,3:PC,4:移动H5）
     */
    public static final int PLATFORM_TYPE = 2;

    /**
     * 默认获取全部的条数
     */
    public static final int ALL_PAGES = 5000;

    /**
     * 默认一页的条数
     */
    public static final int PAGE_ROWS = 20;

    /**
     * 得到当前外部存储设备的目录
     */
    public static final String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FILE_PATH + File.separator;

    /**
     * 压缩图片缓存文件夹
     */
    public static final String COMPRESS_IMAGE_CACHE_DIR = "compress_images";

    public static final String COMPRESS_CACHE_DIR = "cache_images";

    /**
     * 相机权限
     */
    public static final int CAMERA_REQUEST_CODE = 0x13;

    /**
     * 刷新
     */
    public static final int PULL_REFRESH = 0;

    /**
     * 加载更多
     */
    public static final int LOAD_MORE = 1;

    /**
     * 信鸽推送别名前缀
     */
    public static final String PUSH_PREFIX = "liemi_";

    public static final String PUSH_PREFIX_DEVELOP = "liemi_test_";//测试

    public static String getPushPrefix() {
        return AppUtils.isAppDebug() ? PUSH_PREFIX_DEVELOP : PUSH_PREFIX;
    }

    /**
     * 是否第一次登录
     */
    public static final String FIRST_LOGIN = "firstLogin";

    /**
     * 订单状态,0-未付款1-待发货2-待收货3-待评价4-退货申请5-退货申请驳回6-退货中7-已退货8-取消交易9-交易完成10-支付失败
     */
    public static final String ORDER_STATE = "order_state";
    public static final String ORDER_MPID = "order_mpid";
    public static final int ORDER_WAIT_PAY = 0;
    public static final int ORDER_WAIT_SEND = 1;
    public static final int ORDER_WAIT_RECEIVE = 2;
    public static final int ORDER_WAIT_COMMENT = 3;
    public static final int ORDER_SUCCESS = 9;
    public static final int ORDER_REFUND_ASK = 4;
    public static final int ORDER_REFUND_NOT_ALLOW = 5;
    public static final int ORDER_REFUND_NOW = 6;
    public static final int ORDER_REFUND_SUCCESS = 7;
    public static final int ORDER_CANCEL = 8;
    public static final int ORDER_PAY_FAIL = 10;
    /**
     * 订单业务,1： order_remind： 提醒发货 2：order_delete:删除订单, 3：order_cancel：取消订单, 4：order_take：确认收货, 5：order_refund：申请退款
     */
    public static final String ORDER_DO_REMIND = "order_remind";
    public static final String ORDER_DO_DELETE = "order_delete";
    public static final String ORDER_DO_CANCEL = "order_cancel";
    public static final String ORDER_DO_ACCEPT = "order_take";
    public static final String ORDER_DO_REFUND = "order_refund";

    /**
     * 默认倒计时时间 60 * 1000
     */
    public static final long COUNT_DOWN_TIME_DEFAULT = 60 * 1000;

    /**
     * 不同登录方式的组合
     */
    public static final String LOGIN_DEFAULT = "login_default";//用户名+密码登录
    public static final String LOGIN_PHONE = "login_phone";//手机号+密码登录
    public static final String LOGIN_CODE = "login_code";//手机号+验证码登录
    public static final String LOGIN_WECHAT = "login_wechat";//微信登录

    /**
     * 不同获取验证码的组合
     */
    public static final String AUTH_CODE_REGISTER = "register";//注册验证码
    public static final String AUTH_CODE_LOGIN = "login";//登录验证码
    public static final String AUTH_CODE_RESET = "reset";//忘记密码验证码
    public static final String AUTH_CODE_PAY_PASSWORD = "payPassword";//设置支付密码
    public static final String AUTH_CODE_PAY = "inside";//交易支付
    public static final String AUTH_RESET_PASSWORD = "resetPassword";//忘记登录密码
    public static final String AUTH_RESET_PAY_PASSWORD = "resetPayPassword";//忘记资产密码
    public static final String AUTH_CODE_OTC = "OTC";//

    //用户能够输入最大密码长度
    public static final int MAX_PASSWORD_LENGTH = 16;
    //用户输入的最小密码长度
    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final String SAVE_FILE_ROOT_PATH = "/lantian";

    public static final class SharedPreferencesConfig {
        public static final String IS_MAIL_UNREAD = "is_mail_unread";
        public static final String LANGUAGE = "language";
        public static final String IS_PUSH = "is_push";
        public static final String ADD_FRIEND_UNREAD = "add_friend_unread";
        public static final String ADD_COUPON_UNREAD = "add_coupon_unread";
    }


    //shelf
    public static final String SHELF_STATE = "shelf_state";
    public static final String SHELF_UP = "5";
    public static final String SHELF_PENDING = "2";
    public static final String SHELF_DOWN = "1";


}
