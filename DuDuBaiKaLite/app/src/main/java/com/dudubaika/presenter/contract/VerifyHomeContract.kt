package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.VerifyHomeDataBean
import com.dudubaika.model.bean.VerifyProductDetailBean

interface VerifyHomeContract {

    interface View : BaseContract.BaseView {
        fun processVerifyHomeData(data: VerifyHomeDataBean?)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun getVerifyHomeData()

    }

}