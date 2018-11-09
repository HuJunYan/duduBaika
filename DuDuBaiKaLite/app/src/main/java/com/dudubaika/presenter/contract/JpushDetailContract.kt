package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.JpushDetailBean

interface JpushDetailContract {

    interface View:BaseContract.BaseView{

        fun  getMsgDetailData(data: JpushDetailBean)

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getMsgDetail(msg_id:String)

    }
}