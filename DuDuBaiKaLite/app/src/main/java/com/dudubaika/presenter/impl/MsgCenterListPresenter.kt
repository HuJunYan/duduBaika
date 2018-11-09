package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MsgCenterListBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.MsgCenterListContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 消息中心的presenter
 */
class MsgCenterListPresenter @Inject constructor() :RxPresenter<MsgCenterListContract.View>(),MsgCenterListContract.Presenter{



    /*
    *得到消息中心的消息
     */
    override fun getListData(now_page: String, page_size: String, table_identifier: String, table_page_offset: String) {

        val jsonObject = JSONObject()
        jsonObject.put("now_page", now_page)
        jsonObject.put("page_size", page_size)
        jsonObject.put("table_identifier", "")
        jsonObject.put("table_page_offset", "")
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getMsgCenterList(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<MsgCenterListBean>>())
                .compose(RxUtil.handleResult<MsgCenterListBean>())
                .subscribeWith(object : CommonSubscriber<MsgCenterListBean>(mView!!, true) {
                    override fun onNext(t: MsgCenterListBean) {
                        mView?.showDtaa(t)
                    }
                }))
    }
}