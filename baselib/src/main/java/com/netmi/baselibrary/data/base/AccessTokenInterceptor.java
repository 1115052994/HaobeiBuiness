package com.netmi.baselibrary.data.base;

import android.text.TextUtils;
import android.util.Log;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.entity.AccessToken;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.MD5;
import com.netmi.baselibrary.utils.language.MultiLanguageUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.netmi.baselibrary.data.base.AesGsonConverterFactory.CONTENT_TYPE_AES;
import static com.netmi.baselibrary.data.base.AesGsonConverterFactory.CONTENT_TYPE_JSON;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:08
 * 修改备注：
 */
public class AccessTokenInterceptor implements Interceptor {

    private static final String TAG = "AccessTokenInterceptor";

    private static final String CONTENT_SUB_TYPE_JSON = "json";

    private static final String JSON_ACCESS_TOKEN = ",\"token\":" + "\"%1$s\"" + ",\"language\":%2$d}";

    private static final String JSON_LANGUAGE = ",\"language\":%1$d}";

    /**
     * synchronized 加入同步，避免多线程进入，产生线程同步问题：多次刷新token，token数据出错
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public synchronized Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String postBody = "";

        Request.Builder requestBuilder = request.newBuilder();
        if (canInjectIntoBody(request)) {
            AccessToken accessT = AccessTokenCache.get();
            String accssToken = accessT.getToken();
            if (AccessTokenCache.isTokenExpired()) {
                Logs.d(TAG, "token 过期");
                String token = synchGetNewToken();
//                if (!TextUtils.isEmpty(token))
                accssToken = token;
            }
            Logs.d(TAG, "accssToken :" + accessT.toString());
            RequestBody requestBody = request.body();

            //JSON 实体提交，加入access_token
            if (TextUtils.equals(requestBody.contentType().subtype(), "json")) {
                String json = bodyToString(requestBody);
                if (!TextUtils.isEmpty(json)) {
                    String newJson = json.substring(0, json.length() - 1);
                    //access_token 非空，加入token
                    if (!TextUtils.isEmpty(accssToken)) {
                        newJson += String.format(Locale.getDefault(),
                                JSON_ACCESS_TOKEN, accssToken,
                                MultiLanguageUtil.getInstance().getLanguageResult());
                        RequestBody newRequestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), newJson);
                        requestBuilder.post(newRequestBody);
                        Logs.e(TAG, "POST json：" + postStringToJson(newJson));
                    } else {
                        newJson += String.format(Locale.getDefault(), JSON_LANGUAGE, MultiLanguageUtil.getInstance().getLanguageResult());
                    }
//                    RequestBody newRequestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), newJson);
//                    requestBuilder.post(newRequestBody);
                    postRequest(requestBuilder, MediaType.parse(CONTENT_TYPE_JSON), newJson);
                    postBody = newJson;
                }
            }

            //POST 表单提交，加入access_token
            else {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                //access_token 非空，加入token
                if (!TextUtils.isEmpty(accssToken)) {
                    Logs.i("加入的token:" + accssToken);
                    formBodyBuilder.add("token", accssToken);
                }
                formBodyBuilder.add("language", String.valueOf(MultiLanguageUtil.getInstance().getLanguageResult()));
                /**
                 * 职工家商家端 状态为2
                 * */
                formBodyBuilder.add("user_status", "2");

