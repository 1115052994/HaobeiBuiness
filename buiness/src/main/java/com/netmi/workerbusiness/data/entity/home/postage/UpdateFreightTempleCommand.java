package com.netmi.workerbusiness.data.entity.home.postage;

import java.io.Serializable;
import java.util.List;

public class UpdateFreightTempleCommand implements Serializable {

    private String template_name;
    private String t_id;
    private List<PostageDetailEntity.RegionConfBean> region_conf;

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getT_id() {
        return t_id;
    }

    public void setT_id(String id) {
        this.t_id = id;
    }

    public List<PostageDetailEntity.RegionConfBean> getRegion_conf() {
        return region_conf;
    }

    public void setRegion_conf(List<PostageDetailEntity.RegionConfBean> region_conf) {
        this.region_conf = region_conf;
    }

    @Override
    public String toString() {
        return "UpdateFreightTempleCommand{" +
                "template_name='" + template_name + '\'' +
                ", id='" + t_id + '\'' +
                ", region_conf=" + region_conf +
                '}';
    }
}
