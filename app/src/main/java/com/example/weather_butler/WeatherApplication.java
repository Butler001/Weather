package com.example.weather_butler;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mvplibrary.utils.ActivityManager;
import com.example.mvplibrary.utils.BaseApplication;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Handler;

public class WeatherApplication extends BaseApplication {
    /**
     * 创建实例
     */
    public static WeatherApplication weatherApplication;
    private static Context context;
    private static ActivityManager manager;

    private static Activity myActivity;

    public static Context getMyContext(){
        return weatherApplication == null ? null : weatherApplication.getApplicationContext();
    }

    private Handler myHandler;

    public Handler getMyHandler() {
        return myHandler;
    }

    public void setMyHandler(Handler myHandler) {
        this.myHandler = myHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        manager = new ActivityManager();
        context = getApplicationContext();
        weatherApplication = this;

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                myActivity = activity;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    public static ActivityManager getManager() {
        return manager;
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //static 可以防止内存泄漏
    static {
        //设置全局的header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @NotNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull @NotNull Context context, @NonNull @NotNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.gray,R.color.black);
                return new ClassicsHeader(context);
            }
        });
        //设置全局的footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @NotNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull @NotNull Context context, @NonNull @NotNull RefreshLayout layout) {
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
}
