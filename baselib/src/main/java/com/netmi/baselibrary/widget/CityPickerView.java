package com.netmi.baselibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.netmi.baselibrary.R;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.CityChoiceEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2018/2/28 9:54
 * 修改备注：
 */
public class CityPickerView {

    private Context context;

    private List<String> provinceList = new ArrayList<>();
    private List<List<String>> cityList = new ArrayList<>();
    private List<List<List<String>>> areaList = new ArrayList<>();
    private List<CityChoiceEntity> cityDatas;

    private int provinceIndex, cityIndex, areaIndex;

    public CityPickerView(Context context) {
        this.context = context;
    }

    public void loadCityData(BaseData<List<CityChoiceEntity>> data) {
        cityDatas = data.getData();

        //组装数据
        for (CityChoiceEntity province : cityDatas) {
            List<String> cList = new ArrayList<>();
            List<List<String>> areaOptions3 = new ArrayList<>();
            for (CityChoiceEntity.CListBean city : province.getC_list()) {
                cList.add(city.getName());
                List<String> dList = new ArrayList<>();
                for (CityChoiceEntity.CListBean.DListBean area : city.getD_list()) {
                    dList.add(area.getName());
                }
                areaOptions3.add(dList);
            }
            areaList.add(areaOptions3);
            cityList.add(cList);
            provinceList.add(province.getName());
        }
    }

    public void show(final OptionsPickerView.OnOptionsSelectListener listener) {
        OptionsPickerView cityOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                provinceIndex = options1;
                cityIndex = options2;
                areaIndex = options3;
                if (listener != null) {
                    listener.onOptionsSelect(options1, options2, options3, v);
                }
            }
        })
                .setSelectOptions(provinceIndex, cityIndex, areaIndex)
                .setTitleText("选择城市").build();
        cityOptions.setPicker(provinceList, cityList, areaList);
        cityOptions.show();
    }
    public CityChoiceEntity getChoiceProvince() {
        return cityDatas.get(provinceIndex);
    }

    public CityChoiceEntity.CListBean getChoiceCity() {
        return cityDatas.get(provinceIndex).getC_list().get(cityIndex);
    }

    public CityChoiceEntity.CListBean.DListBean getChoiceArea() {
        return cityDatas.get(provinceIndex).getC_list().get(cityIndex).getD_list().get(areaIndex);
    }

    public List<String> getProvinceList() {
        return provinceList;
    }

    public List<List<String>> getCityList() {
        return cityList;
    }

    public List<List<List<String>>> getAreaList() {
        return areaList;
    }
}
