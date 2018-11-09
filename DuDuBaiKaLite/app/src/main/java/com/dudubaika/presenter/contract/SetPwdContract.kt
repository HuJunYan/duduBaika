package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.SetPwdBean

/**
 * Created by admin on 2018/2/26.
 */
class SetPwdContract{

    interface View:BaseContract.BaseView{

        fun setPwdComplete(data: SetPwdBean?)

        fun finshActivity()
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun setPwd(mobile:String?,password:String?)
    }
}