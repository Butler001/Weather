package com.example.mvplibrary.mvp;

import android.os.Bundle;

import com.example.mvplibrary.base.BaseFragment;
import com.example.mvplibrary.base.BasePresenter;
import com.example.mvplibrary.base.BaseView;

/**
 * 用于访问网络的Fragment
 * @param <P>
 */
public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mPresenter;

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attach((BaseView) this);
    }

    protected abstract P createPresenter();

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter.detach((BaseView) this);
        }
    }
}
