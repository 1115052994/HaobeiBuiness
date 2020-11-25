package com.netmi.baselibrary.data.entity;

import java.io.Serializable;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/1/29 11:26
 * 修改备注：
 */
public class UserInfoEntity implements Serializable {
    //性别分类
    public static final int SEX_MAN = 1;//男
    public static final int SEX_WOMEN = 2;//女
    public static final int SEX_UNKNOW = 3;//未知
    //是否绑定
    public static final int BIND = 1;//绑定
    public static final int UNBIND = 0;//未绑定
    //实名认证状态
    public static final int UNCHECK = 0;//未验证
    public static final int PASS = 1;//验证通过
    public static final int NOT_PASS = 2;//未通过
    public static final int CHECKING = 3;//验证中


    //用户主键
    private String uid;
    //手机号
    private String phone;
    //微信用户主键，备用字段
    private String wechat_id;
    //昵称
    private String nickname;
    //用户头像
    private String head_url;
    //会员积分
    private float score;
    //会员等级
    private int level;
    //注册时间
    private String create_time;
    //性别
    private int sex;
    //性别字符,自定义字段
    private String sexFormat;
    //出生日期
    private String date_birth;
    //年龄
    private int age;
    //设备绑定id
    private String audio_device_id;

    public String getAudio_device_id() {
        return audio_device_id;
    }

    public void setAudio_device_id(String audio_device_id) {
        this.audio_device_id = audio_device_id;
    }

    //身份证号
    private String id_card;
    //省id
    private String p_id;
    //市id
    private String c_id;
    //区id
    private String d_id;
    //详细地址
    private String address;
    //地址明细
    private String full_address;
    //个性签名
    private String sign_name;
    //用户token
    private AccessToken token;
    //是否绑定QQ 0:否  1：是
    private int is_bind_qq;
    //是否绑定微博
    private int is_bind_weibo;
    //是否绑定邮箱
    private int is_bind_email;
    //是否绑定微信
    private int is_bind_wechat;
    //是否绑定手机号
    private int is_bind_phone;
    //是否设置支付密码
    private int is_set_paypassword;
    //可用eth
    private String eth_remain;
    //可用eth对应的cny
    private String eth_cny;

    //邀请码
    private String invite_code;
    //用户二维码
    private String qrcode;
    //下载连接二维码
    private String url_android;
    //联系我们的手机号
    private String telephone;
    //是否设置钱包密码 0否1是
    private int wallet_status;
    //yms余额
    private String yms_remain;
    //钱包地址
    private String wallet_address;
    //余额价值（人民币）
    private String total_price;

    //邀请码
    private String share_code;

    //秘钥
    private String secret_key;

    //邮箱
    private String email;


    //app下载链接二维码
    private String app_share_url;


    public String getAuthorStatus() {
        switch (auth_type) {
            case PASS:
                return "已认证";
            case NOT_PASS:
                return "未认证";
            case CHECKING:
                return "审核中";
            default:
                return "未认证";
        }
    }

    /**
     * 职工家二期
     * shop_user_type	    string	用户选择商户类型 0:未选择类型 1:线上 2:线下 3:线上+线下
     * shop_apply_status	string	用户申请入驻审核状态 0:未申请 1:审核中 2:审核成功 3:审核失败
     */
    private String shop_user_type;
    private String shop_apply_status;
    private String shop_pay_status;
    /**
     * 云信ID
     */
    private String cid;
    private String yun_token;



    public String getShop_user_type() {
        return shop_user_type;
    }

    public void setShop_user_type(String shop_user_type) {
        this.shop_user_type = shop_user_type;
    }

    public String getShop_apply_status() {
        return shop_apply_status;
    }

    public void setShop_apply_status(String shop_apply_status) {
        this.shop_apply_status = shop_apply_status;
    }

    public String getWallet_address() {
        return wallet_address;
    }

