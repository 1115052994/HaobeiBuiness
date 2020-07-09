package com.netmi.workerbusiness.data.entity.home.lineorder;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2019/7/11
 * 修改备注：
 */
public class StoreExpressEntity {

    private String express_id;
    private String name;
    private String code;
    private String logistics_company_code;

    public String getExpress_id() {
        return express_id;
    }

    public void setExpress_id(String express_id) {
        this.express_id = express_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getLogistics_company_code() {
        return logistics_company_code;
    }

    public void setLogistics_company_code(String logistics_company_code) {
        this.logistics_company_code = logistics_company_code;
    }
}
