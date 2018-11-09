package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.VerifyHomeDataBean

interface MyCollectionContract {

    interface View : BaseContract.BaseView {
        fun loadDataComplete(data: VerifyHomeDataBean?)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun loadData()
    }

}