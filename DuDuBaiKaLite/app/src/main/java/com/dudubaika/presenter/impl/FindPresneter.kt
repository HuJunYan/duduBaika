package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.FindInitBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.UsersAuthLimitBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.FindContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

class FindPresneter @Inject constructor() :RxPresenter<FindContract.View>(),FindContract.Presenter{
    override fun getTopData() {
        //得到顶部标签数据
        val jsonObject = JSONObject()

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getInitSoftData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<FindInitBean>>())
                .compose(RxUtil.handleResult<FindInitBean>())
                .subscribeWith(object : CommonSubscriber<FindInitBean>(mView!!) {
                    override fun onNext(t: FindInitBean) {
                        super.onNext(t)
                        mView?.showInitData(t)
                    }
                }))

    }

    //得到排序数据
    override fun getSortData(current_page: Int, page_size: String, quota: String, lony_term: String, cycle: String, rate: String, mech: String, mark: String) {

        val jsonObject = JSONObject()
        jsonObject.put("current_page",current_page)
        jsonObject.put("page_size",page_size)

        jsonObject.put("quota",quota)
        jsonObject.put("lony_term",lony_term)
        jsonObject.put("cycle",cycle)
        jsonObject.put("rate",rate)

        jsonObject.put("mech",mech)
        jsonObject.put("mark",mark)

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getSoftData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<UsersAuthLimitBean>>())
                .compose(RxUtil.handleResult<UsersAuthLimitBean>())
                .subscribeWith(object : CommonSubscriber<UsersAuthLimitBean>(mView!!) {
                    override fun onNext(t: UsersAuthLimitBean) {
                        super.onNext(t)
                        mView?.showSortData(t)
                    }
                }))


    }


}