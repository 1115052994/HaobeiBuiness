package com.netmi.baselibrary.data.entity;

import android.content.Context;
import android.support.annotation.StringRes;

import com.bigkoo.pickerview.OptionsPickerView;
import com.netmi.baselibrary.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：选择器内容
 * 创建人：Sherlock
 * 创建时间：2019/3/6
 * 修改备注：
 */
public class SelectEntityBuilder {
    private SelectEntity data = null;

    private SelectEntityBuilder() {
    }

    public static SelectEntityBuilder initBuilder() {
        return new SelectEntityBuilder();
    }

    public <T> SelectEntityBuilder insert(String content, T type) {
        if (data == null)
            data = new SelectEntity<T>();
        if (content != null)
            data.insert(content, type);
        return this;
    }

    public SelectEntity build() {
        return data;
    }

    public static class SelectEntity<R> {
        //显示内容，通常为String类型
        private List<String> showList;
        //实际后台需要类型
        private List<R> realType;
        //PickView
        private OptionsPickerView pickerView;
        private int choosePoistion = 0;

        public OptionsPickerView initPickerView(@StringRes int title, Context context, SelectListenner<R> listenner) {
            if (pickerView == null)
                pickerView = new OptionsPickerView.Builder(context, (options1, option2, options3, v) -> {
                    choosePoistion = options1;
                    if (listenner != null) {
                        listenner.onSelect(getContent(choosePoistion), getType(choosePoistion), option2, options3);
                    }
                })
                        .setSubCalSize(14)
                        .setTitleText(ResourceUtil.getString(title))
                        .build();
            pickerView.setSelectOptions(choosePoistion);
            pickerView.setPicker(getShowList());
            return pickerView;
        }

        public OptionsPickerView getPickerView() {
            return pickerView;
        }


        private SelectEntity() {
            showList = new ArrayList<>();
            realType = new ArrayList<>();
        }

        public List<String> getShowList() {
            return showList;
        }

        private void setShowList(List<String> showList) {
            this.showList = showList;
        }

        public List<R> getRealType() {
            return realType;
        }

        public R getType(int p) {
            if (p < realType.size())
                return realType.get(p);
            else
                return null;
        }

        public String getContent(int p) {
            return showList.get(p);
        }

        private void setRealType(List<R> realType) {
            this.realType = realType;
        }

        public SelectEntity insert(String show, R real) {
            if (show != null) {
                showList.add(show);
                realType.add(real);
            }
            return this;
        }

        public SelectEntity delete(int poistion) {
            showList.remove(poistion);
            realType.remove(poistion);
            return this;
        }

        public int getPoistionByShowContent(String s) {
            return showList.indexOf(s);
        }

        public int getPoistionByRealType(R r) {
            return realType.indexOf(r);
        }

        public interface SelectListenner<T> {
            void onSelect(String content, T type, int option2, int option3);
        }

    }
}
