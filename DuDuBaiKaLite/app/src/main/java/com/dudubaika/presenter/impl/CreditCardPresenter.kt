package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.CreditCardDetailBean
import com.dudubaika.model.bean.HomeCreditCardBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.CreditCardContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

class CreditCardPresenter @Inject constructor() : RxPresenter<CreditCardContract.View>(),CreditCardContract.Presenter{

    //得到信用卡首页数据
    override fun getCreditCardData() {
        val jsonObject = JSONObject()

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getCreditCardData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<HomeCreditCardBean>>())
                .compose(RxUtil.handleResult<HomeCreditCardBean>())
                .subscribeWith(object : CommonSubscriber<HomeCreditCardBean>(mView!!) {
                    override fun onNext(t: HomeCreditCardBean) {
                        super.onNext(t)
                        mView?.getCreditDataComplete(t)
                    }
                }))
    }



}