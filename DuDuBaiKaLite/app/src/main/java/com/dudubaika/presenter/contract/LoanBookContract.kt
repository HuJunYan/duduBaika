package com.dudubaika.presenter.contract

import com.dudubaika.base.BaseContract
import com.dudubaika.model.bean.MyLoadDetailBean

/**
 * 贷款账本
 */
interface LoanBookContract {

    interface View: BaseContract.BaseView{
        fun showData(data:MyLoadDetailBean?)
        fun changeStatusComplete()

    }

    interface Presenter:BaseContract.BasePresenter<View>{

        fun getDetailData()

        fun changeNoteStatus(product_id:String)
    }
}