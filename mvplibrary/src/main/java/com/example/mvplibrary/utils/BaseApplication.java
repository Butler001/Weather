package com.example.mvplibrary.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

/**
 * 用作全局管理
 */
public class BaseApplication extends Application {
    private static ActivityManager activityManager;
    private static BaseApplication baseApplication;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        //声明ActivityManager
        activityManager = new ActivityManager();
        context = getApplicationContext();
        baseApplication = this;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    public static BaseApplication getBaseApplication() {
        return baseApplication;
    }

    public static Context getContext() {
        return context;
    }
}
