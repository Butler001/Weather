package com.example.weather_butler.contract;

import android.content.Context;
import android.util.Log;

import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.NetCallBack;
import com.example.mvplibrary.net.ServiceGenerator;
import com.example.weather_butler.api.ApiService;
import com.example.weather_butler.bean.LifeSuggestion;
import com.example.weather_butler.bean.TodayResponse;
import com.example.weather_butler.bean.WeatherForcastResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 天气订阅器，处理API返回数据
 */
public class WeatherContract {

    public static class WeatherPresenter extends BasePresenter<WeatherPresenter.IWeatherView> {
        private static final String TAG = "LQL";

        /**
         * 获得今天的天气数据
         * @param context
         * @param location
         */
        public void todayWeather(final Context context, String  location) {
            ApiService service = ServiceGenerator.createService(ApiService.class);
            service.getTodayResponse(location).enqueue(new NetCallBack<TodayResponse>() {
                @Override
                public void onSuccess(Call<TodayResponse> call, Response<TodayResponse> response) {
                    if (getView() != null) {
                        getView().getTodayWeatherResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 获取预测天气
         * @param context
         * @param location
         */
        public void WeatherForecast(final Context context,String location){
            ApiService service = ServiceGenerator.createService(ApiService.class);
            service.getWeatherForecast(location).enqueue(new NetCallBack<WeatherForcastResponse>() {
                @Override
                public void onSuccess(Call<WeatherForcastResponse> call, Response<WeatherForcastResponse> response) {
                    if (getView() != null){
                        getView().getWeatherForecast(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

        public void lifeSuggestion(final Context context , String location){
            ApiService service = ServiceGenerator.createService(ApiService.class);
            service.getSuggestion(location).enqueue(new NetCallBack<LifeSuggestion>() {
                @Override
                public void onSuccess(Call<LifeSuggestion> call, Response<LifeSuggestion> response) {
                    if (getView() != null){
                        getView().getLifeSuggestion(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

    public interface IWeatherView extends BaseView{
        //实例化实体类
        void getTodayWeatherResult(Response<TodayResponse> response);
        //获得预报天气
        void getWeatherForecast(Response<WeatherForcastResponse> response);
        //获取生活建议
        void getLifeSuggestion(Response<LifeSuggestion> response);
        //错误返回
        void getDataFailed();
    }
}

}
