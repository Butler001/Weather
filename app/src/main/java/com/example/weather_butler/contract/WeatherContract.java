package com.example.weather_butler.contract;

import android.content.Context;
import android.util.Log;

import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;
import com.example.mvplibrary.net.NetCallBack;
import com.example.mvplibrary.net.ServiceGenerator;
import com.example.weather_butler.api.ApiService;
import com.example.weather_butler.bean.AirConditionResponse;
import com.example.weather_butler.bean.BingImageResponse;
import com.example.weather_butler.bean.HourlyResponse;
import com.example.weather_butler.bean.LifeSuggestion;
import com.example.weather_butler.bean.LocationInfo;
import com.example.weather_butler.bean.TodayResponse;
import com.example.weather_butler.bean.WeatherForcastResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 天气订阅器，处理API返回数据
 */
public class WeatherContract {

    private static final String TAG = "LQL";

    /**
     * 展示数据的订阅器类
     */
    public static class WeatherPresenter extends BasePresenter<WeatherPresenter.IWeatherView>  {
        private static final String TAG = "LQL";

        /**
         * 获得今天的天气数据
         * @param context
         * @param location
         */
        public void todayWeather(final Context context, String  location) {
            ApiService service = ServiceGenerator.createService(ApiService.class,0);
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
            ApiService service = ServiceGenerator.createService(ApiService.class,0);
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

        /**
         * 获得生活建议
         * @param context
         * @param location
         */
        public void lifeSuggestion(final Context context , String location){
            ApiService service = ServiceGenerator.createService(ApiService.class,0);
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

        /**
         * 获取逐小时天气
         * @param context
         * @param location
         */
        public void hourlyWeather(final Context context , String location){
            ApiService service = ServiceGenerator.createService(ApiService.class,0);
            service.getHourly(location).enqueue(new NetCallBack<HourlyResponse>() {
                @Override
                public void onSuccess(Call<HourlyResponse> call, Response<HourlyResponse> response) {
                    if (getView() != null){
                        getView().getHourlyWeather(response);
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

        public void airCondition(final Context context,String city){
            ApiService service = ServiceGenerator.createService(ApiService.class,0);
            service.getAirCond(city).enqueue(new NetCallBack<AirConditionResponse>() {
                @Override
                public void onSuccess(Call<AirConditionResponse> call, Response<AirConditionResponse> response) {
                    if (getView() != null){
                        getView().getAirCond(response);
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



        /**
         * 获取必应每日一图
         * @param context
         */
        public void bing(final Context context){
            ApiService service = ServiceGenerator.createService(ApiService.class,1);
            service.bing().enqueue(new NetCallBack<BingImageResponse>() {
                @Override
                public void onSuccess(Call<BingImageResponse> call, Response<BingImageResponse> response) {
                    if (getView() != null){
                        getView().getBingResult(response);
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
        //获取必应每日一图
        void getBingResult(Response<BingImageResponse> response);
        //获取逐小时天气
        void getHourlyWeather(Response<HourlyResponse> response);
        //获得当前地址天气状况
        void getAirCond(Response<AirConditionResponse> response);
    }
}

}
