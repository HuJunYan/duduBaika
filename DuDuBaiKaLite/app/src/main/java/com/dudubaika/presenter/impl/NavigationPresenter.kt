package com.dudubaika.presenter.impl

import android.app.Activity
import android.text.TextUtils
import com.dudubaika.base.App
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.AdvertisingBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.UpgradeBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.NavigationContract
import com.dudubaika.util.*
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

class NavigationPresenter @Inject constructor()  : RxPresenter<NavigationContract.View>(), NavigationContract.Presenter<NavigationContract.View> {
    //app开始时间
    override fun startAppTime(deviceId: String) {
        val startTime= (System.currentTimeMillis()/1000).toInt().toString()
        UserUtil.saveAppStartTime(App.instance,startTime)
        val jsonObject = JSONObject()
        jsonObject.put("device_id", deviceId)
        jsonObject.put("start_time",startTime )

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.startApp(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, false) {
                    override fun onNext(data: Any) {
                    }
                }))
    }


    override fun getAdverst() {
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.startAdvertise(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<AdvertisingBean>>())
                .compose(RxUtil.handleResult<AdvertisingBean>())
                .subscribeWith(object : CommonSubscriber<AdvertisingBean>(mView!!) {
                    override fun onNext(data: AdvertisingBean) {
                        super.onNext(data)
                        mView?.getAdverstData(data)
                    }
                }))

    }

    override fun checkUpdate() {
        val version = Utils.getVersion(App.instance)
        val channel_id = Utils.getChannelId()
        val device_id = UserUtil.getDeviceId(mView as Activity)

        val jsonObject = JSONObject()
        jsonObject.put("app_type", "1")
        jsonObject.put("current_version", version)
        jsonObject.put("channel_id", channel_id)
        jsonObject.put("device_id", device_id)

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.checkUpgrade(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<UpgradeBean>>())
                .compose(RxUtil.handleResult<UpgradeBean>())
                .subscribeWith(object : CommonSubscriber<UpgradeBean>(mView!!) {
                    override fun onNext(data: UpgradeBean) {
                        super.onNext(data)
                        mView?.checkUpdateResult(data)
                    }
                }))
    }





}