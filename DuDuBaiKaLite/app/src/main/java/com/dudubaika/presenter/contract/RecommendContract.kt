package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.UserAuthResultBean

interface RecommendContract {
    interface View : BaseContract.BaseView {
        fun processData(userAuthResultBean: UserAuthResultBean)
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun getUserAuthResult()
    }

}