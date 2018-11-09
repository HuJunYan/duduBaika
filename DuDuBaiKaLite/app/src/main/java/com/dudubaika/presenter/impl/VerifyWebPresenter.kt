package com.dudubaika.presenter.impl

import com.dudubaika.base.App
import com.dudubaika.base.GlobalParams
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.model.http.ReviewApiManager
import com.dudubaika.presenter.contract.VerifyWebContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.UserUtil
import org.json.JSONObject
import javax.inject.Inject


class VerifyWebPresenter @Inject constructor()
    : RxPresenter<VerifyWebContract.View>(), VerifyWebContract.Presenter<VerifyWebContract.View> {

    override fun collectionArt(article_id: String) {
        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        jsonObject.put("article_id", article_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ReviewApiManager.collection(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(t: Any) {
                        mView?.collectionComplete()
                    }
                }))
    }

    override fun unCollection(article_id: String) {
        val jsonObject = JSONObject()
        jsonObject.put(GlobalParams.CUSTOMER_ID, UserUtil.getUserId(App.instance))
        jsonObject.put("article_id", article_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ReviewApiManager.unCollection(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(t: Any) {
                        mView?.unCollectionComplete()
                    }
                }))
    }

}
