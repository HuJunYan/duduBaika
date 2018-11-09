package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.LoginBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.model.http.ReviewApiManager
import com.dudubaika.presenter.contract.ReviewLoginContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

class ReviewLoginPresenter @Inject constructor() : RxPresenter<ReviewLoginContract.View>(), ReviewLoginContract.Presenter {

    override fun login(mobile: String, code: String) {

        val jsonObject = JSONObject()
        jsonObject.put("mobile", mobile)
        jsonObject.put("code", code)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ReviewApiManager.login(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<LoginBean>>())
                .compose(RxUtil.handleResult<LoginBean>())
                .subscribeWith(object : CommonSubscriber<LoginBean>(mView!!, true) {
                    override fun onNext(data: LoginBean) {
                        super.onNext(data)
                        mView?.loginCompelete(data!!)
                    }

                    override fun onComplete() {
                        super.onComplete()
                        mView?.finishActivity()
                    }
                }))
    }


    override fun getVeryCode(mobile: String, type: String) {

        val jsonObject = JSONObject()
        jsonObject.put("mobile", mobile)
        jsonObject.put("type", type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ReviewApiManager.getVeryCode(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(data: Any) {
                        super.onNext(data)
                        mView?.getVeryCodeResult(data!!)
                    }
                }))

    }


}