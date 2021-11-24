package com.example.mvplibrary.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mvplibrary.R;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.SoftReference;

/**
 * 加载框
 */
public class LoadingView extends AppCompatImageView {
    private int mCenterRotateX;//旋转中心X
    private int mCenterRotateY;//旋转中心Y
    private LoadingRunnable mRunnable;

    public LoadingView(@NonNull @NotNull Context context) {
        super(context);
    }

    public LoadingView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化加载图片
     */
    private void init(){
        setScaleType(ScaleType.MATRIX);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loading);
        setImageBitmap(bitmap);
        mCenterRotateX = bitmap.getWidth() / 2;
        mCenterRotateY = bitmap.getHeight() / 2;
    }

    /**
     * 在onDraw()之前调用
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRunnable == null){
            mRunnable = new LoadingRunnable(this);
        }
        if (!mRunnable.isLoading){
            mRunnable.start();
        }
    }

    /**
     * 在View销毁时调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRunnable != null){
            mRunnable.stop();
        }
        mRunnable = null;
    }

    class LoadingRunnable implements Runnable{

        private boolean isLoading;
        private Matrix mMatrix;
        private SoftReference<LoadingView> mLoadingViewSoftReference;
        private float mDegrees = 0f;

        public LoadingRunnable(LoadingView loadingView) {
            mLoadingViewSoftReference = new SoftReference<LoadingView>(loadingView);
            mMatrix = new Matrix();
        }

        @Override
        public void run() {
            if (mLoadingViewSoftReference.get().mRunnable != null && mMatrix != null){
                mDegrees += 30f;
                mMatrix.setRotate(mDegrees,mCenterRotateX,mCenterRotateY);
                mLoadingViewSoftReference.get().setImageMatrix(mMatrix);
                if (mDegrees == 360){
                    mDegrees=0f;
                }
                if (isLoading){
                    mLoadingViewSoftReference.get().postDelayed(mLoadingViewSoftReference.get().mRunnable,100);
                }
            }

        }
        public void start() {
            isLoading = true;
            if (mLoadingViewSoftReference.get().mRunnable != null && mMatrix != null){
                mLoadingViewSoftReference.get().postDelayed(mLoadingViewSoftReference.get().mRunnable,100);
            }
        }

        public void stop() {
            isLoading = false;
        }

    }
}
