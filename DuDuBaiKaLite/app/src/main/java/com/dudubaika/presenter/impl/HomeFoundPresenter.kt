package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.HomeFoundBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.HomeFoundContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

/**
 * 发现首页网络请求
 */
class HomeFoundPresenter @Inject constructor(): RxPresenter<HomeFoundContract.View>(),HomeFoundContract.Presenter{

    //加载发现首页数据
    override fun loadData() {

        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.getFoundHomeData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<HomeFoundBean>>())
                .compose(RxUtil.handleResult<HomeFoundBean>())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : CommonSubscriber<HomeFoundBean>(mView, isShowDialogLoading = false,
                        isShowViewLoading = false, url = ApiSettings.HELP_CENTER) {
                    override fun onNext(t: HomeFoundBean) {
                        super.onNext(t)
                        mView?.showData(t)
                    }
                }))

    }

}