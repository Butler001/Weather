package com.example.mvplibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.mvplibrary.R;

/**
 * 圆形进度条
 */
public class RoundProgressBar extends View {

    private int mStrokeWidth = dp2px(8);//圆弧的宽度
    private float mStartAngle = 135;//圆弧开始的角度
    private float mAngleSize = 270;//圆弧起点和终点的夹角
    private int mArcBgColor;//圆弧背景色
    private float mMaxProgress;//最大的进度，通过比例来计算当前的进度
    private float mCurrentAngle;//当前的夹角
    private float mCurrentProgress = 0;//当前进度
    private long mDuration = 2000;//动画执行时长
    private int mProgressColor;//进度圆弧的颜色
    private String mFirstText = "0";//第一行文本
    private int mFirstTextColor = Color.WHITE;//第一行文本的颜色
    private float mFirstTextSize = 56f;//第一行文本的字体大小
    private String mSecondText = " ";//第二行文本
    private int mSecondTextColor = Color.WHITE;//第二行文本的颜色
    private float mSecondTextSize = 56f;//第二行文本的大小
    private String mMinText = "0";//进度最小值
    private int mMinTextColor = Color.WHITE;//进度最小值的颜色
    private float mMinTextSize = 32f;//进度最小值字体大小
    private String mMaxText = "0";//进度最大值
    private int mMaxTextColor = Color.WHITE;//进度最大值颜色
    private float mMaxTextSize = 32f;//进度最大值字体大小

    public RoundProgressBar(Context context) {
        super(context,null);
    }

    public RoundProgressBar(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    public RoundProgressBar(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context,attrs);
    }



    /**
     * 初始化参数
     * @param context
     * @param attributeSet
     */
    private void initAttr(Context context,AttributeSet attributeSet){
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.RoundProgressBar);
        mMaxProgress = array.getFloat(R.styleable.RoundProgressBar_round_max_progress,500f);
        mArcBgColor = array.getColor(R.styleable.RoundProgressBar_round_bg_color,Color.YELLOW);
        mStrokeWidth = dp2px(array.getDimension(R.styleable.RoundProgressBar_round_stroke_width,12f));
        mCurrentProgress = array.getFloat(R.styleable.RoundProgressBar_round_progress,300f);
        mProgressColor = array.getColor(R.styleable.RoundProgressBar_round_progress_color,Color.RED);
        mFirstText = array.getString(R.styleable.RoundProgressBar_round_first_text);
        mFirstTextSize = dp2px(array.getDimension(R.styleable.RoundProgressBar_round_first_text_size,20f));
        mFirstTextColor = array.getColor(R.styleable.RoundProgressBar_round_first_text_color,Color.RED);
        mSecondText = array.getString(R.styleable.RoundProgressBar_round_second_text);
        mSecondTextSize = dp2px(array.getDimension(R.styleable.RoundProgressBar_round_second_text_size,20f));
        mSecondTextColor = array.getColor(R.styleable.RoundProgressBar_round_second_text_color,Color.RED);
        mMinText = array.getString(R.styleable.RoundProgressBar_round_min_text);
        mMinTextSize = dp2px(array.getDimension(R.styleable.RoundProgressBar_round_min_text_size,20f));
        mMinTextColor = array.getColor(R.styleable.RoundProgressBar_round_min_text_color,Color.RED);
        mMaxText = array.getString(R.styleable.RoundProgressBar_round_max_text);
        mMaxTextSize = dp2px(array.getDimension(R.styleable.RoundProgressBar_round_max_text_size,20f));
        mMaxTextColor = array.getColor(R.styleable.RoundProgressBar_round_max_text_color,Color.RED);
        mAngleSize = array.getFloat(R.styleable.RoundProgressBar_round_angle_size,270f);
        mStartAngle = array.getFloat(R.styleable.RoundProgressBar_round_start_angle,135f);

