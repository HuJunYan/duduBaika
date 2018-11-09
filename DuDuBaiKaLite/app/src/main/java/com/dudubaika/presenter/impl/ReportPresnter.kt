package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.ReportBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.ReportContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

//举报
class ReportPresnter @Inject constructor(): RxPresenter<ReportContract.View>(),ReportContract.Presenter{

    //获取举报信息
    override fun getReportInfo(discuss_id:String) {
        val jsonObject = JSONObject()
        jsonObject.put("discuss_id",discuss_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getReportInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ReportBean>>())
                .compose(RxUtil.handleResult<ReportBean>())
                .subscribeWith(object : CommonSubscriber<ReportBean>(mView!!) {
                    override fun onNext(t: ReportBean) {
                        super.onNext(t)
                        mView?.getReportInfoComplete(t)
                    }
                }))

    }

    //举报帖子
    override fun reportTalk(discuss_id: String, report_id: String) {
        val jsonObject = JSONObject()
        jsonObject.put("discuss_id",discuss_id)
        jsonObject.put("report_id",report_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.reportTalk(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        mView?.reportComplete()
                    }
                }))
    }


}