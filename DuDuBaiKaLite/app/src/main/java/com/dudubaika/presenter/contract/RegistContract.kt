package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.RegistBean

/**
 * Created by admin on 2018/2/25.
 */
class RegistContract{

    interface View:BaseContract.BaseView{

        fun getVeryCodeComplete()

        fun registComplete(data: RegistBean)

        fun finishActivity()

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getVeryCode(mobile:String?, type:String?)

        fun regist(mobile:String?,verify_code:String,password :String?)
    }
}