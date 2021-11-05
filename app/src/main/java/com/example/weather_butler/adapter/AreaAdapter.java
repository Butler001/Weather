package com.example.weather_butler.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.weather_butler.R;
import com.example.weather_butler.bean.CityResponse;

import java.util.List;

/**
 * 县区级数据适配器
 */
public class AreaAdapter extends BaseQuickAdapter<CityResponse.CityBean.AreaBean, BaseViewHolder> {
    public AreaAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CityResponse.CityBean.AreaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityResponse.CityBean.AreaBean item) {
        helper.setText(R.id.tv_city,item.getName());
        helper.addOnClickListener(R.id.item_city);//点击之后得到区县 ， 然后查询天气数据
    }
}
