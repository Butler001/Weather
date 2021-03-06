package com.example.mvplibrary.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mvplibrary.R;
import com.example.mvplibrary.kit.KnifeKit;

import org.jetbrains.annotations.NotNull;

import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements UiCallback {

    protected View rootView;
    protected LayoutInflater layoutInflater;
    protected Activity context;
    private Unbinder unbinder;

    //添加加载弹窗
    private Dialog mDialog;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeView(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        layoutInflater = inflater;
        if (rootView == null){
            rootView = inflater.inflate(getLayoutId(),null);
            unbinder = KnifeKit.bind(this,rootView);

        }else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null){
                viewGroup.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.context = (Activity)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void initBeforeView(Bundle savedInstanceState) {

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
