package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.JpushDetailBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.JpushDetailContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 推送消息详情
 */
class JpushDetailPresenter @Inject constructor():RxPresenter<JpushDetailContract.View>(),JpushDetailContract.Presenter {



    override fun getMsgDetail(msg_id: String) {
        val jsonObject = JSONObject()
        jsonObject.put("msg_id",msg_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.getDetailJpush(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<JpushDetailBean>>())
                .compose(RxUtil.handleResult<JpushDetailBean>())
                .subscribeWith(object : CommonSubscriber<JpushDetailBean>(mView, true, ApiSettings.GET_MSG_INFO) {
                    override fun onNext(data: JpushDetailBean) {
                        super.onNext(data)
                        mView?.getMsgDetailData(data)
                    }
                }))
    }
}