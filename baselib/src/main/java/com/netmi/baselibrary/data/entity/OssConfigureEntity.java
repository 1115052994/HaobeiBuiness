package com.netmi.baselibrary.data.entity;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/6 16:15
 * 修改备注：
 */
public class OssConfigureEntity {

    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
    private String endPoint;
    private String visitUrl;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    @Override
    public String toString() {
        return "OssConfigureEntity{" +
                "accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", bucket='" + bucket + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", visitUrl='" + visitUrl + '\'' +
                '}';
    }
}
