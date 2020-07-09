package com.netmi.baselibrary.data.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.utils.AppManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:07
 * 修改备注：
 */
public class RetrofitApiFactory {


    /**
     * OkhttpClient
     */
    private static OkHttpClient mOkHttpClient;

    /**
     * 代理接口构建类
     */
    private static Retrofit retrofit;

    /**
     * Http连接超时
     */
    private static int CONNECT_TIMEOUT = 30;

    /**
     * Http 读取超时
     */
    private static int READ_TIMEOUT = 30;

    /**
     * Http 写入超时
     */
    private static int WRITE_TIMEOUT = 30;

    /**
     * 缓存大小
     */
    private static int CACHE_SIZE = 10 * 1024 * 1024;
    /**
     * 记录上一次是否添加了token
     */
    private static boolean haveToken = true;

    static {
        buildOkHttpClient();
        buildRetrofit();
    }

    /**
     * 构建 Retrofit
     */
    private static void buildRetrofit() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>())
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_API)
                .client(mOkHttpClient)
                .addConverterFactory(AesGsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对转换后的数据进行再包装
                .build();
    }

    /**
     * OkHttpClient 请求配置
     */
    private static void buildOkHttpClient() {

//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder builder = new OkHttpClient()
//                .newBuilder()
//                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
//                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
//                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
//        Cache cache = new Cache(AppManager.getApp().getCacheDir(), CACHE_SIZE);
//        builder.cache(cache);
//        //加入日志拦截器
//        mOkHttpClient = builder.addInterceptor(logging).addInterceptor(new AccessTokenInterceptor()).build();
        buildOkHttpClient(true);
    }

    /**
     * OkHttpClient 请求配置
     */
    private static void buildOkHttpClient(boolean addToken) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        Cache cache = new Cache(AppManager.getApp().getCacheDir(), CACHE_SIZE);
        builder.cache(cache);
        //加入日志拦截器
        if (addToken)
            mOkHttpClient = builder.addInterceptor(logging).addInterceptor(new AccessTokenInterceptor()).build();
        else
            mOkHttpClient = builder.addInterceptor(logging).build();
        haveToken = addToken;
    }


    /**
     * 创建不同的Api 接口对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T createApi(Class<T> serviceClass) {
        return createApi(serviceClass,true);
    }

    /**
     * 创建不同的Api 接口对象
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    public static <T> T createApi(Class<T> serviceClass, boolean addToken) {
        //不需要添加token
        if (!addToken) {
            //上一次需要token
            if (haveToken) {
                //修改
                buildOkHttpClient(false);
                buildRetrofit();
            }
            //需要token
        } else if (addToken)
            //上一次没有token
            if (!haveToken) {
                //修改
                buildOkHttpClient(true);
                buildRetrofit();
            }
        return retrofit.create(serviceClass);
    }
}
