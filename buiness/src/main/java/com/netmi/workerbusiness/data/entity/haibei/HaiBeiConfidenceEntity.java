package com.netmi.workerbusiness.data.entity.haibei;

import android.text.TextUtils;

import com.netmi.baselibrary.data.entity.BaseEntity;
import com.netmi.baselibrary.utils.FloatUtils;

import java.util.List;

public class HaiBeiConfidenceEntity extends BaseEntity {
    /*
    *  "all_balance": "10769134.00",            //海贝流通总数
    "confidence": "0.24",            //当前 权益指数
    "synthesize": "100.00",            //当前 海贝指数
    "max_confidence": "159.97",            //历史最高权益指数
    "min_confidence": "0.00",            //历史最低权益指数
    "max_synthesize": "259.97",            //历史最高海贝指数
    "min_synthesize": "100.00"            //历史最低海贝指数
    * */
    private List<HaiEntity> list;
    private String all_balance;
    private String confidence;
    private String synthesize;//当前
    private String max_confidence;
    private String min_confidence;
    private String max_synthesize;
    private String min_synthesize;
    private String synthesize_yesterday; //昨日
    private String synthesize_match; //相较昨日 （根据正负号判断，*100得到百分比）

    public String getSynthesize_yesterday() {
        return synthesize_yesterday;
    }

    public void setSynthesize_yesterday(String synthesize_yesterday) {
        this.synthesize_yesterday = synthesize_yesterday;
    }

    public String getSynthesize_match() {
        if (TextUtils.isEmpty(synthesize_match)) {
            return 0.00 + "%";
        }
        if (synthesize_match.contains("-")) { //负数
            double v = Math.abs(Double.valueOf(synthesize_match)) * 100;
            return "-" + v + "%";
        } else {
            double v = Math.abs(Double.valueOf(synthesize_match)) * 100;
            return "+" + FloatUtils.formatDouble(v) + "%";
        }
    }

    public void setSynthesize_match(String synthesize_match) {
        this.synthesize_match = synthesize_match;
    }

    public List<HaiEntity> getList() {
        return list;
    }

    public void setList(List<HaiEntity> list) {
        this.list = list;
    }

    public String getAll_balance() {
        return all_balance;
    }

    public void setAll_balance(String all_balance) {
        this.all_balance = all_balance;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getSynthesize() {
        return synthesize;
    }

    public void setSynthesize(String synthesize) {
        this.synthesize = synthesize;
    }

    public String getMax_confidence() {
        return max_confidence;
    }

    public void setMax_confidence(String max_confidence) {
        this.max_confidence = max_confidence;
    }

    public String getMin_confidence() {
        return min_confidence;
    }

    public void setMin_confidence(String min_confidence) {
        this.min_confidence = min_confidence;
    }

    public String getMax_synthesize() {
        return max_synthesize;
    }

    public void setMax_synthesize(String max_synthesize) {
        this.max_synthesize = max_synthesize;
    }

    public String getMin_synthesize() {
        return min_synthesize;
    }

    public void setMin_synthesize(String min_synthesize) {
        this.min_synthesize = min_synthesize;
    }
}
