package com.julian.timer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private PlayButton mPlayButton;

    private Circle mCircle;
    public static final int PROGRESS = 123;
    public static final int TIME = 999;
    private static final int UPDATE = 0;
    private long time = 0;
    private long time_start_millis = 0;
    private long time_stop_millis = 0;
    private long time_on_click_start;
    private TextView mTv_second;
    private TextView mTv_millisecond;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE:
                    long time= time_on_click_start + System.currentTimeMillis() - time_start_millis;
                    if(time < 100000){
                        if(mCircle.isMovable()) {
                            setTimeText(time);
                            mCircle.setProgress(time * 360 / 100000);
                            mCircle.invalidate();
                            sendEmptyMessageDelayed(UPDATE,20);
                        }
                        break;
                    }else {
                        setTimeText(100000);
                    }
            }
        }
    };
    private ResetButton mResetButton;


    private void setTimeText(long time_now){

        if(time_now > 100000) time_now = 100000;
        String second;
        String millisecond;
        if(time_now/1000 < 10){
            second = "0" + Long.toString(time_now/1000);
        }else {
            second = Long.toString(time_now/1000);
        }
        mTv_second.setText(second);
        long temp = (time_now%1000)/10;
        if (temp <10){
            millisecond = "0" + Long.toString(temp);
        }else {
            millisecond = Long.toString(temp);
        }
        mTv_millisecond.setText(millisecond);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayButton = (PlayButton) findViewById(R.id.playButton);
        mResetButton = (ResetButton) findViewById(R.id.resetButton);
        mCircle = (Circle) findViewById(R.id.circle);
        mTv_second = (TextView) findViewById(R.id.text_second);
        mTv_millisecond = (TextView) findViewById(R.id.text_millisecond);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPlayButton.isPlaying()){
                    mCircle.setMovable(false);
                    mCircle.invalidate();
                    time_stop_millis = System.currentTimeMillis();
                    time = time + time_stop_millis - time_start_millis;
                }else {
                    time_start_millis = System.currentTimeMillis();
                    time_on_click_start = time;
                    mCircle.setMovable(true);
                    mCircle.setSweepAngle(360);
                    mCircle.setSpeed(3.55);
                    mHandler.sendEmptyMessageDelayed(UPDATE,10);
                }
                mPlayButton.changeState();
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircle.setSweepAngle(0);
                mCircle.setProgress(0);
                mCircle.setMovable(false);
                mPlayButton.setIsPlaying(false);
                mCircle.invalidate();
                time = 0;
                setTimeText(time);
            }
        });
    }
}
