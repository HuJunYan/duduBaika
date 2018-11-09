package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.CreditCardDetailBean

interface CreditCardDetailContract{


    interface View:BaseContract.BaseView{

        fun getCreditCardDetailComplete(data: CreditCardDetailBean)

    }


    interface Presenter :BaseContract.BasePresenter<View>{

        fun getCreditCardDetail(credit_id:String)
    }
}