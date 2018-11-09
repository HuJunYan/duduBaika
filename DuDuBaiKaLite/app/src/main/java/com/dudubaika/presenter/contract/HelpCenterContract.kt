package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.HelpCenterBean

/**
 * Created by admin on 2018/1/30.
 */
class HelpCenterContract{

    interface View: BaseContract.BaseView {

        /**
         * 解析数据
         */
        fun processData(data: HelpCenterBean?)
    }

    interface Presenter: BaseContract.BasePresenter<View> {

        //获取预订单数据
        fun getData(type:String)
    }
}