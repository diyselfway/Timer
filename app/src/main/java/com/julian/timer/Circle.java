package com.julian.timer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Julian on 2015/12/9.
 */
public class Circle extends View {

    private int mFirstColor;
    private boolean mMovable;
    private int mSecondColor;
    private int mStartAngle;
    private int mSweepAngle;
    private double mSpeed;
    private int mStrokeWidth;
    private int mRadius;
    private Paint mPaint;
    private float mProgress;


    public Circle(Context context) {
        this(context, null);
    }

    public Circle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Circle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mProgress = 0;
        mFirstColor = Color.WHITE;
        mMovable = false;
        mSecondColor = Color.RED;
        mStartAngle = 0;
        mSweepAngle = 0;
        mSpeed = 100;
        mRadius = -1;
        mStrokeWidth = -1;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Circle,defStyleAttr,0);
        int n = typedArray.getIndexCount();
        for(int i = 0; i < n; i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.Circle_first_color:
                    mFirstColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.Circle_movable:
                    mMovable = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.Circle_second_color:
                    mSecondColor = typedArray.getColor(attr,Color.RED);
                    break;
                case R.styleable.Circle_start_angle:
                    mStartAngle = typedArray.getInt(attr,-90);
                    break;
                case R.styleable.Circle_sweep_angle:
                    mSweepAngle = typedArray.getInt(attr,180);
                    break;
                case R.styleable.Circle_speed:
                    mSpeed = typedArray.getFloat(attr,100);
                    break;
                case R.styleable.Circle_radius:
                    mRadius = typedArray.getDimensionPixelSize(attr,-1);
                    break;
                case R.styleable.Circle_stroke_width:
                    mStrokeWidth = typedArray.getDimensionPixelSize(attr,-1);
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        move();
    }

    public void move(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mMovable && mProgress < mSweepAngle){
                        mProgress++;
                        try {
                            Thread.sleep((long)(1000/mSpeed));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        postInvalidate();
                    }
                }
            }).start();
    }


    public int getSweepAngle() {
        return mSweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        mSweepAngle = sweepAngle;
    }

    public void setStartAngle(int startAngle) {
        mStartAngle = startAngle;
    }

    public void setSpeed(double speed) {
        mSpeed = speed;
    }

    public void setProgress(float progress) {
        mProgress = progress;
    }

    public float getProgress() {
        return mProgress;
    }

    public boolean isMovable() {
        return mMovable;
    }

    public void setMovable(boolean movable) {
        mMovable = movable;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStrokeWidth == -1){
            mStrokeWidth = Math.min(getWidth(),getHeight())/60;
        }
        if(mRadius == -1){
            mRadius = Math.min(getWidth(),getHeight())/2 - mStrokeWidth*2;
        }
        mPaint.setColor(mFirstColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mPaint);

        if( mSweepAngle != 0){
            mPaint.setColor(mSecondColor);
            canvas.drawArc(getWidth() / 2 - mRadius, getHeight() / 2 - mRadius, getWidth() / 2 + mRadius, getHeight() / 2 + mRadius, mStartAngle, mProgress, false, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            float cx = (float) (getWidth()/2+mRadius*Math.cos((mStartAngle+mProgress)*1.0/180*Math.PI));
            float cy = (float) (getHeight()/2+mRadius*Math.sin((mStartAngle+mProgress)*1.0/180*Math.PI));
            canvas.drawCircle(cx, cy, mStrokeWidth*1.5f, mPaint);
        }
    }
}
