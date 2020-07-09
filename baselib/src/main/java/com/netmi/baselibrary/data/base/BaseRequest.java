package com.netmi.baselibrary.data.base;

import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.entity.AccessToken;

/**
 * 类描述：
 * 创建人：Sherlock
 * 创建时间：2019/5/27
 * 修改备注：
 */
public class BaseRequest<T> {
    private String token;
    private String url;
    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseRequest( String url, T data) {
        this.token = AccessTokenCache.get().getToken();
        this.url = url;
        this.data = data;
    }
}
