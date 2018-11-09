package com.dudubaika.presenter.impl

import com.dudubaika.base.BaseContract
import com.dudubaika.base.RxPresenter
import com.dudubaika.model.bean.CardDetailBean
import com.dudubaika.model.bean.MyHttpResponse
import com.dudubaika.model.bean.VerifyProductDetailBean
import com.dudubaika.model.http.*
import com.dudubaika.presenter.contract.SetPwdContract
import com.dudubaika.presenter.contract.TransficCardConstract
import com.dudubaika.util.RequsetUtil
import com.dudubaika.util.RxUtil
import com.dudubaika.util.SignUtils
import org.json.JSONObject
import javax.inject.Inject

/**
 * Created by lenovo on 2018/3/28.
 */
class TransficCardPresenter @Inject constructor() : RxPresenter<TransficCardConstract.View>(), TransficCardConstract.Presenter {
    override fun setCard_id(product_id: String) {
        val jsonObject = JSONObject()
        jsonObject.put("product_id", product_id)
        val jsonObjectSigned = SignUtils.signJsonNotContainList(jsonObject)
        val body = RequsetUtil.getRequestBody(jsonObjectSigned)
        addSubscribe(ReviewApiManager.getTransficCard(body)
                .compose(RxUtil.rxSchedulerHelper<MyHttpResponse<VerifyProductDetailBean>>())
                .compose(RxUtil.handleResult<VerifyProductDetailBean>())
                .subscribeWith(object : CommonSubscriber<VerifyProductDetailBean>(mView!!, isShowDialogLoading = false,
                        isShowViewLoading = false, url = ReviewApiSettings.GET_REVIEW_PRODUCT_DETAIL) {
                    override fun onNext(t: VerifyProductDetailBean) {
                        super.onNext(t)
                        mView?.show_product_detail(t)
                    }

                }))


    }
}