package com.dudubaika.presenter.impl

import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.ProductInfoListBean
import com.dudubaika.model.bean.SetPwdBean
import com.dudubaika.model.http.ApiManager
import com.dudubaika.model.http.CommonSubscriber
import com.dudubaika.presenter.contract.TagProductContract
import com.dudubaika.util.EncryptUtil
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

class TagProductPresenter @Inject constructor():RxPresenter<TagProductContract.View>(),TagProductContract.Presenter {

    //得到标签数据
    override fun getTagProductList(id: String, current_page: String, page_size: String) {

        val jsonObject = JSONObject()
        jsonObject.put("tag_id",id)
        jsonObject.put("page_size",page_size)
        jsonObject.put("current_page",current_page)
        EncryptUtil.encryptTwoPassword(jsonObject)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ApiManager.getTagProductList(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<ProductInfoListBean>>())
                .compose(RxUtil.handleResult<ProductInfoListBean>())
                .subscribeWith(object : CommonSubscriber<ProductInfoListBean>(mView!!,true) {
                    override fun onNext(data: ProductInfoListBean) {
                        super.onNext(data)
                        mView?.showTagProductListDat(data)
                    }
                })
        )

    }



}