    public void setWallet_address(String wallet_address) {
        this.wallet_address = wallet_address;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    //个人银行卡资料等验证状态 0未验证 1已验证
    private int auth_type;

    //VIP会员资料审核状态 -1未申请 0 申请中 1申请通过 2申请拒绝
    private int is_auth_vip;

    // 个人资料审核状态 -1未申请 0 申请中 1申请通过 2申请拒绝
    private int is_auth;

    //用户vip等级，0：非有效会员，1：有效会员2：一级米商3：二级米商4：三级米商5：四级米商
    private int vip_level;

    public int getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(int auth_type) {
        this.auth_type = auth_type;
    }


    public int getVip_level() {
        return vip_level;
    }

    public void setVip_level(int vip_level) {
        this.vip_level = vip_level;
    }

    public String getYms_remain() {
        return yms_remain;
    }

    public void setYms_remain(String yms_remain) {
        this.yms_remain = yms_remain;
    }

    public int getIs_set_paypassword() {
        return is_set_paypassword;
    }

    public void setIs_set_paypassword(int is_set_paypassword) {
        this.is_set_paypassword = is_set_paypassword;
    }

    private String birthday;
    private String com_name;

    public int getWallet_status() {
        return wallet_status;
    }

    public void setWallet_status(int wallet_status) {
        this.wallet_status = wallet_status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getUrl_android() {
        return url_android;
    }

    public void setUrl_android(String url_android) {
        this.url_android = url_android;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSexFormat() {
        if (sex == SEX_MAN) {
            return "男";
        } else if (sex == SEX_WOMEN) {
            return "女";
        } else if (sex == SEX_UNKNOW) {
            return "未知";
        }
        return sexFormat;
    }

    public void setSexFormat(String sexFormat) {
        this.sexFormat = sexFormat;
    }

    public String getYun_token() {
        return yun_token;
    }

    public void setYun_token(String yun_token) {
        this.yun_token = yun_token;
    }

    public String getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(String date_birth) {
        this.date_birth = date_birth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getSign_name() {
        return sign_name;
    }

    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    public AccessToken getToken() {
        return token;
    }

    public void setToken(AccessToken token) {
        this.token = token;
    }

    public int getIs_bind_qq() {
        return is_bind_qq;
    }

    public void setIs_bind_qq(int is_bind_qq) {
        this.is_bind_qq = is_bind_qq;
    }

    public int getIs_bind_weibo() {
        return is_bind_weibo;
    }

    public void setIs_bind_weibo(int is_bind_weibo) {
        this.is_bind_weibo = is_bind_weibo;
    }

    public int getIs_bind_email() {
        return is_bind_email;
    }

    public void setIs_bind_email(int is_bind_email) {
        this.is_bind_email = is_bind_email;
    }

    public int getIs_bind_wechat() {
        return is_bind_wechat;
    }

    public void setIs_bind_wechat(int is_bind_wechat) {
        this.is_bind_wechat = is_bind_wechat;
    }

    public int getIs_bind_phone() {
        return is_bind_phone;
    }

    public void setIs_bind_phone(int is_bind_phone) {
        this.is_bind_phone = is_bind_phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getEth_remain() {
        return eth_remain;
    }

    public void setEth_remain(String eth_remain) {
        this.eth_remain = eth_remain;
    }

    public String getEth_cny() {
        return eth_cny;
    }

    public void setEth_cny(String eth_cny) {
        this.eth_cny = eth_cny;
    }

    public String getShare_code() {
        return share_code;
    }

    public void setShare_code(String share_code) {
        this.share_code = share_code;
    }


    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getIs_auth_vip() {
        return is_auth_vip;
    }

    public void setIs_auth_vip(int is_auth_vip) {
        this.is_auth_vip = is_auth_vip;
    }

    public String getApp_share_url() {
        return app_share_url;
    }

    public void setApp_share_url(String app_share_url) {
        this.app_share_url = app_share_url;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getShop_pay_status() {
        return shop_pay_status;
    }

    public void setShop_pay_status(String shop_pay_status) {
        this.shop_pay_status = shop_pay_status;
    }
}
