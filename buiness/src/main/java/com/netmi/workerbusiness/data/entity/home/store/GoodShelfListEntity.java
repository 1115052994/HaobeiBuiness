package com.netmi.workerbusiness.data.entity.home.store;

import com.netmi.baselibrary.data.entity.BaseEntity;

import java.io.Serializable;

public class GoodShelfListEntity extends BaseEntity {

    private String item_id;
    private String img_url;
    private String title;
    private String price;
    private String stock;
    private String deal_num;
    private BonusBean bonus;
    private BonusTypeLevelBean Bonus_type_level;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDeal_num() {
        return deal_num;
    }

    public void setDeal_num(String deal_num) {
        this.deal_num = deal_num;
    }

    public BonusBean getBonus() {
        return bonus;
    }

    public void setBonus(BonusBean bonus) {
        this.bonus = bonus;
    }

    public BonusTypeLevelBean getBonus_type_level() {
        return Bonus_type_level;
    }

    public void setBonus_type_level(BonusTypeLevelBean bonus_type_level) {
        Bonus_type_level = bonus_type_level;
    }

    public static class BonusBean implements Serializable {

        private String type_2_level_0;
        private String type_2_level_1;
        private String type_2_level_2;
        private String type_2_level_3;
        private String type_3_level_0;
        private String type_3_level_1;
        private String type_3_level_2;
        private String type_3_level_3;

        public String getType_2_level_0() {
            return type_2_level_0;
        }

        public void setType_2_level_0(String type_2_level_0) {
            this.type_2_level_0 = type_2_level_0;
        }

        public String getType_2_level_1() {
            return type_2_level_1;
        }

        public void setType_2_level_1(String type_2_level_1) {
            this.type_2_level_1 = type_2_level_1;
        }

        public String getType_2_level_2() {
            return type_2_level_2;
        }

        public void setType_2_level_2(String type_2_level_2) {
            this.type_2_level_2 = type_2_level_2;
        }

        public String getType_2_level_3() {
            return type_2_level_3;
        }

        public void setType_2_level_3(String type_2_level_3) {
            this.type_2_level_3 = type_2_level_3;
        }

        public String getType_3_level_0() {
            return type_3_level_0;
        }

        public void setType_3_level_0(String type_3_level_0) {
            this.type_3_level_0 = type_3_level_0;
        }

        public String getType_3_level_1() {
            return type_3_level_1;
        }

        public void setType_3_level_1(String type_3_level_1) {
            this.type_3_level_1 = type_3_level_1;
        }

        public String getType_3_level_2() {
            return type_3_level_2;
        }

        public void setType_3_level_2(String type_3_level_2) {
            this.type_3_level_2 = type_3_level_2;
        }

        public String getType_3_level_3() {
            return type_3_level_3;
        }

        public void setType_3_level_3(String type_3_level_3) {
            this.type_3_level_3 = type_3_level_3;
        }
    }

    public static class BonusTypeLevelBean implements Serializable{
        private String type_2_level_0;
        private String type_2_level_1;
        private String type_2_level_2;
        private String type_2_level_3;
        private String type_3_level_0;
        private String type_3_level_1;
        private String type_3_level_2;
        private String type_3_level_3;

        public String getType_2_level_0() {
            return type_2_level_0;
        }

        public void setType_2_level_0(String type_2_level_0) {
            this.type_2_level_0 = type_2_level_0;
        }

        public String getType_2_level_1() {
            return type_2_level_1;
        }

        public void setType_2_level_1(String type_2_level_1) {
            this.type_2_level_1 = type_2_level_1;
        }

        public String getType_2_level_2() {
            return type_2_level_2;
        }

        public void setType_2_level_2(String type_2_level_2) {
            this.type_2_level_2 = type_2_level_2;
        }

        public String getType_2_level_3() {
            return type_2_level_3;
        }

        public void setType_2_level_3(String type_2_level_3) {
            this.type_2_level_3 = type_2_level_3;
        }

        public String getType_3_level_0() {
            return type_3_level_0;
        }

        public void setType_3_level_0(String type_3_level_0) {
            this.type_3_level_0 = type_3_level_0;
        }

        public String getType_3_level_1() {
            return type_3_level_1;
        }

        public void setType_3_level_1(String type_3_level_1) {
            this.type_3_level_1 = type_3_level_1;
        }

        public String getType_3_level_2() {
            return type_3_level_2;
        }

        public void setType_3_level_2(String type_3_level_2) {
            this.type_3_level_2 = type_3_level_2;
        }

        public String getType_3_level_3() {
            return type_3_level_3;
        }

        public void setType_3_level_3(String type_3_level_3) {
            this.type_3_level_3 = type_3_level_3;
        }
    }

}
