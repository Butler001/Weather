package com.example.weather_butler.adapter;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.weather_butler.R;
import com.example.weather_butler.bean.HourlyResponse;
import com.example.weather_butler.utils.WeatherUtils;

import java.util.List;

public class WeatherHourlyAdapter extends BaseQuickAdapter<HourlyResponse.Hourly, BaseViewHolder> {

    public WeatherHourlyAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<HourlyResponse.Hourly> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HourlyResponse.Hourly item) {
        //更改时间格式
        String timeResponse = item.getFxTime();
        String time = timeResponse.substring(11,16);

        if (time.charAt(0) == '0'){
            String time_r = time.substring(1);

            helper.setText(R.id.tv_time, WeatherUtils.showTimeInfo(time_r)+time_r)
                    .setText(R.id.tv_temp_hourly,item.getTemp()+"℃");
        }else {
            helper.setText(R.id.tv_time, WeatherUtils.showTimeInfo(time)+time)
                    .setText(R.id.tv_temp_hourly,item.getTemp()+"℃");
        }



        ImageView icon = helper.getView(R.id.iv_weather_state_hourly);
        String code = item.getIcon();
        WeatherUtils.changeIcon(icon,code);
    }
}
