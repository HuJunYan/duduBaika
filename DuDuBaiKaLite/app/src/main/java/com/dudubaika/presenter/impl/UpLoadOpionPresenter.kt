package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.OpionContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/2/27.
 */
class UpLoadOpionPresenter @Inject constructor() : RxPresenter<OpionContract.View>(), OpionContract.Presenter {
    override fun upLoadOpion(feed_content: String?, mobile: String?) {
        val jsonObject = JSONObject()

        jsonObject.put("feed_content", feed_content)
        jsonObject.put("mobile", mobile)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.upLoadOpion(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(data: Any) {
                        super.onNext(data)
                        mView?.upLoadComplete()
                    }
                }))
    }

}