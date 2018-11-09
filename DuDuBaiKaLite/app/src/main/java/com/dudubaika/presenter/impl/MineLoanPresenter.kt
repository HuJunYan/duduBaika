package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MineLoanBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.MineLoanContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 获取待还/贷款记录
 */
class MineLoanPresenter @Inject constructor():RxPresenter<MineLoanContract.View>(),MineLoanContract.Presenter {
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
                        mView?.changeComplete()
                    }
                }))
    }

    override fun getData(date_flag: String) {

        val jsonObject = JSONObject()
        jsonObject.put("date_flag",date_flag)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getMineLoan(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<MineLoanBean>>())
                .compose(RxUtil.handleResult<MineLoanBean>())
                .subscribeWith(object : CommonSubscriber<MineLoanBean>(mView!!) {
                    override fun onNext(data: MineLoanBean) {
                        mView?.showData(data)
                    }

                }))
    }
}