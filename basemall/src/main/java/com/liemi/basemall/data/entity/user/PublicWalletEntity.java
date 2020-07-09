package com.liemi.basemall.data.entity.user;

public class PublicWalletEntity {
    private String address;
    private String qrcode;
    private String eth_remain;
    private String eth_cny;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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
}
