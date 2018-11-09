package com.dudubaika.presenter.impl

import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.dudubaika.base.App
import com.dudubaika.base.RxPresenter
import com.dudubaika.log.LogUtil
import com.dudubaika.model.bean.LoginBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.LoginContract
import com.dudubaika.util.*
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/2/25.
 * 登录presenter
 */
class LoginPresenter @Inject constructor():RxPresenter<LoginContract.View>(),LoginContract.Presenter{


    override fun getVerifyCode(mobile: String?, type: String?) {

        val jsonObject = JSONObject()
        jsonObject.put("mobile",mobile)
        jsonObject.put("type",type)
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

    override fun login(mobile: String?, type: String?, password: String?, verify_code: String?) {

        val jsonObject = JSONObject()
        jsonObject.put("mobile",mobile)
        jsonObject.put("type",type)
        jsonObject.put("password",password)
        jsonObject.put("verify_code",verify_code)
        val channel_id = Utils.getChannelId()
        jsonObject.put("channel_id", channel_id)
        var push_id= UserUtil.getUserJPushId(App.instance)
        if (TextUtils.isEmpty(push_id)){
            push_id = JPushInterface.getRegistrationID(App.instance)
        }
        jsonObject.put("push_id", push_id)
        EncryptUtil.encryptTwoPassword(jsonObject)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.login(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<LoginBean>>())
                .compose(RxUtil.handleResult<LoginBean>())
                .subscribeWith(object : CommonSubscriber<LoginBean>(mView!!) {
                    override fun onNext(data: LoginBean) {
                        super.onNext(data)
                        UserUtil.saveUserJPushId(App.instance,push_id)
                        mView?.loginComplete(data!!)
                    }

                    override fun onComplete() {
                        super.onComplete()
                    }
                }))

    }

}