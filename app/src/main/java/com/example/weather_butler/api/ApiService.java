package com.example.weather_butler.api;

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
    @GET("lookup?&key=7724e0d43ee447109f46928e8b08667e")
    Call<LocationInfo> getCityId(@Query("location") String location);

    /**
     * 获取7天预测数据
     * @param location
     * @return
     */
    @GET("/v7/weather/7d?key=7724e0d43ee447109f46928e8b08667e")
    Call<WeatherForcastResponse> getWeatherForecast(@Query("location") String location);

    @GET("/v7/indices/1d?type=0&key=7724e0d43ee447109f46928e8b08667e")
    Call<LifeSuggestion> getSuggestion(@Query("location") String location);
}
