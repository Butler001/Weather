package com.example.mvplibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.mvplibrary.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 为城市列表自定义弹窗
 */
public class CityWindow {
    private CityWindow cityWindow;
    private PopupWindow popupWindow;
    private LayoutInflater inflater;
    private View view;
    private Context mContext;
    private WindowManager show;
    WindowManager.LayoutParams context;
    private Map<String ,Object> mMap = new HashMap<>();

    public Map<String, Object> getMap() {
        return mMap;
    }

    public CityWindow(Context Context) {
        this.mContext = Context;
        inflater = LayoutInflater.from(Context);
        cityWindow = this;
    }

    public CityWindow(Context mContext, Map<String, Object> mMap) {
        this.mContext = mContext;
        this.mMap = mMap;
        inflater = LayoutInflater.from(mContext);
    }

    /**
     * 右侧显示，自适应大小
     * @param view
     */
    public void showRightPopupWindow(View view){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);//点击空白处不关闭弹窗，true为关闭
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        popupWindow.showAtLocation(view, Gravity.RIGHT,0,0);
        setBackgroundAlpha(0.5f,mContext);
        WindowManager.LayoutParams normal1 = ((Activity)mContext).getWindow().getAttributes();
        normal1.alpha = 0.5f;
        ((Activity)mContext).getWindow().setAttributes(normal1);
        popupWindow.setOnDismissListener(closeDismiss);
    }

    /**
     * 右侧显示 高度占满父布局
     * @param view
     */
    public void showRightPopupWindowMatchParent(View view){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);//点击空白处不关闭弹窗，true为关闭
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        popupWindow.showAtLocation(view, Gravity.RIGHT,0,0);
        setBackgroundAlpha(0.5f,mContext);
        WindowManager.LayoutParams normal1 = ((Activity)mContext).getWindow().getAttributes();
        normal1.alpha = 0.5f;
        ((Activity)mContext).getWindow().setAttributes(normal1);
        popupWindow.setOnDismissListener(closeDismiss);
    }

    /**
     * 底部显示
     * @param view
     */
    public void showBottomPopupWindow(View view){
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);//点击空白处不关闭弹窗，true为关闭
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
        setBackgroundAlpha(0.5f,mContext);
        WindowManager.LayoutParams normal1 = ((Activity)mContext).getWindow().getAttributes();
        normal1.alpha = 0.5f;
        ((Activity)mContext).getWindow().setAttributes(normal1);
        popupWindow.setOnDismissListener(closeDismiss);
    }

    /**
     * 设置背景透明度
     * @param bgAlpha
     * @param context
     */
    public static void setBackgroundAlpha(final float bgAlpha ,Context context){
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity)context).getWindow().setAttributes(lp);
    }

    /**
     * 设置弹窗动画
     * @param animId
     * @return
     */
    public CityWindow setAnim(int animId){
        if (popupWindow != null){
            popupWindow.setAnimationStyle(animId);
        }
        return cityWindow;
    }

    /**
     * 弹窗消失时去除阴影
     */
    public PopupWindow.OnDismissListener closeDismiss = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
            lp.alpha = 1f;
            ((Activity)mContext).getWindow().setAttributes(lp);
        }
    };

    /**
     * 关闭弹窗
     */
    public void closePopupWindow(){
        if (popupWindow != null){
            popupWindow.dismiss();
        }
    }
}
