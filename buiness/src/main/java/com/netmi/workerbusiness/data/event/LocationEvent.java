package com.netmi.workerbusiness.data.event;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/12
 * 修改备注：
 */
public class LocationEvent {
    private String longitude;
    private String latitude;
    private String address;
    private String p_name;
    private String c_name;
    private String d_name;
    private String p_id;
    private String c_id;
    private String d_id;

    public LocationEvent(String longitude, String latitude, String address, String p_name, String c_name, String d_name, String p_id, String c_id, String d_id) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.p_name = p_name;
        this.c_name = c_name;
        this.d_name = d_name;
        this.p_id = p_id;
        this.c_id = c_id;
        this.d_id = d_id;
    }


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
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
}
