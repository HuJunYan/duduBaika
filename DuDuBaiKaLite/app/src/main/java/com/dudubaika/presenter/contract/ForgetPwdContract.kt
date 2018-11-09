package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract

/**
 * Created by admin on 2018/2/26.
 */
class ForgetPwdContract{

    interface View:BaseContract.BaseView{

        fun getVCodeComplete()
        fun resetPwdComplete()
        fun finshActivity()
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun resetPwd(mobile:String?,password:String?,verify_code:String?)
        fun getVerifyCode(mobile: String?, type: String?)
    }
}