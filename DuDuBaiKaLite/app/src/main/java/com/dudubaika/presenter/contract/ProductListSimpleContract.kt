package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.MsgListBean
import com.dudubaika.model.bean.ProductListSimpleBean


/**
 * 下款口子
 */
interface ProductListSimpleContract {

    interface View: BaseContract.BaseView {
        fun showData(data:ProductListSimpleBean?)
        fun showMsgList(data: MsgListBean)

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getData()
        fun getMsgListData()

    }
}