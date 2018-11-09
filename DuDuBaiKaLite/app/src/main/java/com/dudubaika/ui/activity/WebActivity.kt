package com.dudubaika.ui.activity

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dudubaika.R
import com.dudubaika.base.SimpleActivity
import com.dudubaika.log.LogUtil
import com.dudubaika.util.HybridInterface
import com.dudubaika.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_web.*

/**
 * H5页面
 * Created by wang on 2017/12/5.
 */
class WebActivity : SimpleActivity() {
    var mUrl: String? = null
    override fun getLayout(): Int = R.layout.activity_web

    companion object {
        var WEB_URL_KEY = "web_url_key"
        var WEB_URL_TITLE = "web_url_title"
    }

    override fun initView() {
        StatusBarUtil.setStatusBarBgWhite(mActivity)
        defaultTitle="H5 界面"
        var systemUiVisibility = mActivity.window.decorView.systemUiVisibility
        systemUiVisibility = systemUiVisibility xor View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        mActivity.window.decorView.systemUiVisibility = systemUiVisibility

        tv_web_back.setOnClickListener { backActivity() }
        initWebView()
    }


    override fun initData() {
        mUrl = intent.getStringExtra(WEB_URL_KEY)
        var title = intent.getStringExtra(WEB_URL_TITLE)
        tv_web_title.text = title
        wv_web.loadUrl(mUrl)
    }

    private fun checkCanGoback() {
        if (wv_web.canGoBack()) {
            wv_web.goBack()
        } else {
            backActivity()
        }
    }


    private fun initWebView() {
        val webSettings = wv_web.settings
        // 打开页面时， 自适应屏幕
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        // 便页面支持缩放
        webSettings.javaScriptEnabled = true //支持js
        webSettings.domStorageEnabled = true
        webSettings.setSupportZoom(true) //支持缩放
        // 设置出现缩放工具
        webSettings.builtInZoomControls = true;
//        扩大比例的缩放
        webSettings.useWideViewPort = true;
//        自适应屏幕
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
        webSettings.loadWithOverviewMode = true;
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        //loaclStorge的使用
        webSettings.setAppCacheMaxSize(1024*1024*8)
        webSettings.setAppCachePath(applicationContext.cacheDir.absolutePath)
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)

//        wv_web.addJavascriptInterface( HybridInterface(mActivity),  "shixintest"  )

        wv_web.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            //使用系统自带的浏览器下载
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {

            }
        }
        wv_web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                LogUtil.d("abc", "mUrl--->" + url)
                if (url != null) {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url)
                    } else {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        } catch (e: Exception) {
                        }
                    }
                }
                return true
            }

        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            val canGoBack = wv_web.canGoBack()
            if (canGoBack) {
                wv_web.goBack()
            } else {
                backActivity()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    private fun getLocalStorageUserKey() {
        /*if (wv_web != null && TextUtils.isEmpty(APPEnvironment.getBeforeLoginUserKey())) {

        }*/
//        wv_web.loadUrl( "javascript:(function(){ var localStorage = window.localStorage; window.shixintest.getUserKey(localStorage.getItem('uid'))})()");

        //与h5数据互通

        //存
            wv_web.evaluateJavascript("window.localStorage.setItem('age','" + 20 + "');",null)
            //取
            wv_web.evaluateJavascript("javascript:(window.localStorage.getItem('uid'))",{
                LogUtil.i("WebViewFragment", "---$it")
            })
            wv_web.evaluateJavascript("javascript:(window.localStorage.getItem('age'))", {
            })
        }

        override fun onDestroy() {
            super.onDestroy()
              if (wv_web != null) {
                val parent = wv_web.parent as ViewGroup
                parent.removeView(wv_web)
                wv_web.removeAllViews()
                wv_web.destroy()
              }
        }


    fun setValue(){

        //1.拼接 JavaScript 代码
         val userAgent = "shixinzhang";
        val js = "window.localStorage.setItem('userAgent','$userAgent');"
        val jsUrl = "javascript:(function({\n" +
                "    var localStorage = window.localStorage;\n" +
                "    localStorage.setItem('uid','\" + userAgent + \"')"
                "})()"

        //2.根据不同版本，使用不同的 API 执行 Js
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wv_web.evaluateJavascript(js, null);
        } else {
            wv_web.loadUrl(jsUrl)
            wv_web.reload()
        }

    }
}