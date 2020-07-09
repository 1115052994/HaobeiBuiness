package com.liemi.basemall.data.entity.user;

import java.io.Serializable;

/**
 * 区块链币种数据
 */
public class CoinEntity implements Serializable {
    private String coinName;//名称
    private int coinIcon;//图片
    private String usedNum;//可用数量
    private String usedCny;//转换为cny的值

    public CoinEntity(){

    }

    public CoinEntity(String name,int icon,String usedNum,String usedCny){
        this.coinName = name;
        this.coinIcon = icon;
        this.usedNum = usedNum;
        this.usedCny = usedCny;
    }


    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getCoinIcon() {
        return coinIcon;
    }

    public void setCoinIcon(int coinIcon) {
        this.coinIcon = coinIcon;
    }

    public String getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(String usedNum) {
        this.usedNum = usedNum;
    }

    public String getUsedCny() {
        return usedCny;
    }

    public void setUsedCny(String usedCny) {
        this.usedCny = usedCny;
    }
}
