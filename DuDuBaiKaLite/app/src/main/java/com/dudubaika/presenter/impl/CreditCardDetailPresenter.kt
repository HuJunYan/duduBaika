package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.CreditCardDetailBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.CreditCardDetailContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 信用卡详情presenter
 */
class CreditCardDetailPresenter @Inject constructor():RxPresenter<CreditCardDetailContract.View>(),CreditCardDetailContract.Presenter{
    //得到信用卡详情数据
   override fun getCreditCardDetail(credit_id:String) {
        val jsonObject = JSONObject()
        jsonObject.put("credit_id",credit_id)

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getCreditCardDetail(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CreditCardDetailBean>>())
                .compose(RxUtil.handleResult<CreditCardDetailBean>())
                .subscribeWith(object : CommonSubscriber<CreditCardDetailBean>(mView!!) {
                    override fun onNext(t: CreditCardDetailBean) {
                        super.onNext(t)
                        mView?.getCreditCardDetailComplete(t)
                    }
                }))
    }


}