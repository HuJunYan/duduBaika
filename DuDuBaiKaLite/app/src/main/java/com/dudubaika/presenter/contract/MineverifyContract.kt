package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract

interface MineverifyContract {

    interface View : BaseContract.BaseView {
        fun processExitLoginResult()
    }

    interface Presenter<in T> : BaseContract.BasePresenter<T> {
        fun loadMine()
        fun exitLogin()
        fun checkUserConfig()
    }

}

