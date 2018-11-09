package com.dudubaika.util;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class HybridInterface {
    Context context;

    public HybridInterface(Context context) {
        this.context = context;
    }

    //Js 回调方法，
    @JavascriptInterface
    public void getUserKey(String userKey) {
        Log.i("WebViewFragment", "读取到userKey : " + userKey);
        //已经拿到值，进行相关操作

    }



}
