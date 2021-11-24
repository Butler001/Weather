package com.example.mvplibrary.net;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 服务构建器 用于访问API
 */
public class ServiceGenerator {
    //将API中固定的部分拆分出来，方便之后调用
    public static String BASE_URL = null;

    /**
     * 设置接口类型
     * @param type
     * @return
     */
    private static String urlType(int type){
        switch (type){
            case 0://和风天气
                BASE_URL = "https://devapi.qweather.com";
                break;
            case 1://必应每日一图
                BASE_URL = "https://cn.bing.com";
                break;
            case 2:
                BASE_URL = "https://geoapi.qweather.com/v2/city/";
                break;
        }
        return BASE_URL;
    }

    //创建服务 参数就是API服务
    public static <T> T createService(Class<T> serviceClass, int type ){

        //创建 OkHttpClientBuilder 构造器对象
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        //设置请求超时的时间
        okHttpClientBuilder.connectTimeout(30000, TimeUnit.SECONDS);

        //消息拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        //设置日志打印的级别 NONE BASIC HEADER BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //添加消息拦截器
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);

        //在Retrofit中设置OKHttpClient
        Retrofit retrofit = new Retrofit.Builder().baseUrl(urlType(type))
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();

        return retrofit.create(serviceClass);
    }
}