                RequestBody formBody = formBodyBuilder.build();
                String postBodyString = bodyToString(request.body());
                if (postBodyString != null) {
                    postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
//                    requestBuilder.post(RequestBody.create(request.body().contentType(), postBodyString));
                    postRequest(requestBuilder, requestBody.contentType(), postBodyString);
                }
                postBody = string2JSON(postBodyString).toString();
            }


            request = requestBuilder.header("Content-Type", AesGsonConverterFactory.ENCRYPTION ? CONTENT_TYPE_AES : CONTENT_TYPE_JSON).build();
        }

        Response response = chain.proceed(request);
        String responseBody = response.peekBody(1024 * 1024).string();
        Logs.e(TAG, String.format("%s \n接口url:%s\n请求json:%s\n返回json:%s",
                canInjectIntoBody(request) ? "接口内容" : "没有参数",
                response.request().url(),
                postBody + (AesGsonConverterFactory.ENCRYPTION ? "\n请求密文：" + Aes128CdcUtils.getInstance().encrypt(postBody) : ""),
                (AesGsonConverterFactory.ENCRYPTION ? Aes128CdcUtils.getInstance().decrypt(responseBody) + "\n返回密文：" : "") + responseBody));

        return response;
    }


    private void postRequest(Request.Builder builder, MediaType contentType, String content) {
        if (AesGsonConverterFactory.ENCRYPTION) {
            String encryptContent;
            if (TextUtils.equals(contentType.subtype(), CONTENT_SUB_TYPE_JSON)) {
                encryptContent = Aes128CdcUtils.getInstance().encrypt(content);
            } else {
                encryptContent = Aes128CdcUtils.getInstance().encrypt(string2JSON(content).toString());
            }
            builder.post(RequestBody.create(!TextUtils.isEmpty(encryptContent) ? null : contentType, !TextUtils.isEmpty(encryptContent) ? encryptContent : content));
        } else {
            builder.post(RequestBody.create(contentType, content));
        }
    }


    /**
     * 同步请求方式，获取最新的Token
     */
    private String synchGetNewToken() throws IOException {

        Logs.d(TAG, "into synchGetNewToken()");
        final String login = LoginInfoCache.get().getLogin();
        final String password = LoginInfoCache.get().getPassword();
        final String openid = LoginInfoCache.get().getOpenid();
        if ((TextUtils.isEmpty(login) || TextUtils.isEmpty(password))
                && TextUtils.isEmpty(openid)) {
            Log.e(TAG, "synchGetNewToken: 1111111");
            return null;
        }
        Call<BaseData<UserInfoEntity>> call = synchToken(login, password, openid);
        retrofit2.Response<BaseData<UserInfoEntity>> baseDataResponse = call.execute();

        if (baseDataResponse.isSuccessful() && baseDataResponse.body() != null) {
            BaseData<UserInfoEntity> baseData = baseDataResponse.body();
            //token刷新成功
            Log.e(TAG, "synchGetNewToken: errcode =  " + baseData.getErrcode() + "    errmsg= " + baseData.getErrmsg());

            if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                AccessToken accessToken = baseData.getData().getToken();
                if (accessToken != null) {
                    AccessTokenCache.put(accessToken);
                    return accessToken.getToken();
                }
                //token刷新失败
                else {
                    Log.e(TAG, "synchGetNewToken: 222222222");

                    return null;
                }
            }
            //token刷新失败
            else {
                //已自动登出，请重新登陆
                if (baseData.getErrcode() == Constant.TOKEN_OUT) {
                    Log.e(TAG, "synchGetNewToken: 33333333");

                    return "" + Constant.TOKEN_OUT;
                } else {
                    Log.e(TAG, "synchGetNewToken: 4444444");

                    return null;
                }
            }
        }
        //token刷新失败
        else {
            Log.e(TAG, "synchGetNewToken: 5555555555");
            return null;
        }
    }

    private Call<BaseData<UserInfoEntity>> synchToken(String login, String password, String openid) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient()
                .newBuilder();
        OkHttpClient mOkHttpClient = builder.addInterceptor(logging).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_API)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        return retrofit.create(LoginApi.class).callLogin(login, MD5.GetMD5Code(password));
        return retrofit.create(LoginApi.class).callLogin(login, MD5.GetMD5Code(password, true), "login_phone");
    }

    /**
     * 将请求提转为String
     *
     * @param request
     * @return
     */
    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        }
    }

    /**
     * 将表单提交的内容转为JSON查看
     *
     * @param post
     * @return
     */
    public static String postStringToJson(String post) {
        String subJson[] = post.split("&");
        String newJson = "{";
        for (int i = 0; i < subJson.length; i++) {
            newJson += "\"" + subJson[i].replace("=", "\":\"") + "\",";
        }
        return newJson.substring(0, newJson.length() - 1) + "}";
    }


    /**
     * 请求是否可以注入参数
     *
     * @param request
     * @return
     */
    private boolean canInjectIntoBody(Request request) {

        if (request == null) {
            return false;
        }

        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }

        RequestBody body = request.body();
        if (body == null) {
            return false;
        }

        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }

        //针对 POST 表单 x-www-form-urlencoded 和 POST 实体 json
        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded") && !TextUtils.equals(mediaType.subtype(), "json")) {
            return false;
        }
        return true;
    }


    /**
     * 将表单提交的内容转为JSON查看
     */
    private JSONObject string2JSON(String str) {
        JSONObject json = new JSONObject();
        try {
            String[] arrStr = str.split("&");
            for (String formString : arrStr) {
                String[] arrKeyValue = formString.split("=");

                if (arrKeyValue.length < 2 || TextUtils.isEmpty(arrKeyValue[0]) || TextUtils.isEmpty(arrKeyValue[1])) {
                    continue;
                }

                String key = getUtf8Text(arrKeyValue[0]).replace("[]", "");
                String value = getUtf8Text(arrKeyValue[1]);
                if (json.isNull(key)) {
                    if (getUtf8Text(arrKeyValue[0]).contains("[]")) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(value);
                        json.put(key, jsonArray);
                    } else {
                        json.put(key, value);
                    }
                } else {
                    JSONArray jsonArray = null;
                    if (json.get(key) instanceof JSONArray) {
                        jsonArray = json.getJSONArray(key);
                    } else {
                        jsonArray = new JSONArray();
                        jsonArray.put(json.get(key));
                    }
                    jsonArray.put(value);
                    json.put(key, jsonArray);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private String getUtf8Text(String content) {
        try {
            return URLDecoder.decode(content, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}
