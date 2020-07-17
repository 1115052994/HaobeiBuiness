package com.netmi.workerbusiness.ui.home;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.netmi.baselibrary.data.base.RetrofitApiFactory;
import com.netmi.baselibrary.data.base.RxSchedulers;
import com.netmi.baselibrary.data.base.XObserver;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.ui.BaseActivity;
import com.netmi.baselibrary.ui.BaseWebviewActivity;
import com.netmi.baselibrary.utils.JumpUtil;
import com.netmi.workerbusiness.R;
import com.netmi.workerbusiness.data.api.CouponApi;
import com.netmi.workerbusiness.data.api.MineApi;
import com.netmi.workerbusiness.data.entity.home.HaibeiConfidenceEntity;
import com.netmi.workerbusiness.data.entity.mine.ContentEntity;
import com.netmi.workerbusiness.databinding.ActivityHaibeiSpareBinding;
import com.netmi.workerbusiness.ui.utils.MyTimeUtil;
import com.netmi.workerbusiness.utils.HTMLFormat;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_CONTENT;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TITLE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE;
import static com.netmi.baselibrary.ui.BaseWebviewActivity.WEBVIEW_TYPE_CONTENT;

public class HaibeiSpareActivity extends BaseActivity<ActivityHaibeiSpareBinding> {

    private String meth;
    private String meth1;
    private float startx;
    private float starty;
    private float offsetx;
    private float offsety;
    private int i = 0;

    /**
     * 获取布局
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_haibei_spare;
    }

    /**
     * 界面初始化
     */
    @Override
    protected void initUI() {
        getTvTitle().setText("海贝指数");
        getRightSetting().setVisibility(View.VISIBLE);
        mBinding.tvRemark1.setText("（近7日）");
        mBinding.tvRemark2.setText("（近7日）");
        getFinancial("1");
        doWebView(mBinding.wvEquity, meth1);
        doWebView(mBinding.wv, meth);
    }

    /**
     * 数据初始化
     */
    @Override
    protected void initData() {
    }

    @Override
    public void doClick(View view) {
        super.doClick(view);
        if (view.getId() == R.id.ll_question) {
            service(39);
        } else if (view.getId() == R.id.cb_day) {
            mBinding.day.setChecked(true);
            mBinding.day.setTextColor(getResources().getColor(R.color.white));
            mBinding.week.setChecked(false);
            mBinding.week.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.month.setChecked(false);
            mBinding.month.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.tvRemark1.setText("（近7日）");
            mBinding.tvRemark2.setText("（近7日）");
            getFinancial("1");
        } else if (view.getId() == R.id.cb_week) {
            mBinding.day.setChecked(false);
            mBinding.day.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.week.setChecked(true);
            mBinding.week.setTextColor(getResources().getColor(R.color.white));
            mBinding.month.setChecked(false);
            mBinding.month.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.tvRemark1.setText("（近12周）");
            mBinding.tvRemark2.setText("（近12周）");
            getFinancial("2");
        } else if (view.getId() == R.id.cb_month) {
            mBinding.day.setChecked(false);
            mBinding.day.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.week.setChecked(false);
            mBinding.week.setTextColor(getResources().getColor(R.color.theme_text_black));
            mBinding.month.setChecked(true);
            mBinding.month.setTextColor(getResources().getColor(R.color.white));
            mBinding.tvRemark1.setText("（近12月）");
            mBinding.tvRemark2.setText("（近12月）");
            getFinancial("3");
        }
    }

