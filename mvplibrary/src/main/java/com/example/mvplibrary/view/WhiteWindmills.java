package com.example.mvplibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mvplibrary.R;

import java.lang.ref.WeakReference;

/**
 * 白色风车
 */
public class WhiteWindmills extends View {


    /**
     * 叶片长度
     */
    private float mBladeRadius;
    /**
     * 旋转中心Y
     */
    private float mCenterY;
    /**
     * 旋转中心X
     */
    private float mCenterX;
    /**
     * 风车旋转中心圆的半径
     */
    private float mPivotRadius;
    private Paint paint = new Paint();
    /**
     * 风车旋转角度
     */
    private int mOffsetAngle;
    private Path path = new Path();
    /**
     * 风车下端有长方形的支柱
     */
    private RectF mRectF= new RectF();
    /**
     * VIEW的宽度
     */
    private int weight;
    /**
     * VIEW的长度
     */
    private int height;
    /**
     * VIEW的颜色
     */
    private int color;

    private MsgHandler msgHandler = new MsgHandler(this);

    public WhiteWindmills(Context context) {
        super(context);
    }

    public WhiteWindmills(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WhiteWindmills(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    /**
     * 配置paint
     * @param context
     * @param attrs
     */
    private void initView(Context context , AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.whiteWindmills);
        if (array != null){
            color = array.getColor(R.styleable.whiteWindmills_windColor, Color.WHITE);

            array.recycle();
        }
        //抗锯齿
        paint.setAntiAlias(true);
        paint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int weightMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int weightMode = MeasureSpec.getMode(widthMeasureSpec);

        height = heightMeasure;
        weight = weightMeasure;
        mCenterY = height/2;
        mCenterX = weight/2;

        mPivotRadius = (float) weight/(float)40;
        mBladeRadius = mCenterY-2*mPivotRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPivot(canvas);

        drawWindBlade(canvas);

        drawPillar(canvas);
    }

    /**
     * 画风车的中心
     * @param canvas
     */
    private void drawPivot(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX,mCenterY,mPivotRadius,paint);
    }

    /**
     * 画叶片
     * @param canvas
     */
    private void drawWindBlade(Canvas canvas){
        canvas.save();
        path.reset();
        //旋转画布定位圆心
        canvas.rotate(mOffsetAngle,mCenterX,mCenterY);

        //画三角形叶片
        //多边形的起点
        path.moveTo(mCenterX,mCenterY-mPivotRadius);
        path.lineTo(mCenterX,mCenterY-mPivotRadius-mBladeRadius);
        path.lineTo(mCenterX+mPivotRadius,mPivotRadius+mBladeRadius * (float)2/(float)3);
        path.close();
        canvas.drawPath(path,paint);

        //第二个叶片
        canvas.rotate(120,mCenterX,mCenterY);
        canvas.drawPath(path,paint);

        //第三个叶片
        canvas.rotate(120,mCenterX,mCenterY);
        canvas.drawPath(path,paint);
        canvas.restore();
    }

    /**
     * 画圆柱
     * @param canvas
     */
    private void drawPillar(Canvas canvas){
        path.reset();
        //画上下圆之间的圆柱
        path.moveTo(mCenterX - mPivotRadius/2,mCenterY + mPivotRadius + mPivotRadius/2);
        path.lineTo(mCenterX + mPivotRadius/2,mCenterY + mPivotRadius + mPivotRadius/2);
        path.lineTo(mCenterX + mPivotRadius , height-2 * mPivotRadius);
        path.lineTo(mCenterX - mPivotRadius, height - 2*mPivotRadius);
        path.close();
        //画顶部半圆
        mRectF.set(mCenterX - mPivotRadius/2 , mCenterY+mPivotRadius, mCenterX + mPivotRadius/2,mCenterY + 2*mPivotRadius);
        path.addArc(mRectF,180,180);

        //画底部半圆
        mRectF.set(mCenterX - mPivotRadius , height - 3* mPivotRadius ,mCenterX+mPivotRadius,height - mPivotRadius);
        path.addArc(mRectF,0,180);

        canvas.drawPath(path,paint);
    }

    public void start(){
        stop();
        msgHandler.sendEmptyMessageDelayed(0,10);
    }

    /**
     * stop rotate
     */
    public void stop(){
        msgHandler.removeMessages(0);
    }
    static class MsgHandler extends Handler {
        private WeakReference<WhiteWindmills> mView;

        MsgHandler(WhiteWindmills view){
            mView = new WeakReference<WhiteWindmills>(view);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            WhiteWindmills view = mView.get();
            if (view != null){
                view.handleMessage(msg);
            }
        }
    }

    private void handleMessage(Message msg) {
        if (mOffsetAngle >= 0 && mOffsetAngle < 360){
            mOffsetAngle += 1;
        }else {
            mOffsetAngle = 1;
        }
        invalidate();
        start();
    }
}
