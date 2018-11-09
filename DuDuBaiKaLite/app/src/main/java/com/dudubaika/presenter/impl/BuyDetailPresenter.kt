package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.BuyDetailBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.NowApplyBean
import com.dudubaika.model.bean.ProductInfoBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.BuyDetailContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

class BuyDetailPresenter @Inject constructor(): RxPresenter<BuyDetailContract.View>(), BuyDetailContract.Presenter {


    override fun getProductDetailData(product_id: String,product_type: String) {
        val jsonObject = JSONObject()
        jsonObject.put("product_id", product_id)
        jsonObject.put("product_type", product_type)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.nowLoan(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ProductInfoBean>>())
                .compose(RxUtil.handleResult<ProductInfoBean>())
                .subscribeWith(object : CommonSubscriber<ProductInfoBean>(mView!!,true,true) {
                    override fun onNext(t: ProductInfoBean) {
                        super.onNext(t)
                        mView?.processProductDetailData(t)
                    }
                }))
    }

    //点击了 立即申请
    override fun nowApply(product_id: String) {
        val jsonObject = JSONObject()
        jsonObject.put("product_id", product_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.nowApply(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<NowApplyBean>>())
                .compose(RxUtil.handleResult<NowApplyBean>())
                .subscribeWith(object : CommonSubscriber<NowApplyBean>(mView!!,true) {
                    override fun onNext(data: NowApplyBean) {
                        mView?.processNowApplyData(data)
                    }
                }))
    }
}