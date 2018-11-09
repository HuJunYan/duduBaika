package com.dudubaika.presenter.impl

import android.text.TextUtils
import com.dudubaika.base.App
import com.dudubaika.base.RxPresenter
import com.dudubaika.event.RecordStopAppEvent
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.IshaveNoReadMsgBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.MeContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.UserUtil
import org.json.JSONObject
import javax.inject.Inject

/**
 * 我的
 */
class MePresenter @Inject constructor():RxPresenter<MeContract.View>(),MeContract.Presenter{

    //app结束时间
    override fun stopAppTime(deviceId: String) {

        var sTime  =""
        val jsonObject = JSONObject()
        if (!TextUtils.isEmpty(UserUtil.getAppStartTime(App.instance))){
            sTime =  UserUtil.getAppStartTime(App.instance)
        }
        jsonObject.put("device_id", deviceId)
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

    //获取是否有未读消息
    override fun getMsgStatus() {

        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.getIsReadMsg(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<IshaveNoReadMsgBean>>())
                .compose(RxUtil.handleResult<IshaveNoReadMsgBean>())
                .subscribeWith(object : CommonSubscriber<IshaveNoReadMsgBean>(mView, true, ApiSettings.GET_IDNUM_INFO) {
                    override fun onNext(data: IshaveNoReadMsgBean) {
                        mView?.isHaveNoreadMsg(data)
                    }
                }))
    }

    override fun setRead(msg_id: String, is_read_all: String, table_identifier: String) {

        val jsonObject = JSONObject()
        jsonObject.put("msg_id",msg_id)
        jsonObject.put("is_read_all",is_read_all)
        jsonObject.put("table_identifier",table_identifier)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.setMsgRead(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView, true, ApiSettings.READ_MSG) {
                    override fun onNext(data: Any) {
                        super.onNext(data)
                        mView?.setReadSuccess()
                    }
                }))

    }



}