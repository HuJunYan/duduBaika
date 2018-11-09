package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.LoginOutContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/2/27.
 */
class LoginOutPresenter @Inject constructor():RxPresenter<LoginOutContract.View>(),LoginOutContract.Presenter{

    override fun loginOut() {
        //退出登录

        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.loginOut(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!,true) {
                    override fun onNext(data: Any) {
                        super.onNext(data)
                        mView?.logingOutComplete()
                    }

                    override fun onComplete() {
                        super.onComplete()

                    }

                }))
    }


}