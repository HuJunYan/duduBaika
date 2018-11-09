package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.BannerListBean
import com.dudubaika.model.bean.ProductListBean
import com.dudubaika.model.bean.ProductRecommentBean
import com.dudubaika.model.bean.UserAuthResultBean

interface RepositoriesRusultContract {

    interface View : BaseContract.BaseView {
        fun showProductRecommentTop(data: ProductRecommentBean)
        fun showProduct(data: ProductListBean)
        fun onUserAuthResult(data: UserAuthResultBean)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun productRecommend()
        fun getProductList(product_type: String)
        fun getUserAuthResult()
    }

}