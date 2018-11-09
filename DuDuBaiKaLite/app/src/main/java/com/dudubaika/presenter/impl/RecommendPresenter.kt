package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.UserAuthResultBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.RecommendContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.Tools
import org.json.JSONObject
import javax.inject.Inject

class RecommendPresenter @Inject constructor()
    : RxPresenter<RecommendContract.View>(), RecommendContract.Presenter<RecommendContract.View> {
    override fun getUserAuthResult() {
        val jsonObject = JSONObject()
        val root = Tools.isRoot()
        jsonObject.put("is_root", if (root) "1" else "2");
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getUserAuthResult(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<UserAuthResultBean>>())
                .compose(RxUtil.handleResult<UserAuthResultBean>())
                .subscribeWith(object : CommonSubscriber<UserAuthResultBean>(mView!!) {
                    override fun onNext(t: UserAuthResultBean) {
                        super.onNext(t)
                        mView?.processData(t)
                    }

                }))
    }

}