package com.netmi.baselibrary.data.entity;

/**
 * Created by Bingo on 2018/2/27.
 */

public class UpdateVersionDto {
    /**
     * id : 1
     * edi_num : 1.1.0
     * code : 11
     * apk : http://qianyi.netmi.com.cn/weixin/download/hz.apk
     * infor : 患者端更新
     * create_time : 2017-11-08 13:33:29
     */

    private String id;
    private String edi_num;
    private int code;
    private String apk;
    private String infor;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEdi_num() {
        return edi_num;
    }

    public void setEdi_num(String edi_num) {
        this.edi_num = edi_num;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getInfor() {
        return infor;
    }

    public void setInfor(String infor) {
        this.infor = infor;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
