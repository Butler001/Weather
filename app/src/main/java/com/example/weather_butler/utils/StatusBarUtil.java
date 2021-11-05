package com.example.weather_butler.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 状态栏工具类
 */
public class StatusBarUtil {
    /**
     * 修改状态栏为全透明
     * @param activity
     */
    public static void transparencyBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 修改状态栏颜色
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity ,int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = activity.getWindow();
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //使用SystemBarTint库支持4.4版本以上状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity);
            SystemBarTintManager manager = new SystemBarTintManager(activity);
            manager.setStatusBarTintEnabled(true);
            manager.setStatusBarTintResource(colorId);
        }
    }

    /**
     * 判断系统类型，使用不同的方法来适配屏幕
     * @param activity
     * @return
     */
    public static int StatusBarLightMode(Activity activity){
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if (MIUISetStatusBarLightMode(activity ,true)){
                result = 1;
            }else if (FlymeSetStatusBarLightMode(activity.getWindow(),true)){
                result = 2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                result = 3;
            }
        }
        return result;
    }

    private static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null){
            try{
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlag = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlag.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlag.getInt(lp);
                if (dark){
                    value |= bit;
                }else {
                    value &= ~bit;
                }
                meizuFlag.setInt(lp,value);
                window.setAttributes(lp);
                result = true;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏亮色模式 ， 设置状态栏黑色文字 图标
     * 适配MIUI Flyme 和6.0以上其他版本Android
     * @param activity
     * @param type
     */
    public static void StatusBarLightMode(Activity activity ,int type){
        if (type == 1){
            MIUISetStatusBarLightMode(activity,true);
        }else if (type == 2){
            FlymeSetStatusBarLightMode(activity.getWindow(),true);
        }else if (type == 3){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 清除亮色模式，开启暗色模式
     * @param activity
     * @param type
     */
    public static void StatusBarDarkMode(Activity activity , int type){
        if (type == 1){
            MIUISetStatusBarLightMode(activity,false);
        }else if (type == 2){
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        }else if (type == 3){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     *深色设置方式   MIUI
     * @param activity
     * @param dark  是否把 状态栏 文字 图标设置为深色
     * @return boolean
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity ,boolean dark){
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null){
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.WindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("getExtraFlags",int.class,int.class);
                if (dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);
                }else {
                    extraFlagField.invoke(window,0,darkModeFlag);
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (dark){
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
