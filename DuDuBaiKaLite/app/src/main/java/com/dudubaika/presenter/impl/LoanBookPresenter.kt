package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.MyLoadDetailBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.LoanBookContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 贷款账本presenter
 */
class LoanBookPresenter @Inject constructor():RxPresenter<LoanBookContract.View>(),LoanBookContract.Presenter {
    override fun changeNoteStatus(product_id: String) {
        val jsonObject = JSONObject()
        jsonObject.put("product_id",product_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.changeLoanStatus(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView, true, ApiSettings.GET_IDNUM_INFO) {
                    override fun onNext(data: Any) {
                        mView?.changeStatusComplete()
                    }
                }))
    }

    override fun getDetailData() {
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.getUsersLoanInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<MyLoadDetailBean>>())
                .compose(RxUtil.handleResult<MyLoadDetailBean>())
                .subscribeWith(object : CommonSubscriber<MyLoadDetailBean>(mView, true, ApiSettings.GET_IDNUM_INFO) {
                    override fun onNext(data: MyLoadDetailBean) {
                        mView?.showData(data)
                    }
                }))
    }
}