    private void doWebView(WebView webView, String method) {

        //开启本地文件读取（默认为true，不设置也可以）
        webView.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/echart/echart.html");

        webView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        startx = event.getX();
                        starty = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetx = Math.abs(event.getX() - startx);
                        offsety = Math.abs(event.getY() - starty);
                        if (offsetx > offsety) {
                            ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        } else {
                            ((WebView) v).requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mBinding.wvEquity.loadUrl(meth1);
                i++;
                if (i == 2) {
                    getFinancial("1");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void getFinancial(String type) {
        RetrofitApiFactory.createApi(CouponApi.class)
                .doList(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<PageEntity<HaibeiConfidenceEntity>>>() {
                    @Override
                    public void onSuccess(BaseData<PageEntity<HaibeiConfidenceEntity>> data) {
                        if (dataExist(data)) {
                            List<String> listY = new ArrayList<>();
                            List<String> listY1 = new ArrayList<>();
                            List<String> listX = new ArrayList<>();

                            if (data.getData().getList().size() > 0) {
                                for (int i = 0; i < data.getData().getList().size(); i++) {
                                    listY.add(String.valueOf(data.getData().getList().get(i).getConfidence()));
                                    listY1.add(String.valueOf(data.getData().getList().get(i).getSynthesize()));
//                                    listX.add(entity.getTime());
                                    if (type.equals("1")) {
                                        listX.add(MyTimeUtil.getStringTime3(data.getData().getList().get(i).getTime()));
                                    } else if (type.equals("2")) {   //按周显示
                                        listX.add("第" + (i + 1) + "周");
                                    } else if (type.equals("3")) {   //按月显示
                                        listX.add("第" + (i + 1) + "月");
                                    }
                                }
                                String line = "line";
                                meth = "javascript:createChart('" + line + "','" + JSON.toJSONString(listY) + "','" + JSON.toJSONString(listX) + "')";
                                //综合分数
                                meth1 = "javascript:createChart('" + line + "','" + JSON.toJSONString(listY1) + "','" + JSON.toJSONString(listX) + "')";
                                mBinding.tvOne.setText(data.getData().getList().get(data.getData().getList().size() - 1).getOne());
                                mBinding.tvTwo.setText(data.getData().getList().get(data.getData().getList().size() - 1).getTwo());
                                doEcharts(mBinding.wvEquity, meth1);
                                doEcharts(mBinding.wv, meth);
                                      /*mBinding.wvEquity.loadUrl(meth1);
                                mBinding.wv.loadUrl(meth);//信心指数*/
                            }
                            mBinding.setData(data.getData());
                        }
//                        doEquity();
//                        doConfidence();
                    }
                });
    }

    private void service(Integer type) {
        RetrofitApiFactory.createApi(MineApi.class)
                .content(type)
                .compose(RxSchedulers.compose())
                .compose((this).bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new XObserver<BaseData<ContentEntity>>() {
                    @Override
                    public void onSuccess(BaseData<ContentEntity> data) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WEBVIEW_TITLE, data.getData().getTitle());
                        bundle.putInt(WEBVIEW_TYPE, WEBVIEW_TYPE_CONTENT);
                        bundle.putString(WEBVIEW_CONTENT, HTMLFormat.getNewData(data.getData().getContent()));
                        JumpUtil.overlay(getContext(), BaseWebviewActivity.class, bundle);
                    }
                });
    }

    private void doEcharts(WebView webView, String method) {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            webView.loadUrl(method);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(method, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                    }
                });
            }

        }
    }


    private void doEquity() {
        //开启本地文件读取（默认为true，不设置也可以）
        mBinding.wvEquity.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        mBinding.wvEquity.getSettings().setJavaScriptEnabled(true);

        mBinding.wvEquity.loadUrl("file:///android_asset/echart/echart.html");

        mBinding.wvEquity.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        startx = event.getX();
                        starty = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetx = Math.abs(event.getX() - startx);
                        offsety = Math.abs(event.getY() - starty);
                        if (offsetx > offsety) {
                            ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        } else {
                            ((WebView) v).requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }

        });

        mBinding.wvEquity.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.wvEquity.loadUrl(meth1);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void doConfidence() {
        //开启本地文件读取（默认为true，不设置也可以）
        mBinding.wv.getSettings().setAllowFileAccess(true);
        //开启脚本支持
        mBinding.wv.getSettings().setJavaScriptEnabled(true);

        mBinding.wv.loadUrl("file:///android_asset/echart/echart.html");

        mBinding.wv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        startx = event.getX();
                        starty = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetx = Math.abs(event.getX() - startx);
                        offsety = Math.abs(event.getY() - starty);
                        if (offsetx > offsety) {
                            ((WebView) v).requestDisallowInterceptTouchEvent(true);
                        } else {
                            ((WebView) v).requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        mBinding.wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.wv.loadUrl(meth);//信心指数
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

}
