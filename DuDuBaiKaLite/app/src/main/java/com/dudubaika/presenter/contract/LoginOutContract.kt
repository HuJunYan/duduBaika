package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract

/**
 * Created by admin on 2018/2/27.
 */
class LoginOutContract {

    interface View:BaseContract.BaseView{

        fun logingOutComplete()

        fun finishActivity()
    }

    interface Presenter:BaseContract.BasePresenter<View>{
        fun loginOut()
    }
}