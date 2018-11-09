package com.dudubaika.presenter.impl

import com.dudubaika.base.App
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.VerifyHomeDataBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.model.http.ReviewApiManager
import com.dudubaika.presenter.contract.MineverifyContract
import com.dudubaika.presenter.contract.MyCollectionContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.UserUtil
import org.json.JSONObject
import javax.inject.Inject

class MyCollectionPresenter @Inject constructor()
    : RxPresenter<MyCollectionContract.View>(), MyCollectionContract.Presenter<MyCollectionContract.View> {
    override fun loadData() {
        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ReviewApiManager.getMycollection(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<VerifyHomeDataBean>>())
                .compose(RxUtil.handleResult<VerifyHomeDataBean>())
                .subscribeWith(object : CommonSubscriber<VerifyHomeDataBean>(mView!!, true) {
                    override fun onNext(t: VerifyHomeDataBean) {
                        mView?.loadDataComplete(t)
                    }
                }))
    }

}