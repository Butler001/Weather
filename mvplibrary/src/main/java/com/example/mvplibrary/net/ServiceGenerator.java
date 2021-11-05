package com.example.mvplibrary.net;

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
    public static String BASE_URL = "https://devapi.qweather.com";

    //创建服务 参数就是API服务
    public static <T> T createService(Class<T> serviceClass){

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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();

        return retrofit.create(serviceClass);
    }
}
