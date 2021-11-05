package com.example.mvplibrary.base;

import java.lang.ref.WeakReference;

/**
 * 基类 操作视图
 * @param <V>
 */
public class BasePresenter <V extends BaseView>{
    private WeakReference<V> mWeakReference;

    /**
     * 绑定视图
     * @param v
     */
    public void attach(V v){
        mWeakReference = new WeakReference<V>(v);
    }

    /**
     * 分离视图
     * @param v
     */
    public void detach(V v){
        if (mWeakReference != null){
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    /**
     * 获取视图
     * @return V
     */
    public V getView(){
        if (mWeakReference != null){
            return mWeakReference.get();
        }
        return null;
    }
}
