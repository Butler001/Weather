package com.example.mvplibrary.mvp;

import android.os.Bundle;

import com.example.mvplibrary.base.BaseActivity;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;

/**
 * 用于访问网络的Activity
 * @param <P>
 */
public abstract class MvpActivity <P extends BasePresenter> extends BaseActivity {
    protected P mPresenter;

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attach((BaseView) this);
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detach((BaseView) this);
    }
}
