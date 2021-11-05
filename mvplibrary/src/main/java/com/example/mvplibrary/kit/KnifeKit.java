package com.example.mvplibrary.kit;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 绑定控件ID
 */
public class KnifeKit {

    //判断绑定类型
    public static Unbinder bind(Object target){
        if (target instanceof Activity){
            return ButterKnife.bind((Activity)target);
        } else if (target instanceof Dialog){
            return ButterKnife.bind((Dialog)target);
        } else if (target instanceof View){
            return ButterKnife.bind((View)target);
        }
        return Unbinder.EMPTY;
    }
    //绑定具体资源
    public static Unbinder bind(Object target,Object resource){
        if (resource instanceof Activity){
            return ButterKnife.bind(target,(Activity)resource);
        } else if (resource instanceof Dialog){
            return ButterKnife.bind(target,(Dialog)resource);
        } else if (resource instanceof View){
            return ButterKnife.bind(target,(View)resource);
        }
        return Unbinder.EMPTY;
    }

    //解绑
    public static void Unbind(Unbinder unbinder){
        if (unbinder != Unbinder.EMPTY){
            unbinder.unbind();
        }
    }

}
