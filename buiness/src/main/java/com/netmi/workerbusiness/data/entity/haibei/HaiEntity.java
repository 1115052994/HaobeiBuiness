package com.netmi.workerbusiness.data.entity.haibei;

import com.netmi.baselibrary.data.entity.BaseEntity;

public class HaiEntity extends BaseEntity {
    /*
    "confidence": "884223.02",//信心指数
            "time": "2020-03-18",//时间
            "synthesize": 8843.2302//综合指数
    * */

    private String confidence;
    private String time;
    private String synthesize;
    private String type;
    private String id;

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSynthesize() {
        return synthesize;
    }

    public void setSynthesize(String synthesize) {
        this.synthesize = synthesize;
    }
}
