package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.RegistBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.RegistContract
import com.dudubaika.util.*
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/2/25.
 * 注册presenter
 */
class RegisterPresenter @Inject constructor():RxPresenter<RegistContract.View>(),RegistContract.Presenter{

    override fun getVeryCode(mobile:String?, type:String?) {
        val jsonObject = JSONObject()
        jsonObject.put("mobile",mobile)
        jsonObject.put("type",type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getVerifyCode(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!,true) {
                    override fun onNext(data: Any) {
                        mView?.getVeryCodeComplete()
                    }

                }))

    }

    override fun regist(mobile: String?, verify_code: String, password: String?) {

        val jsonObject = JSONObject()
        jsonObject.put("mobile",mobile)
        jsonObject.put("verify_code",verify_code)
        jsonObject.put("password",password)

        val channel_id = Utils.getChannelId()

        jsonObject.put("channel_id", channel_id)
        EncryptUtil.encryptTwoPassword(jsonObject)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.reregist(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<RegistBean>>())
                .compose(RxUtil.handleResult<RegistBean>())
                .subscribeWith(object : CommonSubscriber<RegistBean>(mView!!,true) {
                    override fun onNext(data: RegistBean) {
                        super.onNext(data)
                        mView?.registComplete(data)
                    }

                    override fun onComplete() {
                        super.onComplete()
                    }
                }))
    }


}