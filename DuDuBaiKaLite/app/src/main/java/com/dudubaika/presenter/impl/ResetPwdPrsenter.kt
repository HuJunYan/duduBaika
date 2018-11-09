package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.ForgetPwdContract
import com.dudubaika.util.EncryptUtil
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/2/26.
 * 重置密码presneter
 */
class ResetPwdPrsenter @Inject constructor() : RxPresenter<ForgetPwdContract.View>(), ForgetPwdContract.Presenter {
    override fun getVerifyCode(mobile: String?, type: String?) {
        val jsonObject = JSONObject()
        jsonObject.put("mobile", mobile)
        jsonObject.put("type", type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getVerifyCode(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!) {
                    override fun onNext(data: Any) {
                        mView?.getVCodeComplete()
                    }

                }))
    }

    override fun resetPwd(mobile: String?, password: String?, verify_code: String?) {


        val jsonObject = JSONObject()
        jsonObject.put("mobile", mobile)
        jsonObject.put("password", password)
        jsonObject.put("verify_code", verify_code)
        EncryptUtil.encryptTwoPassword(jsonObject)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.resetPwd(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(data: Any) {
                        super.onNext(data)
                        mView?.resetPwdComplete()
                    }

                    override fun onComplete() {
                        super.onComplete()
                    }

                }))
    }

}