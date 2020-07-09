package com.netmi.workerbusiness.data.entity.home.postage;

import java.io.Serializable;
import java.util.List;

public class AddFreightTempleCommand implements Serializable {

    private String template_name;
    private List<PostageTempleItemEntity> region_conf;

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public List<PostageTempleItemEntity> getRegion_conf() {
        return region_conf;
    }

    public void setRegion_conf(List<PostageTempleItemEntity> region_conf) {
        this.region_conf = region_conf;
    }

    @Override
    public String toString() {
        return "AddFreightTempleCommand{" +
                "template_name='" + template_name + '\'' +
                ", region_conf=" + region_conf +
                '}';
    }
}
