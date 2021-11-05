package com.example.weather_butler.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.weather_butler.R;
import com.example.weather_butler.bean.WeatherForcastResponse;

import java.util.List;

/**
 * 天气列表适配器
 */
public class WeatherForecastAdapter extends BaseQuickAdapter<WeatherForcastResponse.Daily, BaseViewHolder> {
    public WeatherForecastAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<WeatherForcastResponse.Daily> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeatherForcastResponse.Daily item) {
        helper.setText(R.id.forecast_date,item.getFxDate())
                .setText(R.id.forecast_description,item.getTextDay())
                .setText(R.id.tv_max_min,item.getTempMax()+"/"+item.getTempMin()+"℃");
    }

}
