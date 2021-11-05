package com.example.weather_butler.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.weather_butler.R;
import com.example.weather_butler.bean.CityResponse;

import java.util.List;

/**
 * 省级数据适配器
 */
public class ProvinceAdapter extends BaseQuickAdapter<CityResponse , BaseViewHolder> {

    public ProvinceAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CityResponse> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityResponse item) {
        helper.setText(R.id.tv_city,item.getName());//省级列表
        helper.addOnClickListener(R.id.item_city);//点击后进入市级列表
    }
}
