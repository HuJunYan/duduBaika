package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.NowApplyBean
import com.dudubaika.model.bean.ProductInfoBean

object BuyDetailContract {

    interface View : BaseContract.BaseView {
        fun processProductDetailData(data: ProductInfoBean?)
        fun processNowApplyData(data: NowApplyBean?)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun getProductDetailData(product_id: String,product_type: String)
        fun nowApply(product_id: String)
    }

}