package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.CardMoneyListBean
import com.dudubaika.model.bean.HomeButtomDialogBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.TalkHomeContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 论坛首页（信用卡+借款）
 *
 */
class TalkHomePresenter @Inject constructor() :RxPresenter<TalkHomeContract.View>(),TalkHomeContract.Presenter{
    override fun loadListData(type: String) {

        val jsonObject = JSONObject()
        jsonObject.put("type",type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getHomeTalkListData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<CardMoneyListBean>>())
                .compose(RxUtil.handleResult<CardMoneyListBean>())
                .subscribeWith(object : CommonSubscriber<CardMoneyListBean>(mView!!) {
                    override fun onNext(t: CardMoneyListBean) {
                        super.onNext(t)
                        mView?.showListData(t)
                    }
                }))

    }

}