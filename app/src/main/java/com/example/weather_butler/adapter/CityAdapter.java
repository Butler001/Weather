package com.example.weather_butler.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.weather_butler.R;
import com.example.weather_butler.bean.CityResponse;

import java.util.List;

/**
 * 市级数据适配器
 */
public class CityAdapter extends BaseQuickAdapter<CityResponse.CityBean, BaseViewHolder> {
    public CityAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CityResponse.CityBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityResponse.CityBean item) {
        helper.setText(R.id.tv_city,item.getName());
        helper.addOnClickListener(R.id.item_city);
    }
}
