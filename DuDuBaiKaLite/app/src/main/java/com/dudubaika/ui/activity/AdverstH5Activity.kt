package com.dudubaika.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.dudubaika.R
import com.dudubaika.base.SimpleActivity
import com.dudubaika.event.RefreshCreditStatusEvent
import com.dudubaika.event.getUserAuthStatus
import com.dudubaika.log.LogUtil
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import kotlinx.android.synthetic.main.activity_web_verify.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * H5 活动详情页面, 只有从广告页面进入
 * Created by wang on 2017/12/5.
 */
class AdverstH5Activity : SimpleActivity() {
    var mUrl: String? = null
    override fun getLayout(): Int = R.layout.activity_web_verify
    private val loadHistoryUrls = ArrayList<String>()

    companion object {
        var WEB_URL_KEY = "web_url_key"
        var WEB_URL_TITLE = "web_url_title"
    }

    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, tb_web)
        defaultTitle="启动进入广告页"
        iv_web_verify_back.setOnClickListener { checkCanGoback() }
        initWebView()
    }

    override fun initData() {
        mUrl = intent.getStringExtra(WEB_URL_KEY)
        var title = intent.getStringExtra(WEB_URL_TITLE)
        tv_web_verify_title.text = title
        wv_web.loadUrl(mUrl)
        LogUtil.d("http", "murl = " + mUrl)
    }

    private fun checkCanGoback() {
        if (checkIsAuthSuccess()) {
            return
        }
        val canGoBack = wv_web.canGoBack()
        if (canGoBack) {
            goBack()
        } else {
//            //直接返回
//            refreshCreditEvent()
//            backActivity()
            startActivity<MainActivity>()
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
                        loadHistoryUrls.add(url)
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
        wv_web.addJavascriptInterface(JSCallback(), "tianshen")

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

    inner class JSCallback {

        @JavascriptInterface
        fun authCallBack(result: Boolean) {
            runOnUiThread {
                refreshCreditEvent()
                if (result) {
                    ToastUtil.showToast(mActivity, "认证成功!", Toast.LENGTH_LONG)
                    //发送event到homeFragment2 跳转到对应界面
                    EventBus.getDefault().post(getUserAuthStatus())
                    backActivity()
                } else {
                    ToastUtil.showToast(mActivity, "认证失败!", Toast.LENGTH_LONG)
                }
            }
        }

        @JavascriptInterface
        fun authCallBackStr(result: String) {
            runOnUiThread {
                ToastUtil.showToast(mActivity, result, Toast.LENGTH_LONG)
                refreshCreditEvent()
                backActivity()
            }
        }
    }

    //检查上一个页面是否为重定向页面并返回上一页页面
    fun goBack() {
        if (loadHistoryUrls.size > 1 && loadHistoryUrls.get(loadHistoryUrls.size - 2).contains("index.html")) {
            if (loadHistoryUrls.size == 2) {//如果第一个页面就是重定向 直接back

                backActivity()
            } else {
                //back 2 次
                wv_web.goBack()
                wv_web.goBack()
            }
            //因为是重定向 去掉后两个url
            loadHistoryUrls.removeAt(loadHistoryUrls.size - 1)
            loadHistoryUrls.removeAt(loadHistoryUrls.size - 1)
        } else {
            wv_web.goBack()
        }
        LogUtil.d("abcd", "size = " + loadHistoryUrls.size)
    }

    //检查 当前页面是否是认证成功页面
    private fun checkIsAuthSuccess(): Boolean {
        if (loadHistoryUrls.size > 0 && loadHistoryUrls[loadHistoryUrls.size - 1] != null && loadHistoryUrls[loadHistoryUrls.size - 1].contains("h5/sesame/se_order.html")) {
            refreshCreditEvent()
            backActivity()
            return true
        }
        return false
    }

    fun refreshCreditEvent() {
        EventBus.getDefault().post(RefreshCreditStatusEvent())
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            startActivity<MainActivity>()
            backActivity()
        }
        return super.onKeyDown(keyCode, event)
    }




}
