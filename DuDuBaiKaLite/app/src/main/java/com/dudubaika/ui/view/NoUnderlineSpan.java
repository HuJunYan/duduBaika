package com.dudubaika.ui.view;


import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * 无下划线的Span
 * Author: msdx (645079761@qq.com)
 * Time: 14-9-4 上午10:43
 */
public class NoUnderlineSpan extends UnderlineSpan {

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }



}