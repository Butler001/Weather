package com.example.weather_butler.utils;

import android.widget.ImageView;

import com.example.weather_butler.R;

/**
 * 天气工具类
 */
public class WeatherUtils {

    /**
     * 根据传入的代码修改天气图标
     * @param icon
     * @param code
     */
    public static void changeIcon(ImageView icon , String code){
        switch (code){
            case "100"://晴
               icon.setBackgroundResource(R.mipmap.icon_100);
               break;
            case "101"://多云
                icon.setBackgroundResource(R.mipmap.icon_101);
                break;
            case "102"://少云
                icon.setBackgroundResource(R.mipmap.icon_102);
                break;
            case "103"://晴转多云
                icon.setBackgroundResource(R.mipmap.icon_103);
                break;
            case "200"://有风
                icon.setBackgroundResource(R.mipmap.icon_2001);
                break;
            case "104"://阴
                icon.setBackgroundResource(R.mipmap.icon_104);
                break;
            case "300"://阵雨
                icon.setBackgroundResource(R.mipmap.icon_300);
                break;
            case "301"://强阵雨
                icon.setBackgroundResource(R.mipmap.icon_301);
                break;
            case "302"://雷阵雨
                icon.setBackgroundResource(R.mipmap.icon_302);
                break;
            case "303"://强雷阵雨
                icon.setBackgroundResource(R.mipmap.icon_303);
                break;
            case "304"://雷阵雨伴有冰雹
                icon.setBackgroundResource(R.mipmap.icon_304);
                break;
            case "305"://小雨
                icon.setBackgroundResource(R.mipmap.icon_305);
                break;
            case "306"://中雨
                icon.setBackgroundResource(R.mipmap.icon_306);
                break;
            case "307"://大雨
                icon.setBackgroundResource(R.mipmap.icon_307);
                break;
            case "308"://极端降雨
                icon.setBackgroundResource(R.mipmap.icon_308);
                break;
            case "309"://毛毛雨
                icon.setBackgroundResource(R.mipmap.icon_309);
                break;
            case "310"://暴雨
                icon.setBackgroundResource(R.mipmap.icon_310);
                break;
            case "311"://大暴雨
                icon.setBackgroundResource(R.mipmap.icon_311);
                break;
            case "312"://特大暴雨
                icon.setBackgroundResource(R.mipmap.icon_312);
                break;
            case "313"://冻雨
                icon.setBackgroundResource(R.mipmap.icon_313);
                break;
            case "314"://小到中雨
                icon.setBackgroundResource(R.mipmap.icon_314);
                break;
            case "315"://中到大雨
                icon.setBackgroundResource(R.mipmap.icon_315);
                break;
            case "316"://大到暴雨
                icon.setBackgroundResource(R.mipmap.icon_316);
                break;
            case "317"://暴雨到大暴雨
                icon.setBackgroundResource(R.mipmap.icon_317);
                break;
            case "318"://大暴雨到特大暴雨
                icon.setBackgroundResource(R.mipmap.icon_318);
                break;
            case "399"://雨
                icon.setBackgroundResource(R.mipmap.icon_399);
                break;
            case "400"://小雪
                icon.setBackgroundResource(R.mipmap.icon_400);
                break;
            case "401"://中雪
                icon.setBackgroundResource(R.mipmap.icon_401);
                break;
            case "402"://大雪
                icon.setBackgroundResource(R.mipmap.icon_402);
                break;
            case "403"://暴雪
                icon.setBackgroundResource(R.mipmap.icon_403);
                break;
            case "404"://雨夹雪
                icon.setBackgroundResource(R.mipmap.icon_404);
                break;
            case "405"://雨雪天气
                icon.setBackgroundResource(R.mipmap.icon_405);
                break;
            case "406"://阵雨夹雪
                icon.setBackgroundResource(R.mipmap.icon_406);
                break;
            case "407"://阵雪
                icon.setBackgroundResource(R.mipmap.icon_407);
                break;
            case "408"://小到中雪
                icon.setBackgroundResource(R.mipmap.icon_408);
                break;
            case "409"://中到大雪
                icon.setBackgroundResource(R.mipmap.icon_409);
                break;
            case "410"://大到暴雪
                icon.setBackgroundResource(R.mipmap.icon_410);
                break;
            case "499"://雪
                icon.setBackgroundResource(R.mipmap.icon_499);
                break;
            case "500"://薄雾
                icon.setBackgroundResource(R.mipmap.icon_500);
                break;
            case "501"://雾
                icon.setBackgroundResource(R.mipmap.icon_501);
                break;
            case "502"://霾
                icon.setBackgroundResource(R.mipmap.icon_502);
                break;
            case "503"://扬沙
                icon.setBackgroundResource(R.mipmap.icon_503);
                break;
            case "504"://浮尘
                icon.setBackgroundResource(R.mipmap.icon_504);
                break;
            case "507"://沙尘暴
                icon.setBackgroundResource(R.mipmap.icon_507);
                break;
            case "508"://强沙尘暴
                icon.setBackgroundResource(R.mipmap.icon_508);
                break;
            case "509"://浓雾
                icon.setBackgroundResource(R.mipmap.icon_509);
                break;
            case "510"://强浓雾
                icon.setBackgroundResource(R.mipmap.icon_510);
                break;
            case "511"://中度霾
                icon.setBackgroundResource(R.mipmap.icon_511);
                break;
            case "512"://重度霾
                icon.setBackgroundResource(R.mipmap.icon_512);
                break;
            case "513"://严重霾
                icon.setBackgroundResource(R.mipmap.icon_513);
                break;
            case "514"://大雾
                icon.setBackgroundResource(R.mipmap.icon_514);
                break;
            case "515"://特强浓雾
                icon.setBackgroundResource(R.mipmap.icon_515);
                break;
        }
    }

    /**
     * 获得对应时间段
     * @param time
     * @return
     */
    public static String showTimeInfo(String time){
        String timeInfo = null;
        int timeInt = 0;

        if (time.trim().length() == 5){
            timeInt = Integer.parseInt(time.trim().substring(0,2));
        }
        if (time.trim().length() == 4){
            timeInt = Integer.parseInt(time.trim().substring(0,1));
        }

        if (timeInt >= 0 && timeInt <= 6) {
            timeInfo = "凌晨";
        }else if (timeInt > 6 && timeInt <= 12) {
            timeInfo = "上午";
        }else if (timeInt > 12 && timeInt <= 13) {
            timeInfo = "中午";
        }else if (timeInt > 13 && timeInt <= 18) {
            timeInfo = "下午";
        } else if (timeInt > 18 && timeInt <= 24) {
            timeInfo = "晚上";
        } else {
            timeInfo = "未知";
        }
        return timeInfo;
    }
}
