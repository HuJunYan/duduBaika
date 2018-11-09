package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.CardMoneyListBean

interface TalkHomeContract{


    interface View:BaseContract.BaseView{

         fun showListData(data: CardMoneyListBean)
    }

    interface Presenter:BaseContract.BasePresenter<View>{

         fun loadListData(type:String)

    }


}