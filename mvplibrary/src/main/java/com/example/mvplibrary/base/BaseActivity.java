package com.example.mvplibrary.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mvplibrary.R;
import com.example.mvplibrary.kit.KnifeKit;
import com.example.mvplibrary.utils.BaseApplication;

import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements UiCallback {
    protected Activity context;
    private Unbinder unbinder;
    //加载弹窗
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeView(savedInstanceState);
        this.context = this;
        //添加继承这个BaseActivity的Activity
        BaseApplication.getActivityManager().addActivity(this);
        if (getLayoutId() > 0){
            setContentView(getLayoutId());
            unbinder = KnifeKit.bind(this);
        }
        initData(savedInstanceState);
    }

    @Override
    public void initBeforeView(Bundle savedInstanceState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 调用弹窗
     */
    public void showLoadingDialog(){
        if (mDialog == null){
            mDialog = new Dialog(context, R.style.dialog_loading);
        }
        mDialog.setContentView(R.layout.dialog_loading);
        mDialog.setCancelable(false);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.show();
    }

    /**
     * 结束弹窗
     */
    public void dismissLoadingDialog(){
        if (mDialog != null){
            mDialog.dismiss();
        }
        mDialog = null;
    }
}
