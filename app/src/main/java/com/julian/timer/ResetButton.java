package com.julian.timer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Julian on 2015/12/16.
 */
public class ResetButton extends Button {

    private Paint mPaint;

    public ResetButton(Context context) {
        this(context, null);
    }

    public ResetButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResetButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cx = getWidth()/2;
        int cy = getHeight()/2;
        int radius = Math.min(cx,cy)*2/3;
        int strokeWidth = radius/4;
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);
        canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, -180, 310, false, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.moveTo(cx - radius - 2*strokeWidth , cy);
        path.lineTo(cx - radius + 2*strokeWidth , cy);
        path.lineTo(cx - radius,cy + 2*strokeWidth );
        path.close();
        canvas.drawPath(path, mPaint);
    }
}
