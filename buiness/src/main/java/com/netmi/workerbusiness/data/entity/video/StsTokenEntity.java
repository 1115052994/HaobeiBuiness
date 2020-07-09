package com.netmi.workerbusiness.data.entity.video;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/8/7
 * 修改备注：
 */
public class StsTokenEntity {

    /**
     * RequestId : 1F6F1F06-3B70-45B7-A8BE-4CA06EE93D9E
     * AssumedRoleUser : {"AssumedRoleId":"312301487809823759:client_name","Arn":"acs:ram::1808457215910569:role/langmanji/client_name"}
     * Credentials : {"AccessKeySecret":"goMWWPGE4qPgnDTHRtw3ML3ntSPN9QkKJXRiiJuCsf7","AccessKeyId":"STS.NKBC7wBu3xWLKXCUKDC77VkQy","Expiration":"2019-08-06T08:06:31Z","SecurityToken":"CAIShQJ1q6Ft5B2yfSjIr4j3CI3Dr6oSz5WnSX7ysUsRT7gbua76mzz2IHlOf3FpBeAZvvw+lGpT6fsTlqJ4T55IQ1Dza8J148ybVbdmzc+T1fau5Jko1beRewHKeSGZsebWZ+LmNpa/Ht6md1HDkAJq3LL+bk/Mdle5MJqP++MFDtMMRVuXYCYEZrZRPRAwjM4BKTmrQpTLCBPxhXfKB0dFoxd1jXgFiZ6y2cqB8BHT/jaYo603392hesj4MpY2ZcouCo/ogr1MG/CfgHIK2X9j77xriaFIwzDDs+yGDkNZixf8aLOOqYU2cF8mNvBrS/Qf86Wtj5x8s+rcko3xyRdd/Ez1tMgut+sagAE4oIhV0N4nUp5DgwS8IUeOKGfgJ2vp7FMMlaoHTd2yvQIZ23EbKGxKW50MJL6tTmgUs1ANhnPnRHmTCbrqBpyNHPoJ+BfE/IH7K7jQxict4kLyLH04xLtc/AG3Z7+50gUF585ZqGfScWak0iEBKD5SRO5eP5lGV0Jks1mhHmF3dA=="}
     */

    private String RequestId;
    private AssumedRoleUserBean AssumedRoleUser;
    private CredentialsBean Credentials;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String RequestId) {
        this.RequestId = RequestId;
    }

    public AssumedRoleUserBean getAssumedRoleUser() {
        return AssumedRoleUser;
    }

    public void setAssumedRoleUser(AssumedRoleUserBean AssumedRoleUser) {
        this.AssumedRoleUser = AssumedRoleUser;
    }

    public CredentialsBean getCredentials() {
        return Credentials;
    }

    public void setCredentials(CredentialsBean Credentials) {
        this.Credentials = Credentials;
    }

    public static class AssumedRoleUserBean {
        /**
         * AssumedRoleId : 312301487809823759:client_name
         * Arn : acs:ram::1808457215910569:role/langmanji/client_name
         */

        private String AssumedRoleId;
        private String Arn;

        public String getAssumedRoleId() {
            return AssumedRoleId;
        }

        public void setAssumedRoleId(String AssumedRoleId) {
            this.AssumedRoleId = AssumedRoleId;
        }

        public String getArn() {
            return Arn;
        }

        public void setArn(String Arn) {
            this.Arn = Arn;
        }
    }

    public static class CredentialsBean {
        /**
         * AccessKeySecret : goMWWPGE4qPgnDTHRtw3ML3ntSPN9QkKJXRiiJuCsf7
         * AccessKeyId : STS.NKBC7wBu3xWLKXCUKDC77VkQy
         * Expiration : 2019-08-06T08:06:31Z
         * SecurityToken : CAIShQJ1q6Ft5B2yfSjIr4j3CI3Dr6oSz5WnSX7ysUsRT7gbua76mzz2IHlOf3FpBeAZvvw+lGpT6fsTlqJ4T55IQ1Dza8J148ybVbdmzc+T1fau5Jko1beRewHKeSGZsebWZ+LmNpa/Ht6md1HDkAJq3LL+bk/Mdle5MJqP++MFDtMMRVuXYCYEZrZRPRAwjM4BKTmrQpTLCBPxhXfKB0dFoxd1jXgFiZ6y2cqB8BHT/jaYo603392hesj4MpY2ZcouCo/ogr1MG/CfgHIK2X9j77xriaFIwzDDs+yGDkNZixf8aLOOqYU2cF8mNvBrS/Qf86Wtj5x8s+rcko3xyRdd/Ez1tMgut+sagAE4oIhV0N4nUp5DgwS8IUeOKGfgJ2vp7FMMlaoHTd2yvQIZ23EbKGxKW50MJL6tTmgUs1ANhnPnRHmTCbrqBpyNHPoJ+BfE/IH7K7jQxict4kLyLH04xLtc/AG3Z7+50gUF585ZqGfScWak0iEBKD5SRO5eP5lGV0Jks1mhHmF3dA==
         */

        private String AccessKeySecret;
        private String AccessKeyId;
        private String Expiration;
        private String SecurityToken;

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String AccessKeySecret) {
            this.AccessKeySecret = AccessKeySecret;
        }

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String AccessKeyId) {
            this.AccessKeyId = AccessKeyId;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String Expiration) {
            this.Expiration = Expiration;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String SecurityToken) {
            this.SecurityToken = SecurityToken;
        }
    }
}
