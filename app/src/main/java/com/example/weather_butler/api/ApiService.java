package com.example.weather_butler.api;

import com.example.weather_butler.bean.AirConditionResponse;
import com.example.weather_butler.bean.BingImageResponse;
import com.example.weather_butler.bean.HourlyResponse;
import com.example.weather_butler.bean.LifeSuggestion;
import com.example.weather_butler.bean.LocationInfo;
import com.example.weather_butler.bean.TodayResponse;
import com.example.weather_butler.bean.WeatherForcastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *   api服务接口
 */
public interface ApiService {
    /**
     * 封装URL到GET注解
     * @param location
     */
    @GET("/v7/weather/now?key=7724e0d43ee447109f46928e8b08667e")
    Call<TodayResponse> getTodayResponse(@Query("location") String location);

    /**
     * 获得地点代号
     * @param location
     * @return
     */
    @GET("/v2/city/lookup?key=7724e0d43ee447109f46928e8b08667e")
    Call<LocationInfo> getCityId(@Query("location") String location);

    /**
     * 获取7天预测数据
     * @param location
     * @return
     */
    @GET("/v7/weather/7d?key=7724e0d43ee447109f46928e8b08667e")
    Call<WeatherForcastResponse> getWeatherForecast(@Query("location") String location);

    /**
     * 生活建议
     * @param location
     * @return
     */
    @GET("/v7/indices/1d?type=0&key=7724e0d43ee447109f46928e8b08667e")
    Call<LifeSuggestion> getSuggestion(@Query("location") String location);

    /**
     * 获取必应每日一图
     * @return
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Call<BingImageResponse> bing();

    /**
     * 获得逐小时天气
     * @param location
     * @return
     */
    @GET("/v7/weather/24h?key=7724e0d43ee447109f46928e8b08667e")
    Call<HourlyResponse> getHourly(@Query("location") String location);

    /**
     * 获得当前天气状况  要传入市级的定位，否则会报权限错误
     * @param city
     * @return
     */
    @GET("/v7/air/now?key=7724e0d43ee447109f46928e8b08667e")
    Call<AirConditionResponse> getAirCond(@Query("location") String city);
}
