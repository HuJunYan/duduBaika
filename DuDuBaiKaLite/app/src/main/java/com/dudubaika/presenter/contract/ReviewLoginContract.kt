package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.LoginBean

/**
 * Created by admin on 2018/2/25.
 */

class ReviewLoginContract {

    interface View : BaseContract.BaseView {
        fun loginCompelete(data: LoginBean)
        fun getVeryCodeResult(data: Any)
        fun finishActivity()
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun login(userName: String, password: String)
        fun getVeryCode(mobile: String, type: String)
    }
}