package com.dudubaika.presenter.impl

import com.dudubaika.base.App
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.*
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.model.http.ReviewApiManager
import com.dudubaika.presenter.contract.VerifyHomeContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.UserUtil
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import javax.inject.Inject

class VerifyHomePresenter @Inject constructor()
    : RxPresenter<VerifyHomeContract.View>(), VerifyHomeContract.Presenter<VerifyHomeContract.View> {
//    override fun setCard_id(product_id: String?) {
//        val jsonObject = JSONObject()
//        jsonObject.put("product_id",product_id)
//        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
//        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
//        addSubscribe(ReviewApiManager.getTransficCard(body)
//                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<VerifyProductDetailBean>>())
//                .compose(RxUtil.handleResult<VerifyProductDetailBean>())
//                .subscribeWith(object : CommonSubscriber<VerifyProductDetailBean>(mView!!,true) {
//                    override fun onNext(t: VerifyProductDetailBean) {
//                        super.onNext(t)
//                        mView?.setCard_id_Complete(t)
//                    }
//
//                }))
//    }

    override fun getVerifyHomeData() {

        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ReviewApiManager.getVerifyView(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<VerifyHomeDataBean>>())
                .compose(RxUtil.handleResult<VerifyHomeDataBean>())
                .subscribeWith(object : CommonSubscriber<VerifyHomeDataBean>(mView!!, true) {
                    override fun onNext(t: VerifyHomeDataBean) {
                        super.onNext(t)

                        doAsync {
                            Thread.sleep(1000)
                            uiThread { mView?.processVerifyHomeData(t) }
                        }
                    }
                }))
    }


}
