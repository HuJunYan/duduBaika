package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.SetPwdBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.SetPwdContract
import com.dudubaika.util.EncryptUtil
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/2/26.
 * 设置密码
 */
class SetPwdPresenter @Inject constructor():RxPresenter<SetPwdContract.View>(),SetPwdContract.Presenter{
    override fun setPwd(mobile: String?, password: String?) {

        val jsonObject = JSONObject()
        jsonObject.put("mobile",mobile)
        jsonObject.put("password",password)
        EncryptUtil.encryptTwoPassword(jsonObject)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.setPwd(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<SetPwdBean>>())
                .compose(RxUtil.handleResult<SetPwdBean>())
                .subscribeWith(object : CommonSubscriber<SetPwdBean>(mView!!,true) {
                    override fun onNext(data: SetPwdBean) {
                        super.onNext(data)
                        mView?.setPwdComplete(data)
                    }

                    override fun onComplete() {
                        super.onComplete()
                    }

                }))
    }

}