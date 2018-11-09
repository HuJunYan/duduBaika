package com.dudubaika.presenter.impl

import com.dudubaika.base.GlobalParams
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.*
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.ApiSettings
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.HomeContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.Tools
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

class HomePresenter @Inject constructor()
    : RxPresenter<HomeContract.View>(), HomeContract.Presenter<HomeContract.View> {
    override fun getDialogForUser() {
        //得到弹窗信息
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getHomeDialogForUser(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<HomeDialogForUser>>())
                .compose(RxUtil.handleResult<HomeDialogForUser>())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : CommonSubscriber<HomeDialogForUser>(mView, false,
                        false, url = ApiSettings.HOME_POPUP) {
                    override fun onNext(t: HomeDialogForUser) {
                        super.onNext(t)
                        mView?.showDialogForUser(t)
                    }
                }))
    }

    //获取首页底部信息
    override fun getBottomInfo(isShowLoading: Boolean) {

        val jsonObject = JSONObject()

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getHomeButtomDialogData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<HomeButtomDialogBean>>())
                .compose(RxUtil.handleResult<HomeButtomDialogBean>())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : CommonSubscriber<HomeButtomDialogBean>(mView, isShowLoading,
                        isShowLoading, url = ApiSettings.HOME_POPUP) {
                    override fun onNext(t: HomeButtomDialogBean) {
                       super.onNext(t)
                        mView?.showHomeBottomData(t)
                    }
                }))

    }

    /**
     * 首页推荐位
     */
    override fun getBannerInfo(isShowLoading: Boolean) {

        val jsonObject = JSONObject()

        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getHomeTopInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<HomeTopInfo>>())
                .compose(RxUtil.handleResult<HomeTopInfo>())
                .subscribeOn(Schedulers.newThread())
                .subscribeWith(object : CommonSubscriber<HomeTopInfo>(mView, isShowDialogLoading = false,
                        isShowViewLoading = false, url = ApiSettings.GET_PRODUCT_LIST) {

                    override fun onNext(t: HomeTopInfo) {
                        if (isShowLoading) {
                            mView?.hideProgress()
                        }
                        mView?.showBannerInfo(t)
                    }
                }))

    }

    //手机信息
    fun submitPhoneInfo2(data: JSONObject, deviceId: String) {
        var jsonObject = JSONObject(data.toString())
        jsonObject.put("device_id", deviceId)
        val jsonObjectSigned = SignUtils.signJsonContainList(jsonObject, GlobalParams.USER_INFO_APP_LIST, GlobalParams.USER_INFO_MESSAGE_LIST)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.savePhoneInfo(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<Any>>())
                .compose(RxUtil.handleResult<Any>())
                .subscribeWith(object : CommonSubscriber<Any>(mView!!, true) {
                    override fun onNext(t: Any) {
                    }

                }))

    }

}