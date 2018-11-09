package com.dudubaika.ui.activity

import android.text.TextUtils
import android.view.KeyEvent
import com.dudubaika.R
import com.dudubaika.base.App
import com.dudubaika.base.SimpleActivity
import com.dudubaika.log.LogUtil
import com.dudubaika.util.ImageUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_advertising.*
import org.jetbrains.anko.startActivity
import java.util.concurrent.TimeUnit

class AdvertisingActivity : SimpleActivity() {

    private var mDefaultTime : Int =3
    //任务队列
    private var disposables: CompositeDisposable? = null
    //轮询时间间隔(时间单位分钟)
    private var mLoopInterval: Long = 1
    //轮询结束时间
    private var mLoopEndTime: Long = 3
    //跳转链接
    private var mJumpUrl :String?=null

    override fun getLayout(): Int = R.layout.activity_advertising

    companion object {
        var IMG_URL_KEY : String="img_url_key"
        var TIME_KEY : String="time_key"
        var JUMP_URL : String="jump_url"
    }

    override fun initView() {
        val imgUrl = intent.getStringExtra(IMG_URL_KEY)
        mLoopEndTime  = intent.getStringExtra(TIME_KEY).toLong()
        mJumpUrl  = intent.getStringExtra(JUMP_URL)
        defaultTitle="启动广告"
        if (!TextUtils.isEmpty(imgUrl)) {
            ImageUtil.loadWithCache(App.instance, ad, imgUrl, R.drawable.ic_error_close)
        }else{
            return
        }
        ad.setOnClickListener {
            disposables?.dispose()
            startActivity<AdverstH5Activity>(AdverstH5Activity.WEB_URL_TITLE to "活动详情"
            ,AdverstH5Activity.WEB_URL_KEY to mJumpUrl!!)
         finish()
        }

    }

    override fun initData() {
        startLoop()
    }

    private fun startLoop() {
        try {
             disposables = CompositeDisposable()
            disposables?.add(getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(getObserver()))
        } catch (e: Exception) {

        }
    }

    private fun getObservable(): Observable<out Long> {
        return Observable.interval(0, mLoopInterval, TimeUnit.SECONDS).take(mLoopEndTime)
    }


    private fun getObserver(): DisposableObserver<Long> {
        return object : DisposableObserver<Long>() {

            override fun onStart() {
                super.onStart()
                LogUtil.i("倒计时", "开始计时")
            }

            override fun onNext(t: Long) {
                LogUtil.i("倒计时", "倒计时$t")
                time.text = (mLoopEndTime-t).toString()+"S"

            }

            override fun onError(e: Throwable) {
                LogUtil.i("倒计时", e.toString().trim())
            }


            override fun onComplete() {
                LogUtil.i("倒计时", "计时结束")
                disposables?.dispose()
                goToMainActivity()
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        disposables?.dispose()
    }


    fun goToMainActivity(){
        startActivity<MainActivity>()
        AdvertisingActivity@ this.finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
            //屏蔽返回键
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}



