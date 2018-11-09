package com.dudubaika.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {
    private Button button;
    private TextView textView;
    private String msg;
    //是否来自登录
    private boolean isFromLogin = false;

    public TimeCount(long millisInFuture, long countDownInterval) {
        this(null, millisInFuture, countDownInterval, "");
    }

    public TimeCount(TextView textView, long millisInFuture, long countDownInterval, String msg) {
        super(millisInFuture, countDownInterval);
        if (textView != null) {
            textView.setClickable(false);
        }
        this.textView = textView;
        this.msg = msg;
    }

    public TimeCount(TextView textView, long millisInFuture, long countDownInterval, String msg,boolean isFromLogin) {
       this(textView,millisInFuture,countDownInterval,msg);
       this.isFromLogin = isFromLogin;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (textView != null && !isFromLogin) {
            textView.setTextColor(Color.parseColor("#FF7058"));
            textView.setText(millisUntilFinished / 1000 + "s后重发");
        }

        if (null !=textView && isFromLogin){
            textView.setTextColor(Color.parseColor("#C8C8C8"));
            textView.setText("已获取("+millisUntilFinished / 1000 + "s)");
        }
    }

    @Override
    public void onFinish() {
        if (textView != null && !isFromLogin) {
            textView.setClickable(true);
            textView.setTextColor(Color.parseColor("#FF7058"));
            textView.setText(msg);
        }
        if (textView != null && isFromLogin) {
            textView.setClickable(true);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setText(msg);
        }
    }

    public void finish() {
        mHandler.sendEmptyMessage(0);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            TimeCount.this.onFinish();
            TimeCount.this.cancel();
        }
    };
}
