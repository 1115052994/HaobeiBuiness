package com.netmi.workerbusiness.data.entity.home.linecommodity.createcommofity;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateGoodCommand implements Serializable {
    /**
     * token	是	string	登录凭证
     * title	是	string	商品标题
     * price	是	float	商品现价
     * old_price	是	float	商品原价
     * deal_num_false	是	int	虚拟销量
     * multi_urls	是	array	商品图片
     * cate_list	是	array	商品分类 见类目获取接口
     * sort	是	int	商品排序
     * template_id	是	int	运费模板ID
     * label	是	string	标签
     * nature_list	是	array	服务描述
     * item_value_list	是	array	规格组 需要自行组合
     * status	是	int	存入已下架 默认为7
     */
    //修改商品信息时  需要商品id
    private String item_id;
    private String title;
    private String price;
    private String old_price;
    private String deal_num_false;
    private List<String> multi_urls = new ArrayList<>();
    private List<String> cate_list = new ArrayList<>();
    private String sort;
    private String template_id;
    private String label;
    private List<String> nature_list = new ArrayList<>();
    private List<SpecBean> item_value_list = new ArrayList<>();
    private int status;


    //以下为修改商品信息时返回的参数
    private List<String> itemImgs = new ArrayList<>();  //图片
    private List<CategoryItemsBean> meCategoryItems = new ArrayList<>();  //分类
    private String categoryStr;  //分类名称
    private TemplateBean template_list;  //分类名称
    private String meLabel;  //标签
    private List<SpecBean> meItemValues = new ArrayList<>();  //规格
    private List<MeNaturesBean> meNatures = new ArrayList<>();  //服务描述


    private List<SpecBean> spec = new ArrayList<>();
    private String remark;
    private List<String> label_list = new ArrayList<>();
    private String rich_text;
    private List<String> group = new ArrayList<>();
    private String cate_list_name;
    private String group_name;
    private String postage_name;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCate_list_name() {
        return cate_list_name;
    }

    public void setCate_list_name(String cate_list_name) {
        this.cate_list_name = cate_list_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getPostage_name() {
        return postage_name;
    }

    public void setPostage_name(String postage_name) {
        this.postage_name = postage_name;
    }

    public List<String> getMulti_urls() {
        return multi_urls;
    }

    public void setMulti_urls(List<String> multi_urls) {
        this.multi_urls = multi_urls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public List<String> getCate_list() {
        if (meCategoryItems.size() > 0) {
            for (int i = 0; i < meCategoryItems.size(); i++) {
                cate_list.add(meCategoryItems.get(i).getMcid());
            }
        }
        return cate_list;
    }

    public void setCate_list(List<String> cate_list) {
        this.cate_list = cate_list;
    }

    public List<String> getLabel_list() {
        return label_list;
    }

    public void setLabel_list(List<String> label_list) {
        this.label_list = label_list;
    }

    public String getRich_text() {
        return rich_text;
    }

    public void setRich_text(String rich_text) {
        this.rich_text = rich_text;
    }

    public List<String> getGroup() {
        return group;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public List<SpecBean> getSpec() {
        return spec;
    }

    public void setSpec(List<SpecBean> spec) {
        this.spec = spec;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int upper_shelf) {
        this.status = upper_shelf;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOld_price() {
        return old_price;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public String getDeal_num_false() {
        return deal_num_false;
    }

    public void setDeal_num_false(String deal_num_false) {
        this.deal_num_false = deal_num_false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getNature_list() {
        if (meNatures.size() > 0) {
            for (int i = 0; i < meNatures.size(); i++) {
                nature_list.add(meNatures.get(i).getId());
            }
        }
        return nature_list;
    }

    public void setNature_list(List<String> nature_list) {
        this.nature_list = nature_list;
    }

    public List<SpecBean> getItem_value_list() {
        if (meItemValues.size() > 0) {
            item_value_list = meItemValues;
        }
        return item_value_list;
    }

    public void setItem_value_list(List<SpecBean> item_value_list) {
        this.meItemValues = item_value_list;
        this.item_value_list = item_value_list;
    }

    public List<String> getItemImgs() {
        return itemImgs;
    }

    public void setItemImgs(List<String> itemImgs) {
        this.itemImgs = itemImgs;
    }

    public List<CategoryItemsBean> getMeCategoryItems() {
        return meCategoryItems;
    }

    public void setMeCategoryItems(List<CategoryItemsBean> meCategoryItems) {
        this.meCategoryItems = meCategoryItems;
    }

    public String getCategoryStr() {
        if (meCategoryItems.size() > 0) {
            for (int i = 0; i < meCategoryItems.size(); i++) {
                if (categoryStr == null) {
                    categoryStr = meCategoryItems.get(i).getCate_name();
                } else {
                    categoryStr = categoryStr + "、" + meCategoryItems.get(i).getCate_name();
                }
            }
        }
        return categoryStr;
    }

    public void setCategoryStr(String categoryStr) {
        this.categoryStr = categoryStr;
    }

    public TemplateBean getTemplate_list() {
        return template_list;
    }

    public void setTemplate_list(TemplateBean template_list) {
        this.template_list = template_list;
    }

    public String getMeLabel() {
        return meLabel;
    }

    public void setMeLabel(String meLabel) {
        this.meLabel = meLabel;
    }

    public List<SpecBean> getMeItemValues() {
        return meItemValues;
    }

    public void setMeItemValues(List<SpecBean> meItemValues) {
        this.meItemValues = meItemValues;
    }

    public List<MeNaturesBean> getMeNatures() {
        return meNatures;
    }

    public void setMeNatures(List<MeNaturesBean> meNatures) {
        this.meNatures = meNatures;
    }


    public static class SpecBean implements Serializable {
        private String value_names;
        private String value_ids;
        private String stock;
        private String price;
        private String discount;


        public String getValue_names() {
            return value_names;
        }

        public void setValue_names(String value_names) {
            this.value_names = value_names;
        }

        public String getValue_ids() {
            return value_ids;
        }

        public void setValue_ids(String value_ids) {
            this.value_ids = value_ids;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        @Override
        public String toString() {
            return "SpecBean{" +
                    "value_names='" + value_names + '\'' +
                    ", value_ids='" + value_ids + '\'' +
                    ", price='" + price + '\'' +
                    ", stock='" + stock + '\'' +
                    ", discount='" + discount + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CreateGoodCommand{" +
                "multi_urls=" + multi_urls +
                ", title='" + title + '\'' +
                ", remark='" + remark + '\'' +
                ", sort='" + sort + '\'' +
                ", template_id='" + template_id + '\'' +
                ", cate_list=" + cate_list +
                ", label_list=" + label_list +
                ", rich_text='" + rich_text + '\'' +
                ", group=" + group +
                ", spec=" + spec +
                ", upper_shelf='" + status + '\'' +
                ", cate_list_name='" + cate_list_name + '\'' +
                ", group_name='" + group_name + '\'' +
                ", postage_name='" + postage_name + '\'' +
                '}';
    }

    private class CategoryItemsBean implements Serializable {

        /**
         * id : 7966
         * item_id : 1639
         * mcid : 257
         * cate_name : 电脑配件
         */

        private String id;
        private String item_id;
        private String mcid;
        private String cate_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getMcid() {
            return mcid;
        }

        public void setMcid(String mcid) {
            this.mcid = mcid;
        }

        public String getCate_name() {
            return cate_name;
        }

        public void setCate_name(String cate_name) {
            this.cate_name = cate_name;
        }
    }

    public static class TemplateBean implements Serializable {

        /**
         * template_id : 59
         * template_name : 本机
         */

        private String template_id;
        private String template_name;

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public String getTemplate_name() {
            return template_name;
        }

        public void setTemplate_name(String template_name) {
            this.template_name = template_name;
        }
    }

    public static class MeNaturesBean implements Serializable {

        /**
         * id : 72
         * name : 延保服务
         * remark : 在购买商品时选择购买“延保服务”后，在服务保障期内如消费者购买的商品发生故障（电器因生产制造商的材料、工艺、零部件质量问题引起的、影响正常使用功能的故障），符合服务申请条件的，由消费者发起申请，在消费者的服务保障额度范围内为消费者提供维修的保障服务项目。
         * item_id : null
         * icon : http://liemimofang.oss-cn-hangzhou.aliyuncs.com/__liemi__/image/png/ABWXZMNHK1345678_1547271194.png
         */

        private String id;
        private String name;
        private String remark;
        private Object item_id;
        private String icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Object getItem_id() {
            return item_id;
        }

        public void setItem_id(Object item_id) {
            this.item_id = item_id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
