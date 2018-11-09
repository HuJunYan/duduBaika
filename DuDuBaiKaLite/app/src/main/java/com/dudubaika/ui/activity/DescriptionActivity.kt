package com.dudubaika.ui.activity

import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dudubaika.R
import com.dudubaika.model.bean.Repository
import com.dudubaika.util.StatusBarUtil
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.dudubaika.base.SimpleActivity
import kotlinx.android.synthetic.main.activity_des.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DescriptionActivity : SimpleActivity() {

    private var mOffset = 0
    private var mScrollY = 0
    private var mURL: String? = null
    private var mTitle: String? = null

    override fun getLayout(): Int = R.layout.activity_des

    override fun setStatusBar() {
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
    }

    override fun initView() {

        var repository = intent.extras.get("data") as Repository

        mURL = repository.html_url
        mTitle = repository.name

        initToolBar()
        initRefreshLayout()
        initWebView()
    }

    private fun initToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)

        tv_title_des.text = mTitle
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wv != null) {
            val parent = wv.parent as ViewGroup
            parent.removeView(wv)
            wv.removeAllViews()
            wv.destroy()
        }
    }

    private fun initRefreshLayout() {

        refreshLayout.isEnableLoadMore = false
        buttonBarLayout.alpha = 0f
        toolbar.setBackgroundColor(0)

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onRefresh(refreshlayout: RefreshLayout?) {
                doAsync {
                    Thread.sleep(1000)
                    uiThread {
                        refreshLayout.finishRefresh()
                    }
                }
            }
        })

        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0
            private val h = DensityUtil.dp2px(170f)
            private val color = ContextCompat.getColor(applicationContext, R.color.colorPrimary) and 0x00ffffff

            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                var scrollY = scrollY
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY)
                    mScrollY = if (scrollY > h) h else scrollY
                    buttonBarLayout.alpha = 1f * mScrollY / h
                    toolbar.setBackgroundColor(255 * mScrollY / h shl 24 or color)
                    parallax.translationY = (mOffset - mScrollY).toFloat()
                }
                lastScrollY = scrollY
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {//toolbar返回按钮监听
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {

    }

    private fun initWebView() {
        setWebViewSettings()
        setWebView()
    }

    private fun setWebViewSettings() {
        val webSettings = wv.settings
        // 打开页面时， 自适应屏幕
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        // 便页面支持缩放
        webSettings.javaScriptEnabled = true //支持js
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.domStorageEnabled = true//解决HTML显示不全的问题
        webSettings.setSupportZoom(true) //支持缩放
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
    }

    private fun setWebView() {
        wv.loadUrl(mURL)
        wv.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (url != null) {
                    view.loadUrl(url)
                }
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress.visibility = View.GONE
            }
        })
    }

}