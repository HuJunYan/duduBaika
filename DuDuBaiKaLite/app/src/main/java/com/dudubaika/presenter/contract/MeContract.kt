package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.IshaveNoReadMsgBean

interface MeContract{

    interface View:BaseContract.BaseView{
        fun isHaveNoreadMsg(data: IshaveNoReadMsgBean)
        fun setReadSuccess()

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getMsgStatus()
        fun setRead(msg_id: String, is_read_all: String, table_identifier: String)
        fun stopAppTime(deviceId :String)
    }
}