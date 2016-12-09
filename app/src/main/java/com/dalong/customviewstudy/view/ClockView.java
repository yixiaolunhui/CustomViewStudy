package com.dalong.customviewstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.dalong.customviewstudy.R;

import java.util.Calendar;

/**
 * 自定义学习一  钟表控件
 * Created by dalong on 2016/12/8.
 */

public class ClockView extends View {
    //handler what
    public final int START_CLOCK=1000;

    //文字画笔对象
    private  Paint mTextPaint;

    //圆，指针，刻度画笔
    private  Paint mPaint;

    //半径
    public float mRadius;

    //外圆的颜色
    public int mCircleColor;

    // 外圆的宽度
    public float mCircleWidth;

    //文字的大小
    public float mTextSize;

    //文字的颜色
    public int mTextColor;

    //大刻度颜色
    public int mBigScaleColor;

    //中刻度
    public int mMiddlecaleColor;

    //小刻度颜色
    public int mSmallScaleColor;

    //时针颜色
    public int mHourHandColor;

    //分针颜色
    public int mMinuteHandColor;

    //秒针颜色
    public int mSecondHandColor;

    //时针宽度
    public float mHourHandWidth;

    //分针宽度
    public float mMinuteHandWidth;

    //秒针宽度
    public float mSecondHandWidth;

    //控件宽度
    public int mWidth;

    //控件高度
    public int mHeght;

