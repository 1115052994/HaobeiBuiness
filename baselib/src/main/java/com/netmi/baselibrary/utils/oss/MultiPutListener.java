package com.netmi.baselibrary.utils.oss;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;

import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/16 11:06
 * 修改备注：
 */
public interface MultiPutListener {

    public void onSuccess(List<String> uploads);

    public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1);

}
