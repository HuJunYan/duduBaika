package com.dudubaika.presenter.impl


import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.HelpCenterBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.HelpCenterContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by admin on 2018/1/30.
 * 帮助中心
 */
class HelpcenterPresenter @Inject constructor(): RxPresenter<HelpCenterContract.View>(), HelpCenterContract.Presenter {

    /**
     *得到数据
     */
    override fun getData(type: String) {

        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)

        addSubscribe(ApiManager.helpCenter(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<HelpCenterBean>>())
                .compose(RxUtil.handleResult<HelpCenterBean>())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : CommonSubscriber<HelpCenterBean>(mView, isShowDialogLoading = false,
                        isShowViewLoading = false, url = ApiSettings.HELP_CENTER) {
                    override fun onNext(t: HelpCenterBean) {
                       super.onNext(t)
                        mView?.processData(t)
                    }
                }))

    }
}