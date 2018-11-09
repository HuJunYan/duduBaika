package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.HomeButtomDialogBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.model.bean.UserAuthResultBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.ProductContract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import com.dudubaika.util.Tools
import org.json.JSONObject
import javax.inject.Inject

/**
 * 产品列表presenter
 */
class ProductPresenter@Inject constructor()
:RxPresenter<ProductContract.View>(),ProductContract.Presenter{

    override fun getButtomDialog() {
        //得到底部数据
        val jsonObject = JSONObject()
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getHomeButtomDialogData(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<HomeButtomDialogBean>>())
                .compose(RxUtil.handleResult<HomeButtomDialogBean>())
                .subscribeWith(object : CommonSubscriber<HomeButtomDialogBean>(mView!!) {
                    override fun onNext(t: HomeButtomDialogBean) {
                        super.onNext(t)
                        mView?.getButtomDialogData(t)
                    }
                }))

    }

    override fun getProductInfo(product_type: String, current_page: String, page_size: String) {

        val jsonObject = JSONObject()
        jsonObject.put("product_type",product_type)
        jsonObject.put("current_page",current_page)
        jsonObject.put("page_size",page_size)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getProductInfoList(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ProductInfoListBean>>())
                .compose(RxUtil.handleResult<ProductInfoListBean>())
                .subscribeWith(object : CommonSubscriber<ProductInfoListBean>(mView!!) {
                    override fun onNext(t: ProductInfoListBean) {
                        super.onNext(t)
                        mView?.getPrductInfo(t)
                    }
                }))


    }

}