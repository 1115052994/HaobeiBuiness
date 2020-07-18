package com.netmi.workerbusiness.data.entity.home;

/**
 * 类描述：
 * 创建人：Leo
 * 创建时间：2020/3/20
 * 修改备注：
 */
public class HaibeiConfidenceEntity {
    /**
     * confidence : 884223.02
     * time : 2020-03-18
     * synthesize : 8843.2302
     */

    private String confidence;
    private String time;
    private String synthesize;
    private String one;
    private String two;

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

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    @Override
    public String toString() {
        return "HaibeiConfidenceEntity{" +
                "confidence=" + confidence +
                ", time='" + time + '\'' +
                ", synthesize=" + synthesize +
                ", one='" + one + '\'' +
                ", two='" + two + '\'' +
                '}';
    }
}
