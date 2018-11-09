package com.dudubaika.model.http

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.dudubaika.base.App
import com.dudubaika.base.BaseContract
import com.dudubaika.event.ServiceErrorEvent
import com.dudubaika.event.ServiceUpdateEvent
import com.dudubaika.event.TokenErrorEvent
import com.dudubaika.ext.toast
import com.dudubaika.log.LogUtil
import com.dudubaika.ui.activity.LoginActivity
import com.dudubaika.util.LoadingUtil
import com.dudubaika.util.ToastUtil
import com.dudubaika.util.UserUtil
import io.reactivex.subscribers.ResourceSubscriber
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.getStackTraceString
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class CommonSubscriber<T>(private var mView: BaseContract.BaseView?) :
        ResourceSubscriber<T>() {

    //错误信息
    private var mErrorMsg = ""
    //请求的url
    private var mUrl = ""
    //是否显示dialog的loading
    private var mIsShowDialogLoading: Boolean = false
    //是否显示页面中的loading(页面中的loadingView需要预埋到各个页面中)
    private var mIsShowViewLoading: Boolean = false

    constructor(view: BaseContract.BaseView?, isShowDialogLoading: Boolean = false,
                isShowViewLoading: Boolean , url: String = "") : this(view) {
        this.mIsShowDialogLoading = isShowDialogLoading
        this.mIsShowViewLoading = isShowViewLoading
        this.mUrl = url
    }

    constructor(view: BaseContract.BaseView?, url: String) : this(view) {
        this.mView = view
        this.mUrl = url
    }

    constructor(view: BaseContract.BaseView?, isShowLoading: Boolean = true, url: String = "") : this(view) {
        this.mView = view
        this.mIsShowDialogLoading = isShowLoading
        this.mUrl = url
    }

    override fun onStart() {
        super.onStart()
        if ((mIsShowDialogLoading && mView is Fragment) || (mIsShowDialogLoading && mView is Activity)) {
            if (mView is Activity) {
                LoadingUtil.showLoadingDialog(mView as Activity, false)
            } else {
                LoadingUtil.showLoadingDialog(mView as Fragment, false)
            }
        }
        if (mIsShowViewLoading) {
            mView?.showProgress()
        }
    }

    override fun onNext(t: T) {
        hideProgress()
        if (mIsShowViewLoading) {
            mView?.hideProgress()
        }
    }

    override fun onComplete() {
        hideProgress()
    }

    override fun onError(e: Throwable?) {

        if (mView == null) {
            return
        }
        //判断是哪种异常
        when (e) {
        // 自定义异常
            is ApiException -> {
                var responseData = ""
                try {
                    responseData = e.getResponseData()
                    val json = JSONObject(responseData)
                    mErrorMsg = json.optString("msg")
                    val code = json.optInt("code")
                    //判断自定义的状态码
                    when (code) {
                    //token 错误
                        ApiCode.TOKEN_ERROR -> {
                            mErrorMsg = ""
                            UserUtil.clearUser(App.instance)
                            EventBus.getDefault().post(TokenErrorEvent())
                            val intent = Intent(App.instance, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            App.instance.startActivity(intent)
                        }
                    //检查更新
                        ApiCode.UPDATE -> {
                            mErrorMsg = ""
                            val data = json.getJSONObject("data")
                            val download_url = data.getString("download_url")
                            EventBus.getDefault().post(ServiceUpdateEvent(download_url))
                        }
                    //系统维护
                        ApiCode.ERROR -> {
                            EventBus.getDefault().post(ServiceErrorEvent("系统维护"))
                        }
                    }
                } catch (e: Exception) {
                    mErrorMsg = "服务器数据错误"
                    hideProgress()
                    e.printStackTrace()
                }

            }
        //网络异常
            is HttpException, is ConnectException, is UnknownHostException,
            is SocketTimeoutException, is NoRouteToHostException -> {
                mErrorMsg = "手机网络不顺畅，请检查后再试~"
                /*if (mUrl == ApiSettings.OCR_IDCARD) {
                    mErrorMsg = "识别身份证信息失败"
                    EventBus.getDefault().post(IdCardFailureEvent())
                }*/
                if (mUrl == ApiSettings.BURIED_POINT) { //如果是埋点错误，就不提示
                    mErrorMsg = ""
                }
            }
        //其他错误
            else -> {
                mErrorMsg = "未知错误ヽ(≧Д≦)ノ"
                LogUtil.i(ApiManager.HTTP_LOG_TAG, "error =" + e?.getStackTraceString())

            }

        }

        //显示toast
        if (!TextUtils.isEmpty(mErrorMsg)) {
            if (mView is Activity) {
                if (e is ApiException) {
                    try {
                        val json = JSONObject(e.getResponseData())
                        val code = json.optInt("code")
                        //系统维护中,  不弹出toast
                        if (code != ApiCode.ERROR) {
                            ToastUtil.showToast(mView as Activity, mErrorMsg)
                        }
                    } catch (e: Exception) {
                        ToastUtil.showToast(mView as Activity, mErrorMsg)
                    }
                } else {
                    ToastUtil.showToast(mView as Activity, mErrorMsg)
                }
            } else if (mView is Fragment) {
                ToastUtil.showToast(mView as Fragment, mErrorMsg)
            } else {
                App.instance.toast(mErrorMsg)
            }
        }

       /* if (mView is Activity) {
            ToastUtil.showToast(mView as Activity, mErrorMsg)
        } else if (mView is Fragment) {
            ToastUtil.showToast(mView as Fragment, mErrorMsg)
        } else {
            App.instance.toast(mErrorMsg)
        }*/

        //隐藏dialog样式的loading
        hideProgress()

        //回调view层
        mView!!.showError(mUrl, mErrorMsg)
    }

    private fun hideProgress() {
        if (mIsShowDialogLoading) {
            if (mView is Activity) {
                LoadingUtil.hideLoadingDialog(mView as Activity)
            } else {
                LoadingUtil.hideLoadingDialog(mView as Fragment)
            }
            mIsShowDialogLoading = false
        }
    }


}