        array.recycle();
    }

    /**
     * 绘制圆形进度框
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth()/2;

        RectF rectF = new RectF();
        rectF.left = mStrokeWidth;
        rectF.top = mStrokeWidth;
        rectF.right = centerX * 2 - mStrokeWidth;
        rectF.bottom = centerX * 2 - mStrokeWidth;

        //画最外层的圆弧
        drawArcBg(canvas,rectF);
        //画进度的圆弧
        drawArcProgress(canvas,rectF);
        //画第一级文字
        drawFirstText(canvas,centerX);
        //绘制第二行文字
        drawSecondText(canvas,centerX);
        //绘制最小值文本
        drawMinText(canvas,rectF.left,rectF.bottom);
        //绘制最大值文本
        drawMaxText(canvas,rectF.right,rectF.bottom);

    }



    /**
     * 画最外层的圆弧
     * @param canvas
     * @param rectF
     */
    private void drawArcBg(Canvas canvas , RectF rectF){
        Paint mPaint = new Paint();

        //画笔的填充样式，Paint.Style.FILL 填充内部;Paint.Style.FILL_AND_STROKE 填充内部和描边;Paint.Style.STROKE 描边
        mPaint.setStyle(Paint.Style.STROKE);
        //圆弧的宽度
        mPaint.setStrokeWidth(mStrokeWidth);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //画笔的颜色
        mPaint.setColor(mArcBgColor);
        //画笔的样式 Paint.Cap.Round 圆形,Cap.SQUARE 方形
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //开始画圆弧
        canvas.drawArc(rectF,mStartAngle,mAngleSize,false,mPaint);
    }

    /**
     * 画进度的圆弧
     * @param canvas
     * @param rectF
     */
    private void drawArcProgress(Canvas canvas,RectF rectF){
        Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(mProgressColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(rectF,mStartAngle,mCurrentAngle,false,mPaint);
    }

    /**
     * 绘制第一级文字
     * @param canvas
     * @param centerX
     */
    private void drawFirstText(Canvas canvas ,float centerX){
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mFirstTextColor);
        mPaint.setTextSize(mFirstTextSize);
        Rect firstTextBounds = new Rect();
        mPaint.getTextBounds(mFirstText,0,mFirstText.length(),firstTextBounds);
        canvas.drawText(mFirstText,centerX,firstTextBounds.height()/2+getHeight()*2/5,mPaint);
    }

    /**
     * 绘制第二行文字
     * @param canvas
     * @param centerX
     */
    private void drawSecondText(Canvas canvas , float centerX){
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSecondTextColor);
        mPaint.setTextSize(mSecondTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();
        mPaint.getTextBounds(mSecondText,0,mSecondText.length(),bounds);
        canvas.drawText(mSecondText, centerX, getHeight() / 2 + bounds.height() / 2 +
                getFontHeight(mSecondText, mSecondTextSize), mPaint);
    }

    /**
     * 获取字体高度
     * @param text
     * @param textSize
     * @return
     */
    private float getFontHeight(String text, float textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text,0,text.length(),bounds);
        return bounds.height();
    }

    /**
     * 绘制最小值文本
     * @param canvas
     * @param leftX
     * @param bottomY
     */
    private void drawMinText(Canvas canvas,float leftX,float bottomY){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mMinTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mMinTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(mMinText,0,mMinText.length(),bounds);
        canvas.drawText(mMinText,leftX + bounds.width()*4,bottomY + 16,paint);
    }

    /**
     * 绘制最大值文本
     * @param canvas
     * @param right
     * @param bottom
     */
    private void drawMaxText(Canvas canvas, float right, float bottom) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mMaxTextColor);
        paint.setTextSize(mMaxTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();
        paint.getTextBounds(mMaxText,0,mMaxText.length(),bounds);
        canvas.drawText(mMaxText,right - bounds.width(),bottom + 16,paint);
    }

    /**
     * 设置最大进度
     * @param progress
     */
    public void setMaxProgress(int progress){
        if (progress < 0){
            throw new IllegalArgumentException("The value of progress could not be less than 0");
        }
        mMaxProgress = progress;
    }

    /**
     * 设置当前进度
     * @param progress
     */
    public void setCurrentProgress(float progress){
        if (progress < 0){
            throw new IllegalArgumentException("The value of progress could not be less than 0");
        }
        if (progress > mMaxProgress){
            progress = mMaxProgress;
        }
        mCurrentProgress = progress;
        float size = mCurrentProgress / mMaxProgress;
        mCurrentAngle = (int)(mAngleSize * size);


    }

    /**
     * 设置进度条颜色
     * @param color
     */
    public void setProgressColor(int color){
        if (color <= 0){
            throw  new IllegalArgumentException("The value of color could not be 0");
        }
        mProgressColor = color;
    }

    /**
     * 设置圆弧背景颜色
     * @param color
     */
    public void setArcBgColor(int color){
        if (color <= 0){
            throw  new IllegalArgumentException("The value of color could not be less than 0");
        }
        mArcBgColor = color;
    }

    /**
     * 设置圆弧宽度
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth){
        if (strokeWidth < 0){
            throw new IllegalArgumentException("The value of strokeWidth could not be less than 0");
        }
        mStrokeWidth = dp2px(strokeWidth);
    }

    /**
     * 设置动画时长
     * @param duration
     */
    public void setDuration(long duration){
        if (duration < 0){
            throw new IllegalArgumentException("The value of duration could not be less than 0");
        }
        mDuration = duration;
    }

    /**
     * 设置第一行文本
     * @param firstText
     */
    public void setFirstText(String firstText){
        mFirstText = firstText;
    }

    /**
     * 设置第一行文本的颜色
     * @param firstTextColor
     */
    public void setFirstTextColor(int firstTextColor){
        if (firstTextColor <= 0){
            throw new IllegalArgumentException("The value of firstTextColor could not be less than 0");
        }
        mFirstTextColor = firstTextColor;
    }

    /**
     * 设置第一行文本的大小
     * @param textSize
     */
    public void setFirstTextSize(float textSize){
        if (textSize <= 0){
            throw new IllegalArgumentException("The value of textSize could not less than 0");
        }
        mFirstTextSize = textSize;
    }
    /**
     * 设置第二行文本
     * @param secondText
     */
    public void setSecondText(String secondText){
        mSecondText = secondText;
    }

    /**
     * 设置第二行文本的颜色
     * @param secondTextColor
     */
    public void setSecondTextColor(int secondTextColor){
        if (secondTextColor <= 0){
            throw new IllegalArgumentException("The value of secondTextColor could not be less than 0");
        }
        mSecondTextColor = secondTextColor;
    }

    /**
     * 设置第二行文本的大小
     * @param textSize
     */
    public void setSecondTextSize(float textSize){
        if (textSize <= 0){
            throw new IllegalArgumentException("The value of textSize could not less than 0");
        }
        mSecondTextSize = textSize;
    }

    /**
     * 设置最小值文本
     * @param text
     */
    public void setMinText(String text){
        mMinText = text;
    }

    /**
     * 设置最小值文本的颜色
     * @param color
     */
    public void setMinTextColor(int color){
        if (color <= 0){
            throw  new IllegalArgumentException("The value of color could not be less than 0");
        }
        mMinTextColor = color;
    }

    /**
     * 设置最小值文本的大小
     * @param textSize
     */
    public void setMinTextSize(float textSize){
        if (textSize <= 0){
            throw new IllegalArgumentException("The value of textSize could not less than 0");
        }
        mMinTextSize = textSize;
    }

    /**
     * 设置最大值文本
     * @param text
     */
    public void setMaxText(String text){
        mMaxText = text;
    }

    /**
     * 设置最大值文本颜色
     * @param color
     */
    public void setMaxTextColor(int color){
        if (color <= 0){
            throw  new IllegalArgumentException("The value of color could not be less than 0");
        }
        mMaxTextColor = color;
    }

    /**
     * 设置最大值文本大小
     * @param textSize
     */
    public void setMaxTextSize(float textSize){
        if (textSize <= 0){
            throw new IllegalArgumentException("The value of textSize could not less than 0");
        }
        mMaxTextSize = textSize;
    }

    /**
     * 设置圆弧开始的角度
     * @param startAngle
     */
    public void setStartAngle(int startAngle){
        mStartAngle = startAngle;
    }

    /**
     * 设置转动角度大小
     * @param angleSize
     */
    public void setAngleSize(int angleSize){
        mAngleSize = angleSize;
    }

    /**
     * 设置动画
     * @param start
     * @param target
     */
    private void setAnimator(float start , float target){
        ValueAnimator animator = ValueAnimator.ofFloat(start,target);
        animator.setDuration(mDuration);
        animator.setTarget(mCurrentAngle);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }
    /**
     * dp  转化为 px
     * @param dp
     * @return
     */
    private int dp2px(float dp) {
        float destiny = getResources().getDisplayMetrics().density;
        return (int)(dp * destiny + 0.5f * (dp >=0 ? 1 : -1));
    }
}
