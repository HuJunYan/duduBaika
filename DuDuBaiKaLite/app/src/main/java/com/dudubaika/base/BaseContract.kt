package com.dudubaika.base

import com.dudubaika.model.bean.AuthStatus


interface BaseContract {

    interface BaseView {

        fun showProgress()

        fun hideProgress()

        fun showError(url: String, msg: String)

        fun showStatus(data:AuthStatus)=Unit

    }

    interface BasePresenter<in T> {

        /**
         * 绑定

         * @param view view
         */
        fun attachView(view: T)

        /**
         * 解绑
         */
        fun detachView()
    }

}
