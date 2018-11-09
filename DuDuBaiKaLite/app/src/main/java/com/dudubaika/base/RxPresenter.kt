package com.dudubaika.base

import android.app.Activity
import android.app.Fragment
import android.text.TextUtils
import com.dudubaika.event.RecordStopAppEvent
import com.dudubaika.event.RefreshCreditStatusEvent
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.AuthStatus
import com.dudubaika.model.bean.CreditAssessBean
import com.dudubaika.model.bean.LastSmsTimeBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.util.*
import com.moxie.client.manager.MoxieCallBack
import com.moxie.client.manager.MoxieCallBackData
import com.moxie.client.manager.MoxieContext
import com.moxie.client.manager.MoxieSDK
import com.moxie.client.model.MxParam
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import java.util.*

open class RxPresenter<T : BaseContract.BaseView> : BaseContract.BasePresenter<T> {

    var mView: T? = null
    var mCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: T) {
        this.mView = view
    }

    override fun detachView() {
        this.mView = null
        unSubscribe()
    }

    fun addSubscribe(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        } else {
            mCompositeDisposable?.add(disposable)
        }
    }

    fun unSubscribe() {
        mCompositeDisposable?.dispose()
    }


    /**
     * 埋点需求
     */
     fun dian(flag: String, product_id: String) {

        var mobile = UserUtil.getMobile(App.instance)
        val jsonObject = JSONObject()
        jsonObject.put("flag", flag)
        jsonObject.put("product_id", product_id)
        if (TextUtils.isEmpty(mobile)){
            mobile=""
        }
        jsonObject.put("mobile", mobile)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val requestBody = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.builePoint(requestBody)
                .compose(RxUtil.rxSchedulerHelper<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, false) {
                    override fun onNext(data: Any) {

                    }
                }))

    }

    /**
     * 埋点需求
     * 重载函数
     */
    fun dian(flag: String) {
GlobalParams
       dian(flag,"")
    }

    fun goWhere(){

    }

    /**
     * 得到用户认证状态
     * 根据状态跳转到对应的界面
     */
    fun getAuthStatush(isShowDialog:Boolean) {

        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getAuthStatus(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<AuthStatus>>())
                .compose(RxUtil.handleResult<AuthStatus>())
                .subscribeWith(object : CommonSubscriber<AuthStatus>(mView!!, isShowDialog) {
                    override fun onNext(data: AuthStatus) {
                        mView?.showStatus(data)
                    }
                }))
    }

    //更改魔蝎状态
    fun changeMoxieStatus() {

        val jsonObject = JSONObject()
          jsonObject.put("status","1")
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.changeMoxieStatus(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!,false) {
                    override fun onNext(t: Any) {
                    }

                }))
    }


    //提交风控
    fun gotoAuthFK(){

        val jsonObject = JSONObject()
        val root = Tools.isRoot()
        jsonObject.put("is_root", if (root) "1" else "2")
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.creditApply2(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(data: Any) {
                    }
                }))

    }



    //手机信息 上传
     fun getLastSmsTime2() {
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getLastSmsTime(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<LastSmsTimeBean>>())
                .compose(RxUtil.handleResult<LastSmsTimeBean>())
                .subscribeWith(object : CommonSubscriber<LastSmsTimeBean>(mView!!) {
                    override fun onNext(t: LastSmsTimeBean) {
                        super.onNext(t)
                    }

                }))
    }

    //app结束时间
    fun stopAppTime2(){
        var sTime  =""
        val jsonObject = JSONObject()
        if (!TextUtils.isEmpty(UserUtil.getAppStartTime(App.instance))){
            sTime =  UserUtil.getAppStartTime(App.instance)
        }
        jsonObject.put("device_id", UserUtil.getDeviceId(App.instance))
        jsonObject.put("start_time",sTime)
        jsonObject.put("end_time", (System.currentTimeMillis()/1000).toInt().toString())
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.stopApp(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, false) {
                    override fun onNext(data: Any) {
                    }
                }))
    }



}