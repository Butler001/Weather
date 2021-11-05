package com.example.mvplibrary.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理所有Activity MVP架构
 */
public class ActivityManager {
    //保存所有创建的Activity
    List<Activity> allActivities = new ArrayList<>();


    /**
     * 添加Activity到管理器
     * @param activity
     */
    public void addActivity(Activity activity){
        if (activity != null){
            allActivities.add(activity);
        }
    }

    /**
     * 移除activity
     * @param activity
     */
    private void removeActivity(Activity activity){
        if (activity != null){
            allActivities.remove(activity);
        }
    }

    /**
     * 关闭所有Activity
     */
    private void finishAllActivities(){
        for (Activity activity :allActivities) {
            activity.finish();
        }
    }

    /**
     * 返回最上层Activity
     * @return Activity
     */
    public Activity getTaskTop(){
        return allActivities.get(allActivities.size()-1);
    }
}
