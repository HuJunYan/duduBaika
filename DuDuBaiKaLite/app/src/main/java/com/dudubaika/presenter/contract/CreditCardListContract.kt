package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.CreditCardListBean

//信用卡列表
interface CreditCardListContract{

    interface View:BaseContract.BaseView{

        //得到信用卡首页数据
        fun getCreditListComplete(data: CreditCardListBean)
    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun  getCreditCardList(bank_id:String,ability_id:String,type :String)
    }
}