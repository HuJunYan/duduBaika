package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MsgListBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.model.bean.ProductListSimpleBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.ProductListSimpleContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * 获取下款入口presenter
 */
class ProductListSimplePresenter @Inject constructor():RxPresenter<ProductListSimpleContract.View>(),ProductListSimpleContract.Presenter {

    //得到快讯列表消息
    override fun getMsgListData() {

        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getMsgList(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<MsgListBean>>())
                .compose(RxUtil.handleResult<MsgListBean>())
                .subscribeWith(object : CommonSubscriber<MsgListBean>(mView!!) {
                    override fun onNext(t: MsgListBean) {
                        super.onNext(t)
                        mView?.showMsgList(t)
                    }
                }))


    }

    override fun getData() {

        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getProductListSimple(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ProductListSimpleBean>>())
                .compose(RxUtil.handleResult<ProductListSimpleBean>())
                .subscribeWith(object : CommonSubscriber<ProductListSimpleBean>(mView!!) {
                    override fun onNext(t: ProductListSimpleBean) {
                        super.onNext(t)
                        mView?.showData(t)
                    }
                }))
    }
}