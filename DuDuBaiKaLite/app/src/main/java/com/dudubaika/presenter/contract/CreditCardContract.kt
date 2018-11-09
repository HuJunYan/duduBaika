package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.HomeCreditCardBean

interface CreditCardContract{

    interface View:BaseContract.BaseView{

        //得到信用卡首页数据
        fun getCreditDataComplete(data: HomeCreditCardBean)
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun  getCreditCardData()
    }
}