package com.netmi.baselibrary.ui;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.netmi.baselibrary.R;
import com.netmi.baselibrary.databinding.BaselibActivityWebviewBinding;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 10:29
 * 修改备注：
 */
public class BaseWebviewActivity extends BaseActivity<BaselibActivityWebviewBinding> {

    public static final String WEBVIEW_TITLE = "webview_title";

    //2跳转链接， 1跳转富文本
    public static final String WEBVIEW_TYPE = "webview_type";

    public static final String WEBVIEW_CONTENT = "webview_content";

    public static final int WEBVIEW_TYPE_URL = 2;

    public static final int WEBVIEW_TYPE_CONTENT = 1;

    public final static String CSS_STYLE = "<style>* {font-size:45px;line-height:50px;}p </style>";
    public final static String CSS_STYLE_NEW = "<style>* {font-size:45px;line-height:50px;}p {color:#FFFFFF;}</style>";

    @Override
    protected int getContentView() {
        return R.layout.baselib_activity_webview;
    }

    @Override
    protected void initUI() {
        getTvTitle().setText(getIntent().getStringExtra(WEBVIEW_TITLE));
        WebView webView = mBinding.wvWeb;
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (TextUtils.isEmpty(getTvTitle().getText())) {
                    getTvTitle().setText(title);
                }
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //支持js
//        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING); //支持内容重新布局
        else
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
//        webSettings.setDefaultFontSize(35);

        //支持http和https的混合模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (getIntent().getIntExtra(WEBVIEW_TYPE, WEBVIEW_TYPE_URL) == WEBVIEW_TYPE_URL) {
            webView.loadUrl(getIntent().getStringExtra(WEBVIEW_CONTENT));
        } else {
            webView.loadData(getIntent().getStringExtra(WEBVIEW_CONTENT), "text/html; charset=UTF-8", null);
        }
    }

    @Override
    protected void initData() {

    }
}
