package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.LoginBean

/**
 * Created by admin on 2018/2/25.
 */

class LoginContract{

     interface View : BaseContract.BaseView {
         fun loginComplete(data:LoginBean?)
         fun getVCodeComplete()
         fun finishActivity()

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getVerifyCode(mobile:String?,type:String?)
        fun login(mobile:String?,type:String?,password :String?,verify_code :String?)

    }
}
