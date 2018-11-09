package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.CreditCardListBean
import com.dudubaika.model.bean.HomeCreditCardBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.CreditCardListContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 信用卡列表Presenter
 */
class CreditCardListPresenter @Inject constructor() :RxPresenter<CreditCardListContract.View>(),CreditCardListContract.Presenter{

    override fun getCreditCardList(bank_id: String, ability_id: String, type: String) {

        val jsonObject = JSONObject()
        jsonObject.put("bank_id",bank_id)
        jsonObject.put("ability_id",ability_id)
        jsonObject.put("type",type)

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getCreditCardListData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CreditCardListBean>>())
                .compose(RxUtil.handleResult<CreditCardListBean>())
                .subscribeWith(object : CommonSubscriber<CreditCardListBean>(mView!!) {
                    override fun onNext(t: CreditCardListBean) {
                        super.onNext(t)
                        mView?.getCreditListComplete(t)
                    }
                }))
    }

}