package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.UsersAuthLimitBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.GetUserLoanContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 获取用户额度
 */
class GetUserLoanPresenter@Inject constructor():RxPresenter<GetUserLoanContract.View>(),GetUserLoanContract.Presenter{
    override fun getData() {
        val jsonObject = JSONObject()

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getUsersAuthLimit(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<UsersAuthLimitBean>>())
                .compose(RxUtil.handleResult<UsersAuthLimitBean>())
                .subscribeWith(object : CommonSubscriber<UsersAuthLimitBean>(mView!!) {
                    override fun onNext(t: UsersAuthLimitBean) {
                        super.onNext(t)
                        mView?.showData(t)
                    }
                }))
    }


}