package com.liemi.basemall.data.entity.floor;

import com.netmi.baselibrary.data.entity.PageEntity;
import com.netmi.baselibrary.utils.Strings;

import java.util.List;

/**
 * Created by Bingo on 2018/11/19.
 */

public class FloorPageEntity<T> extends PageEntity<T>{


    /**
     * content : {"bg_img":""}
     */

    private ContentBean<T> content;

    public ContentBean<T> getContent() {
        return content;
    }

    public void setContent(ContentBean<T> content) {
        this.content = content;
    }

    @Override
    public List<T> getList() {
        return Strings.isEmpty(super.getList()) ? content.getList() : super.getList();
    }

    public static class ContentBean<T> {
        /**
         * bg_img :
         */

        private String bg_img;
        /**
         * 数据
         */
        public List<T> list;

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }

        public String getBg_img() {
            return bg_img;
        }

        public void setBg_img(String bg_img) {
            this.bg_img = bg_img;
        }
    }
}
