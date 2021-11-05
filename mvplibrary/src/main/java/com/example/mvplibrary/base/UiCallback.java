package com.example.mvplibrary.base;

import android.os.Bundle;

/**
 * UI回调接口
 */
public interface UiCallback {
    //初始化savedInstanceState
    void initBeforeView(Bundle savedInstanceState);
    //初始化数据
    void initData(Bundle savedInstanceState);
    //layout
    int getLayoutId();

}