    public ClockView(Context context) {
        this(context,null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.ClockView);
        mRadius=typedArray.getDimension(R.styleable.ClockView_mRadius,400);
        mCircleColor=typedArray.getColor(R.styleable.ClockView_mCircleColor, Color.WHITE);
        mCircleWidth=typedArray.getDimension(R.styleable.ClockView_mCircleWidth,20);
        mTextSize=typedArray.getDimension(R.styleable.ClockView_mCircleWidth,40);
        mTextColor=typedArray.getColor(R.styleable.ClockView_mTextColor,Color.DKGRAY);
        mBigScaleColor=typedArray.getColor(R.styleable.ClockView_mBigScaleColor,Color.BLACK);
        mSmallScaleColor=typedArray.getColor(R.styleable.ClockView_mSmallScaleColor,Color.RED);
        mMiddlecaleColor=typedArray.getColor(R.styleable.ClockView_mMiddlecaleColor,Color.BLACK);
        mHourHandColor=typedArray.getColor(R.styleable.ClockView_mHourHandColor,Color.BLACK);
        mMinuteHandColor=typedArray.getColor(R.styleable.ClockView_mMinuteHandColor,Color.BLACK);
        mSecondHandColor=typedArray.getColor(R.styleable.ClockView_mSecondHandColor,Color.BLACK);
        mHourHandWidth=typedArray.getDimension(R.styleable.ClockView_mHourHandWidth,20);
        mMinuteHandWidth=typedArray.getDimension(R.styleable.ClockView_mMinuteHandWidth,10);
        mSecondHandWidth=typedArray.getDimension(R.styleable.ClockView_mSecondHandWidth,5);

        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureSize(widthMeasureSpec),measureSize(heightMeasureSpec));
    }

    private int measureSize(int mMeasureSpec) {
        int result;
        int mode=MeasureSpec.getMode(mMeasureSpec);
        int size=MeasureSpec.getSize(mMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result=400;
            if(mode==MeasureSpec.AT_MOST){
                result=Math.min(result,size);
            }
        }
        return  result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置宽高、半径
        mWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
        mHeght=getMeasuredHeight()-getPaddingBottom()-getPaddingTop();
        mRadius=Math.min(mWidth/2,mHeght/2);
        //首先绘制圆
        drawCircle(canvas);
        //绘制刻度
        drawScale(canvas);
        //绘制指针
        drawPointer(canvas);
        //发送消息刷新ui
        handler.sendEmptyMessageDelayed(START_CLOCK,1000);
    }

    /**
     * 画圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mWidth/2,mHeght/2,mRadius,mPaint);
    }

    /**
     * 刻度和文字
     * @param canvas
     */
    private void drawScale(Canvas canvas) {

        for (int i=0;i<60;i++){
            //设置大刻度
            if(i==0||i==15||i==30||i==45){
                mPaint.setStrokeWidth(6);
                mPaint.setColor(mBigScaleColor);
                canvas.drawLine(mWidth/2,mHeght/2-mWidth/2+mCircleWidth/2,
                        mWidth/2,mHeght/2-mWidth/2+mCircleWidth/2+60,mPaint);
                String scaleTv=String.valueOf(i==0?12:i/5);
                canvas.drawText(scaleTv,mWidth/2-mTextPaint.measureText(scaleTv)/2,
                        mHeght/2-mWidth/2+mCircleWidth/2+95,mTextPaint);
            }else if (i==5||i==10||i==20||i==25||i==35||i==40||i==50||i==55)
            //设置中刻度
            {
                mPaint.setStrokeWidth(4);
                mPaint.setColor(mMiddlecaleColor);
                canvas.drawLine(mWidth/2,mHeght/2-mWidth/2+mCircleWidth/2,
                        mWidth/2,mHeght/2-mWidth/2+mCircleWidth/2+40,mPaint);
                String scaleTv=String.valueOf(i/5);
                canvas.drawText(scaleTv,mWidth/2-mTextPaint.measureText(scaleTv)/2,
                        mHeght/2-mWidth/2+mCircleWidth/2+75,mTextPaint);

            }else
            //设置小刻度
            {
                mPaint.setColor(mSmallScaleColor);
                mPaint.setStrokeWidth(2);
                canvas.drawLine(mWidth/2,mHeght/2-mWidth/2+mCircleWidth/2,
                        mWidth/2,mHeght/2-mWidth/2+mCircleWidth+30,mPaint);
            }
            canvas.rotate(6,mWidth/2,mHeght/2);
        }
    }


    /**
     * 绘制指针
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        Calendar mCalendar=Calendar.getInstance();
        //获取当前小时数
        int hours = mCalendar.get(Calendar.HOUR);
        //获取当前分钟数
        int minutes = mCalendar.get(Calendar.MINUTE);
        //获取当前秒数
        int seconds=mCalendar.get(Calendar.SECOND);

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //绘制时针
        canvas.save();
        mPaint.setColor(mHourHandColor);
        mPaint.setStrokeWidth(mHourHandWidth);
        //这里计算时针需要旋转的角度 实现原理是计算出一共多少分钟除以60计算出真实的小时数（带有小数，为了更加准确计算度数），已知12小时是360度，现在求出了实际小时数比例求出角度
        Float hoursAngle = (hours * 60 + minutes) / 60f / 12f  * 360;
        canvas.rotate(hoursAngle, mWidth / 2, mHeght / 2);
        canvas.drawLine(mWidth / 2, mHeght / 2 - mWidth/2f*0.5f, mWidth / 2, mHeght / 2 +  mWidth/2f*0.15f, mPaint);
        canvas.restore();


        //绘制分针
        canvas.save();
        mPaint.setColor(mMinuteHandColor);
        mPaint.setStrokeWidth(mMinuteHandWidth);
        //这里计算分针需要旋转的角度  60分钟360度，求出实际分钟数所占的度数
        Float minutesAngle = (minutes*60+seconds) / 60f/ 60f * 360;
        canvas.rotate(minutesAngle, mWidth / 2, mHeght / 2);
        canvas.drawLine(mWidth / 2, mHeght / 2 -  mWidth/2f*0.7f, mWidth / 2, mHeght / 2 +  mWidth/2f*0.15f, mPaint);
        canvas.restore();

        //绘制中间的圆圈
        canvas.save();
        mPaint.setColor(mSecondHandColor);
        mPaint.setStrokeWidth(mSecondHandWidth);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth/2,mHeght/2,20,mPaint);
        canvas.restore();


        //绘制秒针
        canvas.save();
        mPaint.setColor(mSecondHandColor);
        mPaint.setStrokeWidth(mSecondHandWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //这里计算秒针需要旋转的角度  60秒360度，求出实际秒数所占的度数
        Float secondAngle = seconds/60f*360;
        canvas.rotate(secondAngle, mWidth / 2, mHeght / 2);
        canvas.drawLine(mWidth / 2, mHeght / 2 -  mWidth/2f*0.8f, mWidth / 2, mHeght / 2 +  mWidth/2f*0.2f, mPaint);
        canvas.restore();


    }

    /**
     * handler处理
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case START_CLOCK:
                    //更新时分秒
                    invalidate();
                    //每隔1秒更新一次
                    handler.sendEmptyMessageDelayed(START_CLOCK,1000);
                    break;
            }

        }
    };
}

