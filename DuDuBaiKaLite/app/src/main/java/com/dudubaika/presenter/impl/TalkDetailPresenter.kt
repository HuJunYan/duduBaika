package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.TalkDetailBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.TalkDetailContact
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by 胡俊焰 on 2018/8/4.
 */
class TalkDetailPresenter @Inject constructor():RxPresenter<TalkDetailContact.View>(),TalkDetailContact.Presenter{

    //举报帖子
    override fun reportTalk(discuss_id: String, report_id: String) {

        val jsonObject = JSONObject()
        jsonObject.put("discuss_id",discuss_id)
        jsonObject.put("report_id",report_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.reportTalk(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                    }
                }))

    }

    override fun Talkdiss(discuss_id: String, comment_content: String) {
        //发表评论
        val jsonObject = JSONObject()
        jsonObject.put("discuss_id",discuss_id)
        jsonObject.put("comment_content",comment_content)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.dissTalk(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!,true) {
                    override fun onNext(t: Any) {
                        super.onNext(t)
                        mView?.dissTalkCompete()
                    }
                }))

    }

    //获取详情数据
    override fun getDeatilData(discuss_id:String) {
        val jsonObject = JSONObject()
        jsonObject.put("discuss_id",discuss_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getDetaliTalkData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<TalkDetailBean>>())
                .compose(RxUtil.handleResult<TalkDetailBean>())
                .subscribeWith(object : CommonSubscriber<TalkDetailBean>(mView!!) {
                    override fun onNext(t: TalkDetailBean) {
                        super.onNext(t)
                        mView?.showDetailData(t)
                    }
                }))


    }


}