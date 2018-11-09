package com.dudubaika.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.BaseActivity
import com.dudubaika.event.HomeDataRefreEvent
import com.dudubaika.event.RefreshCollectionEvent
import com.dudubaika.log.LogUtil
import com.dudubaika.presenter.contract.VerifyWebContract
import com.dudubaika.presenter.impl.VerifyWebPresenter
import com.dudubaika.util.StatusBarUtil
import com.dudubaika.util.ToastUtil
import com.dudubaika.util.UserUtil
import kotlinx.android.synthetic.main.activity_verify_web.*
import org.greenrobot.eventbus.EventBus

/**
 * 用于审核的H5页面(含有收藏文章功能)
 * Created by wang on 2017/12/5.
 */

class VerifyWebActivity : BaseActivity<VerifyWebPresenter>(), VerifyWebContract.View {

    var mStatus: String? = null
    var isCollection: Boolean = false
    var mUrl: String? = null
    var mArticleId: String? = null //文章的id 用于请求接口
    override fun getLayout(): Int = R.layout.activity_verify_web

    companion object {
        var VERIFY_WEB_URL_KEY = "verify_web_url_key"
        var VERIFY_WEB_ARTICLE_ID = "verify_web_article_id"
        var ARTICES_COLLECTED_STATUE = "artices_collected_statue"
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initPresenter() {
        mPresenter.attachView(this)
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    override fun showError(url: String, msg: String) {
    }

    override fun collectionComplete() {
        isCollection = true
        iv_web_icon.setImageResource(R.drawable.my_collecion)
        iv_web_icon.isClickable = true
        EventBus.getDefault().post(RefreshCollectionEvent())
    }

    override fun unCollectionComplete() {
        isCollection = false
        iv_web_icon.setImageResource(R.drawable.icon_web_collection)
        iv_web_icon.isClickable = true
        EventBus.getDefault().post(RefreshCollectionEvent())
    }


    override fun initView() {
        StatusBarUtil.setPaddingSmart(mActivity, tb_verify_web)
        iv_web_icon.setImageResource(R.drawable.icon_web_collection)
        tv_verify_web_back.setOnClickListener { checkCanGoback() }
        initWebView()
        iv_web_icon.setOnClickListener {
            if (!UserUtil.isLogin(mActivity)) {
                ToastUtil.showToast(mActivity, "请先登录")
                return@setOnClickListener
            }
            iv_web_icon.isClickable = false
            if (isCollection) {
                //取消
                mPresenter.unCollection(mArticleId!!)
            } else {
                mPresenter.collectionArt(mArticleId!!)
            }
            //刷新首页数据
            EventBus.getDefault().post(HomeDataRefreEvent())
        }

    }


    override fun initData() {
        mStatus = intent.getStringExtra(ARTICES_COLLECTED_STATUE)
        mUrl = intent.getStringExtra(VERIFY_WEB_URL_KEY)
        mArticleId = intent.getStringExtra(VERIFY_WEB_ARTICLE_ID)
        wv_web.loadUrl(mUrl)
        showCollectionStatue()
    }

    /**
     * 显示收藏按钮的状态
     */
    private fun showCollectionStatue() {

        if (!UserUtil.isLogin(App.instance)) {
            isCollection = false
        } else {
            if ("1" == mStatus) {
                isCollection = true
                iv_web_icon.setImageResource(R.drawable.my_collecion)
            }
        }

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
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        wv_web.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            //使用系统自带的浏览器下载
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {

            }
        }
        wv_web.setWebViewClient(object : WebViewClient() {
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
        })

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


    override fun onDestroy() {
        super.onDestroy()
        if (wv_web != null) {
            val parent = wv_web.parent as ViewGroup
            parent.removeView(wv_web)
            wv_web.removeAllViews()
            wv_web.destroy()
        }
    }
}