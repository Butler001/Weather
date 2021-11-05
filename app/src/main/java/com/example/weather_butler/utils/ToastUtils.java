package com.example.weather_butler.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 消息提示工具类
 */

public class ToastUtils {
    //long text
    public static void showLongToast(Context context , CharSequence text){
        Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
    //short text
    public static void showShortToast(Context context ,CharSequence text){
        Toast.makeText(context.getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

}
