package com.dudubaika.presenter.impl

import com.dudubaika.base.GlobalParams
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.CreditAssessBean
import com.dudubaika.model.bean.CreidtApplyBean
import com.dudubaika.model.bean.LastSmsTimeBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.CreditAssessmentContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.Tools
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/1/16.
 */
class CreditAssessmentPresenter @Inject constructor() : RxPresenter<CreditAssessmentContract.View>(), CreditAssessmentContract.Presenter<CreditAssessmentContract.View> {

    //手机信息
    override fun submitPhoneInfo(data: JSONObject, deviceId: String) {
        var jsonObject = JSONObject(data.toString())
        jsonObject.put("device_id", deviceId)
        val jsonObjectSigned = SignUtils.signJsonContainList(jsonObject, GlobalParams.USER_INFO_APP_LIST, GlobalParams.USER_INFO_MESSAGE_LIST)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.savePhoneInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(t: Any) {
                        mView?.processSubmitPhoneInfoResult()
                    }

                }))

    }

    //手机信息 上传
    override fun getLastSmsTime(isFirst: Boolean) {
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getLastSmsTime(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<LastSmsTimeBean>>())
                .compose(RxUtil.handleResult<LastSmsTimeBean>())
                .subscribeWith(object : CommonSubscriber<LastSmsTimeBean>(mView!!) {
                    override fun onNext(t: LastSmsTimeBean) {
                        super.onNext(t)
                        mView?.processLastSmsTime(t, isFirst)
                    }

                }))
    }

    override fun submitCredit() {
        val jsonObject = JSONObject()
        val root = Tools.isRoot()
        jsonObject.put("is_root", if (root) "1" else "2");
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.creditApply(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CreidtApplyBean>>())
                .compose(RxUtil.handleResult<CreidtApplyBean>())
                .subscribeWith(object : CommonSubscriber<CreidtApplyBean>(mView!!, true) {
                    override fun onNext(t: CreidtApplyBean) {
                        super.onNext(t)
                        mView?.processCreditApplyData(t)
                    }

                }))
    }

    override fun getCreditAssessData(isNeedJump: Boolean) {
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.creditAssess(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CreditAssessBean>>())
                .compose(RxUtil.handleResult<CreditAssessBean>())
                .subscribeWith(object : CommonSubscriber<CreditAssessBean>(mView!!,false,true) {

                    override fun onNext(t: CreditAssessBean) {
                        super.onNext(t)
                        mView?.getCreditAssessResult(t, isNeedJump)
                    }
                }))
    }


}