package com.netmi.baselibrary.utils.oss;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.netmi.baselibrary.data.entity.OssConfigureEntity;
import com.netmi.baselibrary.ui.MApplication;
import com.netmi.baselibrary.utils.BitmapUtil;
import com.netmi.baselibrary.utils.Logs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/6/8 17:14
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class OssUtils {

    private OSS oss;

    // 运行sample前需要配置以下字段为有效的值
    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "8C17a7KHludNpQFJ";
    private static String accessKeySecret = "VygVO1luFJwIWDZE0KvTC0lWNmRD49";

    private static String OSS_REGION_HOST_ID = "oss-cn-hangzhou.aliyuncs.com";
    private static String OSS_BUCKET = "liemimofang";
    public static String OSS_HOST = "https://" + OSS_BUCKET + "." + OSS_REGION_HOST_ID + "/";

    public static void initConfigure(OssConfigureEntity configure) {
//        endpoint = "http://" + configure.getEndPoint();
        endpoint = "https://" + configure.getEndPoint();
        OSS_BUCKET = configure.getBucket();
        accessKeyId = configure.getAccessKeyId();
        accessKeySecret = configure.getAccessKeySecret();
        OSS_REGION_HOST_ID = configure.getEndPoint();
        OSS_HOST = "https://" + OSS_BUCKET + "." + OSS_REGION_HOST_ID + "/";
    }

    public OssUtils initOss() {
        OSSCredentialProvider credentialProvider = new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {
                return OSSUtils.sign(accessKeyId, accessKeySecret, content);
            }
        };
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(MApplication.getAppContext(), endpoint, credentialProvider, conf);
        return this;
    }

    // 从本地文件上传，采用阻塞的同步接口
    public String putObjectFromLocalFile(String uploadFilePath) {
        // 构造上传请求
        String fileExt = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1); // 文件后缀名
        String objectKey = setOssFileName(fileExt);
        PutObjectRequest put = new PutObjectRequest(OSS_BUCKET, objectKey, uploadFilePath);

        try {

            PutObjectResult putResult = oss.putObject(put);

            Logs.d("PutObject", "UploadSuccess");

            Logs.d("ETag", putResult.getETag());
            Logs.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Logs.e("RequestId", e.getRequestId());
            Logs.e("ErrorCode", e.getErrorCode());
            Logs.e("HostId", e.getHostId());
            Logs.e("RawMessage", e.getRawMessage());
        }
        return OssUtils.OSS_HOST + objectKey;
    }

    //压缩上传
    public void putObjectSyncZip(String uploadFilePath, OSSCompletedCallback<PutObjectRequest, PutObjectResult> listen) {
        //压缩操作
        String[] split = uploadFilePath.split("[.]");
        String path = "";
        for (int i = 0; i <split.length ; i++) {
            if(i<split.length){
                path=path+split[i]+".";
            }else {
                path= path+split[i];
            }
        }
        File file = new File(path);
        BitmapUtil.pixeCompressBitmap(uploadFilePath,file);
        String zipPath = file.toString();


        String fileExt = zipPath.substring(zipPath.lastIndexOf(".") + 1); // 文件后缀名
        String objectKey = setOssFileName(fileExt);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(OSS_BUCKET, objectKey, zipPath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Logs.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, listen);
    }


    public void putObjectSync(String uploadFilePath, OSSCompletedCallback<PutObjectRequest, PutObjectResult> listen) {

        String fileExt = uploadFilePath.substring(uploadFilePath.lastIndexOf(".") + 1); // 文件后缀名
        String objectKey = setOssFileName(fileExt);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(OSS_BUCKET, objectKey, uploadFilePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Logs.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, listen);
    }

    private List<String> uploadSuccessFile = new ArrayList<>();

    //多文件异步上传
    public void multiPutObjectSync(final List<String> uploadFilePaths, final int current, final MultiPutListener multiPutListener) {
        if (uploadFilePaths == null || uploadFilePaths.isEmpty() || current >= uploadFilePaths.size())
            return;

        if (current == 0)
            uploadSuccessFile.clear();

        putObjectSync(uploadFilePaths.get(current), new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                uploadSuccessFile.add(OssUtils.OSS_HOST + putObjectRequest.getObjectKey());
                if (current < uploadFilePaths.size() - 1) {
                    multiPutObjectSync(uploadFilePaths, current + 1, multiPutListener);
                } else if (multiPutListener != null) {
                    multiPutListener.onSuccess(uploadSuccessFile);
                }
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                if (multiPutListener != null)
                    multiPutListener.onFailure(putObjectRequest, e, e1);
            }
        });

    }

    private String setOssFileName(String var0) {
        SimpleDateFormat var1 = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        Date var2 = new Date();
        String var3 = var1.format(var2);
        String var4 = UUID.randomUUID().toString();
        var4 = var4.substring(0, 8) + var4.substring(9, 13) + var4.substring(14, 18) + var4.substring(19, 23) + var4.substring(24);
        return var3 + "/" + var4 + "." + var0;
    }



}
